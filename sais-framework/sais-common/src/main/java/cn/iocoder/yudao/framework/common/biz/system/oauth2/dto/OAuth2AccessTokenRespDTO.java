package cn.iocoder.yudao.framework.common.biz.system.oauth2.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OAuth2.0 access token information Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class OAuth2AccessTokenRespDTO implements Serializable {

 /**
     * access token
 */
 private String accessToken;
 /**
     * refresh token
 */
 private String refreshToken;
 /**
     * User ID
 */
 private Long userId;
 /**
     * User type
 */
 private Integer userType;
 /**
     * Expiration time
 */
 private LocalDateTime expiresTime;

}
