package cn.iocoder.yudao.module.infra.framework.file.core.client.s3;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.module.infra.framework.file.core.client.AbstractFileClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

/**
 * File client based on S3 protocol, realizing cloud services such as MinIO, Alibaba Cloud, Tencent Cloud, Qiniu Cloud, Huawei Cloud, etc.
 *
 * @author Yudao Source Code
 */
public class S3FileClient extends AbstractFileClient<S3FileClientConfig> {

    private static final Duration EXPIRATION_DEFAULT = Duration.ofHours(24);

    private S3Client client;
    private S3Presigner presigner;

    public S3FileClient(Long id, S3FileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // Complete domain
        if (StrUtil.isEmpty(config.getDomain())) {
            config.setDomain(buildDomain());
        }
        // Initialize S3 client
        // Priority: configured region > region parsed from endpoint > default us-east-1
        String regionStr = resolveRegion();
        Region region = Region.of(regionStr);
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(config.getAccessKey(), config.getAccessSecret()));
        URI endpoint = URI.create(buildEndpoint());
        S3Configuration serviceConfiguration = S3Configuration.builder() // Path-style visit
                .pathStyleAccessEnabled(Boolean.TRUE.equals(config.getEnablePathStyleAccess()))
                .chunkedEncodingEnabled(false) // Disable chunked encoding, see https://t.zsxq.com/kBy57
                .build();
        client = S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .endpointOverride(endpoint)
                .serviceConfiguration(serviceConfiguration)
                .build();
        presigner = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .endpointOverride(endpoint)
                .serviceConfiguration(serviceConfiguration)
                .build();
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        // ConstructPutObjectRequest
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(config.getBucket())
                .key(path)
                .contentType(type)
                .contentLength((long) content.length)
                .build();
        // Upload file
        client.putObject(putRequest, RequestBody.fromBytes(content));
        // splice return path
        return presignGetUrl(path, null);
    }

    @Override
    public void delete(String path) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(config.getBucket())
                .key(path)
                .build();
        client.deleteObject(deleteRequest);
    }

    @Override
    public byte[] getContent(String path) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(config.getBucket())
                .key(path)
                .build();
        return IoUtil.readBytes(client.getObject(getRequest));
    }

    @Override
    public String presignPutUrl(String path) {
        return presigner.presignPutObject(PutObjectPresignRequest.builder()
                .signatureDuration(EXPIRATION_DEFAULT)
                .putObjectRequest(b -> b.bucket(config.getBucket()).key(path)).build())
                .url().toString();
    }

    @Override
    public String presignGetUrl(String url, Integer expirationSeconds) {
        // 1. Convert URL to path
        String path = StrUtil.removePrefix(url, config.getDomain() + "/");
        path = HttpUtils.decodeUtf8(HttpUtils.removeUrlQuery(path));

        // 2.1 Scenario 1: Public access: no signature required
        // Considering the compatibility of old versions, signing must be performed when config.getEnablePublicAccess() is false.
        if (!BooleanUtil.isFalse(config.getEnablePublicAccess())) {
            return config.getDomain() + "/" + path;
        }

        // 2.2 Scenario 2: Private access: Generate GET pre-signed URL
        String finalPath = path;
        Duration expiration = expirationSeconds != null ? Duration.ofSeconds(expirationSeconds) : EXPIRATION_DEFAULT;
        URL signedUrl = presigner.presignGetObject(GetObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .getObjectRequest(b -> b.bucket(config.getBucket()).key(finalPath)).build())
                .url();
        return signedUrl.toString();
    }

    /**
     * Build the accessed Domain address based on bucket + endpoint
     *
     * @return Domain address
     */
    private String buildDomain() {
        // If it is already http or https, no splicing will be performed. Mainly adapted to MinIO
        if (HttpUtil.isHttp(config.getEndpoint()) || HttpUtil.isHttps(config.getEndpoint())) {
            return StrUtil.format("{}/{}", config.getEndpoint(), config.getBucket());
        }
        // Alibaba Cloud, Tencent Cloud, and Huawei Cloud are all suitable. Qiniu Cloud is special and must have a custom domain name
        return StrUtil.format("https://{}.{}", config.getBucket(), config.getEndpoint());
    }

    /**
     * Node address completion protocol header
     *
     * @return Node address
     */
    private String buildEndpoint() {
        // If it is already http or https, it will not be spliced.
        if (HttpUtil.isHttp(config.getEndpoint()) || HttpUtil.isHttps(config.getEndpoint())) {
            return config.getEndpoint();
        }
        return StrUtil.format("https://{}", config.getEndpoint());
    }

    /**
     * Resolve AWS Regions
     * Priority: configured region > region parsed from endpoint > default us-east-1
     *
     * @return locale string
     */
    private String resolveRegion() {
        // 1. If region is configured, use it directly
        if (StrUtil.isNotEmpty(config.getRegion())) {
            return config.getRegion();
        }

        // 2.1 Try to parse region from endpoint
        String endpoint = config.getEndpoint();
        if (StrUtil.isEmpty(endpoint)) {
            return "us-east-1";
        }

        // 2.2 Remove protocol header (http:// or https://)
        String host = endpoint;
        if (HttpUtil.isHttp(endpoint) || HttpUtil.isHttps(endpoint)) {
            try {
                host = URI.create(endpoint).getHost();
            } catch (Exception e) {
                // Parsing failed, using default value
                return "us-east-1";
            }
        }
        if (StrUtil.isEmpty(host)) {
            return "us-east-1";
        }

        // 3.1 AWS S3 format: S3.us-west-2.amazonaws.com or S3.amazonaws.com
        if (host.contains("amazonaws.com")) {
            // Matches the format S3.{region}.amazonaws.com
            if (host.startsWith("s3.") && host.contains(".amazonaws.com")) {
                String regionPart = host.substring(3, host.indexOf(".amazonaws.com"));
                if (StrUtil.isNotEmpty(regionPart) && !regionPart.equals("accelerate")) {
                    return regionPart;
                }
            }
            // S3.amazonaws.com or S3-accelerate.amazonaws.com use default value
            return "us-east-1";
        }
        // 3.2 Alibaba Cloud OSS format: oss-cn-beijing.aliyuncs.com
        if (host.contains(S3FileClientConfig.ENDPOINT_ALIYUN)) {
            // Match oss-{region}.aliyuncs.com format
            if (host.startsWith("oss-") && host.contains("." + S3FileClientConfig.ENDPOINT_ALIYUN)) {
                String regionPart = host.substring(4, host.indexOf("." + S3FileClientConfig.ENDPOINT_ALIYUN));
                if (StrUtil.isNotEmpty(regionPart)) {
                    return regionPart;
                }
            }
        }
        // 3.3 Tencent Cloud COS format: cos.ap-shanghai.myqcloud.com
        if (host.contains(S3FileClientConfig.ENDPOINT_TENCENT)) {
            // Match cos.{region}.myqcloud.com format
            if (host.startsWith("cos.") && host.contains("." + S3FileClientConfig.ENDPOINT_TENCENT)) {
                String regionPart = host.substring(4, host.indexOf("." + S3FileClientConfig.ENDPOINT_TENCENT));
                if (StrUtil.isNotEmpty(regionPart)) {
                    return regionPart;
                }
            }
        }

        // 3.4 In other cases (MinIO, Qiniu Cloud, etc.), use the default value
        return "us-east-1";
    }

}
