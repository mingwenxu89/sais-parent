package cn.iocoder.yudao.module.system.api.social.dto;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Social binding Request DTO, use code authorization code
 *
 * @author Yudao Source Code
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserUnbindReqDTO {

    /**
     * User ID
     */
    @NotNull(message = "User ID cannot be empty")
    private Long userId;
    /**
     * User type
     */
    @InEnum(UserTypeEnum.class)
    @NotNull(message = "User type cannot be empty")
    private Integer userType;

    /**
     * Types of social platforms
     */
    @InEnum(SocialTypeEnum.class)
    @NotNull(message = "The type of social platform cannot be empty")
    private Integer socialType;

    /**
     * openid of social platform
     */
    @NotEmpty(message = "The openid of the social platform cannot be empty")
    private String openid;

}
