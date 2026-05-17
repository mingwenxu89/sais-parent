package cn.iocoder.yudao.framework.common.biz.system.oauth2.dto;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * OAuth2.0 access token creation Request DTO
 *
 * @author Yudao Source Code
 */
@Data
public class OAuth2AccessTokenCreateReqDTO implements Serializable {

 /**
     * User ID
 */
    @NotNull(message = "user ID cannot be empty")
 private Long userId;
 /**
     * User type
 */
    @NotNull(message = "user type cannot be empty")
    @InEnum(value = UserTypeEnum.class, message = "user type must be {value}")
 private Integer userType;
 /**
     * client number
 */
    @NotNull(message = "client ID cannot be empty")
 private String clientId;
 /**
     * Authorization scope
 */
 private List<String> scopes;

}
