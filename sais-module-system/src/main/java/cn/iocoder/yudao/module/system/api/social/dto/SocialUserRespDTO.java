package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Social User Response DTO
 *
 * @author Yudao Source Code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserRespDTO {

    /**
     * openid of social user
     */
    private String openid;
    /**
     * Nickname of social user
     */
    private String nickname;
    /**
     * social useravatar
     */
    private String avatar;

    /**
     * Associated user ID
     */
    private Long userId;

}
