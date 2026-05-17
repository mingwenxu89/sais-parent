package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;

/**
 * Mobile phone information of WeChat applet Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class SocialWxPhoneNumberInfoRespDTO {

    /**
     * The mobile phone ID bound by the user (foreign mobile phone IDs will have area codes)
     */
    private String phoneNumber;

    /**
     * Mobile phone ID without area code
     */
    private String purePhoneNumber;
    /**
     * area code
     */
    private String countryCode;

}
