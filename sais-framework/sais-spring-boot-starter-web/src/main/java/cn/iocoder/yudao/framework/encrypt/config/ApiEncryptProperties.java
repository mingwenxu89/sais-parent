package cn.iocoder.yudao.framework.encrypt.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * HTTP API encryption and decryption configuration
 *
 * @author Yudao Source Code
 */
@ConfigurationProperties(prefix = "yudao.api-encrypt")
@Validated
@Data
public class ApiEncryptProperties {

    /**
     * Whether to turn on
     */
    @NotNull(message = "whether to enable or not cannot be empty")
    private Boolean enable;

    /**
     * Request Header (response Header) name
     *
     * 1. If the request Header is not empty, it means that the request parameters have been encrypted by the "front end" and need to be decrypted by the "back end"
     * 2. If the response Header is not empty, it means that the response result has been encrypted by the "backend" and needs to be decrypted by the "frontend"
     */
    @NotEmpty(message = "request Header (response Header) name cannot be empty")
    private String header = "X-Api-Encrypt";

    /**
     * Symmetric encryption algorithm, used for request/response encryption and decryption
     *
     * Currently supported
     * [Symmetric encryption]:
     *      1. {@link cn.hutool.crypto.symmetric.SymmetricAlgorithm#AES}
     * 2. {@link cn.hutool.crypto.symmetric.SM4#ALGORITHM_NAME} (requires secondary development by yourself, low cost)
     * 【Asymmetric Encryption】
     *      1. {@link cn.hutool.crypto.asymmetric.AsymmetricAlgorithm#RSA}
     * 2. {@link cn.hutool.crypto.asymmetric.SM2} (requires secondary development by yourself, low cost)
     *
     * @see <a href="https://help.aliyun.com/zh/ssl-certificate/what-are-a-public-key-and-a-private-key">What are public keys and private keys? </a>
     */
    @NotEmpty(message = "symmetric encryption algorithm cannot be empty")
    private String algorithm;

    /**
     * Requested decryption key
     *
     * Note:
     * 1. If it is [symmetric encryption], its "backend" corresponds to the "key". Correspondingly, the "front end" also corresponds to the "key".
     * 2. If it is [asymmetric encryption], its "backend" corresponds to the "private key". Correspondingly, the "front end" corresponds to the "public key". (Important!!!)
     */
    @NotEmpty(message = "request decryption secret key cannot be empty")
    private String requestKey;

    /**
     * Response encryption key
     *
     * Note:
     * 1. If it is [symmetric encryption], its "backend" corresponds to the "key". Correspondingly, the "front end" also corresponds to the "key".
     * 2. If it is [asymmetric encryption], its "backend" corresponds to the "public key". Correspondingly, the "front end" corresponds to the "private key". (Important!!!)
     */
    @NotEmpty(message = "response encryption secret key cannot be empty")
    private String responseKey;

}
