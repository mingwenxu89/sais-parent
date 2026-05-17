package cn.iocoder.yudao.module.system.service.auth;

import cn.iocoder.yudao.module.system.controller.admin.auth.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;

import jakarta.validation.Valid;

/**
 * Management backend authentication service API
 *
 * Provide users with the ability to log in and log out
 *
 * @author Yudao Source Code
 */
public interface AdminAuthService {

    /**
     * Verify account + password. If passed, return the user
     *
     * @param username Account
     * @param password Password
     * @return User
     */
    AdminUserDO authenticate(String username, String password);

    /**
     * Account login
     *
     * @param reqVO Login information
     * @return Login results
     */
    AuthLoginRespVO login(@Valid AuthLoginReqVO reqVO);

    /**
     * Log out based on token
     *
     * @param token token
     * @param logType Logout type
     */
    void logout(String token, Integer logType);

    /**
     * Send SMS captcha
     *
     * @param reqVO Send request
     */
    void sendSmsCode(AuthSmsSendReqVO reqVO);

    /**
     * SMS login
     *
     * @param reqVO Login information
     * @return Login results
     */
    AuthLoginRespVO smsLogin(AuthSmsLoginReqVO reqVO);

    /**
     * Quick social login, use code authorization code
     *
     * @param reqVO Login information
     * @return Login results
     */
    AuthLoginRespVO socialLogin(@Valid AuthSocialLoginReqVO reqVO);

    /**
     * Refresh access token
     *
     * @param refreshToken Refresh Token
     * @return Login results
     */
    AuthLoginRespVO refreshToken(String refreshToken);

    /**
     * User registration
     *
     * @param createReqVO Registered user
     * @return Registration results
     */
    AuthLoginRespVO register(AuthRegisterReqVO createReqVO);

    /**
     * reset password
     *
     * @param reqVO Captcha information
     */
    void resetPassword(AuthResetPasswordReqVO reqVO);

}
