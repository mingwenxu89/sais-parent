package cn.iocoder.yudao.module.system.api.social;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserUnbindReqDTO;
import jakarta.validation.Valid;

/**
 * Social user API API
 *
 * @author Yudao Source Code
 */
public interface SocialUserApi {

    /**
     * Bind social users
     *
     * @param reqDTO binding information
     * @return social user openid
     */
    String bindSocialUser(@Valid SocialUserBindReqDTO reqDTO);

    /**
     * Unbind social users
     *
     * @param reqDTO unbundle
     */
    void unbindSocialUser(@Valid SocialUserUnbindReqDTO reqDTO);

    /**
     * Get social users, based on userId
     *
     * @param userType   User type
     * @param userId     User ID
     * @param socialType Types of social platforms
     * @return social user
     */
    SocialUserRespDTO getSocialUserByUserId(Integer userType, Long userId, Integer socialType);

    /**
     * Get social users
     *
     * When the authentication information is incorrect, {@link ServiceException} business exception will also be thrown.
     *
     * @param userType   User type
     * @param socialType Types of social platforms
     * @param code       Authorization code
     * @param state      state
     * @return social user
     */
    SocialUserRespDTO getSocialUserByCode(Integer userType, Integer socialType, String code, String state);

}
