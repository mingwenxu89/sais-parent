package cn.iocoder.yudao.module.agri.framework.iot;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttClientSslConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AWS IoT Core MQTT3 client using HiveMQ client + X.509 certificate TLS authentication.
 */
@Slf4j
public class AwsIotMqttClient {

    private final Mqtt3AsyncClient client;
    private final AwsIotProperties properties;
    private final Map<String, MqttMessageHandler> topicHandlers = new LinkedHashMap<>();

    public AwsIotMqttClient(AwsIotProperties awsIotProperties) throws Exception {
        this.properties = awsIotProperties;

        KeyManagerFactory kmf = buildKeyManagerFactory();
        TrustManagerFactory tmf = buildTrustManagerFactory();
        logCertInfo();

        MqttClientSslConfig sslConfig = MqttClientSslConfig.builder()
                .keyManagerFactory(kmf)
                .trustManagerFactory(tmf)
                .protocols(Collections.singletonList("TLSv1.2"))
                .build();

        this.client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(properties.getClientId())
                .serverHost(properties.getEndpoint())
                .serverPort(properties.getPort())
                .sslConfig(sslConfig)
                .automaticReconnectWithDefaultConfig()
                .addConnectedListener(ctx -> {
                    log.info("[AwsIotMqttClient] Connected to {}, resubscribing {} topic(s)",
                            properties.getEndpoint(), topicHandlers.size());
                    resubscribeAll();
                })
                .addDisconnectedListener(ctx ->
                        log.warn("[AwsIotMqttClient] Disconnected [{}]", ctx.getCause().getClass().getSimpleName(), ctx.getCause()))
                .buildAsync();

        log.info("[AwsIotMqttClient] Connecting to {}:{} with clientId={}",
                properties.getEndpoint(), properties.getPort(), properties.getClientId());
        try {
            client.connectWith()
                    .cleanSession(true)
                    .keepAlive(60)
                    .send()
                    .get(30, TimeUnit.SECONDS);
            log.info("[AwsIotMqttClient] Connected to AWS IoT Core");
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            log.warn("[AwsIotMqttClient] Initial connect failed (will retry): {} [{}]",
                    cause.getMessage(), cause.getClass().getSimpleName(), e);
        }
    }

    public void publish(String topic, String payload) {
        client.publishWith()
                .topic(topic)
                .payload(payload.getBytes(StandardCharsets.UTF_8))
                .qos(MqttQos.AT_LEAST_ONCE)
                .send()
                .whenComplete((pubAck, ex) -> {
                    if (ex != null) log.error("[AwsIotMqttClient] Publish failed on {}", topic, ex);
                    else log.debug("[AwsIotMqttClient] Published to {}: {}", topic, payload);
                });
    }

    public void subscribe(String topicFilter, MqttMessageHandler handler) {
        topicHandlers.put(topicFilter, handler);
        if (client.getState().isConnected()) {
            doSubscribe(topicFilter, handler);
        } else {
            log.warn("[AwsIotMqttClient] Not connected; {} registered, will subscribe on reconnect", topicFilter);
        }
    }

    private void logCertInfo() {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            try (FileInputStream in = new FileInputStream(properties.getCertPath())) {
                X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
                log.info("[AwsIotMqttClient] Using certificate: subject={}, issuer={}, validUntil={}",
                        cert.getSubjectX500Principal().getName(),
                        cert.getIssuerX500Principal().getName(),
                        cert.getNotAfter());
            }
        } catch (Exception e) {
            log.warn("[AwsIotMqttClient] Could not read certificate info", e);
        }
    }

    public void disconnect() {
        client.disconnect()
                .whenComplete((v, ex) -> {
                    if (ex != null) log.error("[AwsIotMqttClient] Error disconnecting", ex);
                    else log.info("[AwsIotMqttClient] Disconnected from AWS IoT Core");
                });
    }

    public String getTopicPrefix() {
        return properties.getTopicPrefix();
    }

    private void resubscribeAll() {
        topicHandlers.forEach(this::doSubscribe);
    }

    private void doSubscribe(String topicFilter, MqttMessageHandler handler) {
        client.subscribeWith()
                .topicFilter(topicFilter)
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(publish -> {
                    byte[] bytes = publish.getPayload()
                            .map(buf -> { byte[] a = new byte[buf.remaining()]; buf.duplicate().get(a); return a; })
                            .orElse(new byte[0]);
                    handler.onMessage(publish.getTopic().toString(), bytes);
                })
                .send()
                .whenComplete((subAck, ex) -> {
                    if (ex != null) log.error("[AwsIotMqttClient] Subscribe failed for {}", topicFilter, ex);
                    else log.info("[AwsIotMqttClient] Subscribed to {}", topicFilter);
                });
    }

    // ---- TLS helpers (cert/key loading unchanged from Paho version) ----

    private TrustManagerFactory buildTrustManagerFactory() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate caCert;
        try (FileInputStream caIn = new FileInputStream(properties.getCaCertPath())) {
            caCert = (X509Certificate) cf.generateCertificate(caIn);
        }
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);
        return tmf;
    }

    private KeyManagerFactory buildKeyManagerFactory() throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(buildDeviceKeyStore(), "".toCharArray());
        return kmf;
    }

    private KeyStore buildDeviceKeyStore() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate deviceCert;
        try (FileInputStream certIn = new FileInputStream(properties.getCertPath())) {
            deviceCert = (X509Certificate) cf.generateCertificate(certIn);
        }

        java.security.PrivateKey privateKey;
        try (FileInputStream keyIn = new FileInputStream(properties.getPrivateKeyPath())) {
            byte[] keyBytes = keyIn.readAllBytes();
            String keyStr = new String(keyBytes);
            boolean isPkcs1 = keyStr.contains("BEGIN RSA PRIVATE KEY");
            keyStr = keyStr.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = java.util.Base64.getDecoder().decode(keyStr);
            if (isPkcs1) {
                // Wrap PKCS#1 into PKCS#8 envelope
                byte[] hdr = {0x30, (byte) 0x82, 0, 0, 0x30, 0x0d,
                        0x06, 0x09, 0x2a, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xf7, 0x0d, 0x01, 0x01, 0x01,
                        0x05, 0x00, 0x04, (byte) 0x82, 0, 0};
                int total = hdr.length - 4 + decoded.length;
                hdr[2] = (byte) ((total >> 8) & 0xff);
                hdr[3] = (byte) (total & 0xff);
                hdr[hdr.length - 2] = (byte) ((decoded.length >> 8) & 0xff);
                hdr[hdr.length - 1] = (byte) (decoded.length & 0xff);
                byte[] pkcs8 = new byte[hdr.length + decoded.length];
                System.arraycopy(hdr, 0, pkcs8, 0, hdr.length);
                System.arraycopy(decoded, 0, pkcs8, hdr.length, decoded.length);
                privateKey = java.security.KeyFactory.getInstance("RSA")
                        .generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(pkcs8));
            } else {
                java.security.spec.PKCS8EncodedKeySpec spec = new java.security.spec.PKCS8EncodedKeySpec(decoded);
                try {
                    privateKey = java.security.KeyFactory.getInstance("RSA").generatePrivate(spec);
                } catch (Exception e) {
                    privateKey = java.security.KeyFactory.getInstance("EC").generatePrivate(spec);
                }
            }
        }

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("cert", deviceCert);
        ks.setKeyEntry("key", privateKey, "".toCharArray(), new java.security.cert.Certificate[]{deviceCert});
        return ks;
    }
}
