package cn.iocoder.yudao.framework.common.biz.system.oauth2.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * OAuth2.0 access token verification Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class OAuth2AccessTokenCheckRespDTO implements Serializable {

 /**
     * User ID
 */
 private Long userId;
 /**
     * User type
 */
 private Integer userType;
 /**
     * User information
 */
 private Map<String, String> userInfo;
 /**
     * Tenant number
 */
 private Long tenantId;
 /**
     * array of authorization scopes
 */
 private List<String> scopes;
 /**
     * Expiration time
 */
 private LocalDateTime expiresTime;

}
