package cn.iocoder.yudao.module.system.service.social;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.user.SocialUserPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Social user service API, such as authorized login of social platforms
 *
 * @author Yudao Source Code
 */
public interface SocialUserService {

    /**
     * Get the social user list of the specified user
     *
     * @param userId   User ID
     * @param userType User type
     * @return Social user list
     */
    List<SocialUserDO> getSocialUserList(Long userId, Integer userType);

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
     * @param userId User ID
     * @param userType Global user type
     * @param socialType Type of social platform {@link SocialTypeEnum}
     * @param openid openid of social platform
     */
    void unbindSocialUser(Long userId, Integer userType, Integer socialType, String openid);

    /**
     * Get social users, based on userId
     *
     * @param userType User type
     * @param userId User ID
     * @param socialType Types of social platforms
     * @return social user
     */
    SocialUserRespDTO getSocialUserByUserId(Integer userType, Long userId, Integer socialType);

    /**
     * Get social users
     *
     * When the authentication information is incorrect, {@link ServiceException} business exception will also be thrown.
     *
     * @param userType User type
     * @param socialType Types of social platforms
     * @param code Authorization code
     * @param state state
     * @return social user
     */
    SocialUserRespDTO getSocialUserByCode(Integer userType, Integer socialType, String code, String state);

    // ==================== Social User CRUD ====================

    /**
     * Get social users
     *
     * @param id ID
     * @return social user
     */
    SocialUserDO getSocialUser(Long id);

    /**
     * Get social user pagination
     *
     * @param pageReqVO Page query
     * @return Social user pagination
     */
    PageResult<SocialUserDO> getSocialUserPage(SocialUserPageReqVO pageReqVO);

}
