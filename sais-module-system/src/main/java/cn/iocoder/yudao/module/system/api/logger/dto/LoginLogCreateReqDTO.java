package cn.iocoder.yudao.module.system.api.logger.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Login log creation Request DTO
 *
 * @author Yudao Source Code
 */
@Data
public class LoginLogCreateReqDTO {

    /**
     * Log type
     */
    @NotNull(message = "Log type cannot be empty")
    private Integer logType;
    /**
     * link tracking ID
     */
    private String traceId;

    /**
     * User ID
     */
    private Long userId;
    /**
     * User type
     */
    @NotNull(message = "User type cannot be empty")
    private Integer userType;
    /**
     * User account
     *
     * It is no longer mandatory to verify that username is not empty, because when Member social login, there is currently no username (mobile)!
     */
    private String username;

    /**
     * Login results
     */
    @NotNull(message = "Login result cannot be empty")
    private Integer result;

    /**
     * User IP
     */
    @NotEmpty(message = "User IP cannot be empty")
    private String userIp;
    /**
     * Browser UserAgent
     *
     * Allow empty, reason: When the job expires and logs out, the UserAgent cannot be passed
     */
    private String userAgent;

}
