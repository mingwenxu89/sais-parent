package cn.iocoder.yudao.module.infra.framework.file.core.client.s3;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * Configuration class for S3 file client
 *
 * @author Yudao Source Code
 */
@Data
public class S3FileClientConfig implements FileClientConfig {

    public static final String ENDPOINT_QINIU = "qiniucs.com";
    public static final String ENDPOINT_ALIYUN = "aliyuncs.com";
    public static final String ENDPOINT_TENCENT = "myqcloud.com";
    public static final String ENDPOINT_VOLCES = "volces.com"; // Volcano Cloud (bytes)

    /**
     * Node address
     * 1. MinIO: https://www.iocoder.cn/Spring-Boot/MinIO. For example, http://127.0.0.1:9000
     * 2. Alibaba Cloud: https://help.aliyun.com/document_detail/31837.html
     * 3. Tencent Cloud: https://cloud.tencent.com/document/product/436/6224
     * 4. Qiniu Cloud: https://developer.qiniu.com/kodo/4088/S3-access-domainname
     * 5. Huawei Cloud: https://console.huaweicloud.com/apiexplorer/#/endpoint/OBS
     * 6. Volcano cloud: https://www.volcengine.com/docs/6349/107356
     */
    @NotNull(message = "endpoint cannot be empty")
    private String endpoint;
    /**
     * Custom domain name
     * 1. MinIO: configured through Nginx
     * 2. Alibaba Cloud: https://help.aliyun.com/document_detail/31836.html
     * 3. Tencent Cloud: https://cloud.tencent.com/document/product/436/11142
     * 4. Qiniu Cloud: https://developer.qiniu.com/kodo/8556/set-the-custom-source-domain-name
     * 5. Huawei Cloud: https://support.huaweicloud.com/usermanual-obs/obs_03_0032.html
     * 6. Volcano cloud: https://www.volcengine.com/docs/6349/128983
     */
    @URL(message = "domain must be in URL format")
    private String domain;
    /**
     * Storage Bucket
     */
    @NotNull(message = "bucket cannot be empty")
    private String bucket;

    /**
     * Access Key
     * 1. MinIO：https://www.iocoder.cn/Spring-Boot/MinIO
     * 2. Alibaba Cloud: https://ram.console.aliyun.com/manage/ak
     * 3. Tencent Cloud: https://console.cloud.tencent.com/cam/capi
     * 4. Qiniu Cloud: https://portal.qiniu.com/user/key
     * 5. Huawei Cloud: https://support.huaweicloud.com/qs-obs/obs_qs_0005.html
     * 6. Volcano Cloud: https://console.volcengine.com/iam/keymanage/
     */
    @NotNull(message = "accessKey cannot be empty")
    private String accessKey;
    /**
     * Visit Secret
     */
    @NotNull(message = "accessSecret cannot be empty")
    private String accessSecret;

    /**
     * Whether to enable PathStyle access
     */
    @NotNull(message = "enablePathStyleAccess cannot be empty")
    private Boolean enablePathStyleAccess;

    /**
     * Is it publicly accessible?
     *
     * true: public access, accessible to everyone
     * false: private access, only the configured accessKey can access
     */
    @NotNull(message = "Whether public access cannot be empty")
    private Boolean enablePublicAccess;

    /**
     * area
     * 1. AWS S3: https://docs.aws.amazon.com/general/latest/gr/S3.html For example, us-east-1, us-west-2
     * 2. MinIO: You can fill in any value, usually use us-east-1
     * 3. Alibaba Cloud: No need to fill in, it will be automatically recognized
     * 4. Tencent Cloud: No need to fill in, it will be automatically recognized
     * 5. Qiniu Cloud: No need to fill in, it will be automatically recognized
     * 6. Huawei Cloud: No need to fill in, it will be automatically recognized
     * 7. Volcano Cloud: No need to fill in, it will be automatically recognized
     */
    private String region;

    @SuppressWarnings("RedundantIfStatement")
    @AssertTrue(message = "domain cannot be empty")
    @JsonIgnore
    public boolean isDomainValid() {
        // If it is Qiniu, it must contain domain
        if (StrUtil.contains(endpoint, ENDPOINT_QINIU) && StrUtil.isEmpty(domain)) {
            return false;
        }
        return true;
    }

}
