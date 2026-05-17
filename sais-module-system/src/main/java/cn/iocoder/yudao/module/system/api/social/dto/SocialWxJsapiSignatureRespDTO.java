package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;

/**
 * WeChat official account JSAPI signature Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class SocialWxJsapiSignatureRespDTO {

    /**
     * appId of WeChat official account
     */
    private String appId;
    /**
     * anonymous string
     */
    private String nonceStr;
    /**
     * Timestamp
     */
    private Long timestamp;
    /**
     * URL
     */
    private String url;
    /**
     * signature
     */
    private String signature;

}
