package cn.iocoder.yudao.module.system.service.social;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.user.SocialUserPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserBindDO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserBindMapper;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserMapper;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.SOCIAL_USER_NOT_FOUND;

/**
 * Social user Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class SocialUserServiceImpl implements SocialUserService {

    @Resource
    private SocialUserBindMapper socialUserBindMapper;
    @Resource
    private SocialUserMapper socialUserMapper;

    @Resource
    private SocialClientService socialClientService;

    @Override
    public List<SocialUserDO> getSocialUserList(Long userId, Integer userType) {
        // get binding
        List<SocialUserBindDO> socialUserBinds = socialUserBindMapper.selectListByUserIdAndUserType(userId, userType);
        if (CollUtil.isEmpty(socialUserBinds)) {
            return Collections.emptyList();
        }
        // Get social users
        return socialUserMapper.selectByIds(convertSet(socialUserBinds, SocialUserBindDO::getSocialUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        // Get social users
        SocialUserDO socialUser = authSocialUser(reqDTO.getSocialType(), reqDTO.getUserType(),
                reqDTO.getCode(), reqDTO.getState());
        Assert.notNull(socialUser, "Social user cannot be empty");

        // Social users may have been bound to other users before and need to unbind them.
        socialUserBindMapper.deleteByUserTypeAndSocialUserId(reqDTO.getUserType(), socialUser.getId());

        // The user may have bound this social type before and needs to unbind it.
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(reqDTO.getUserType(), reqDTO.getUserId(),
                socialUser.getType());

        // Bind the currently logged in social user
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(reqDTO.getUserId()).userType(reqDTO.getUserType())
                .socialUserId(socialUser.getId()).socialType(socialUser.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
        return socialUser.getOpenid();
    }

    @Override
    public void unbindSocialUser(Long userId, Integer userType, Integer socialType, String openid) {
        // Get the SocialUserDO social user corresponding to openid
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndOpenid(socialType, openid);
        if (socialUser == null) {
            throw exception(SOCIAL_USER_NOT_FOUND);
        }

        // Obtain the corresponding social binding relationship
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(userType, userId, socialUser.getType());
    }

    @Override
    public SocialUserRespDTO getSocialUserByUserId(Integer userType, Long userId, Integer socialType) {
        // Get bound user
        SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserIdAndUserTypeAndSocialType(userId, userType, socialType);
        if (socialUserBind == null) {
            return null;
        }
        // Get social users
        SocialUserDO socialUser = socialUserMapper.selectById(socialUserBind.getSocialUserId());
        Assert.notNull(socialUser, "Social user cannot be empty");
        return new SocialUserRespDTO(socialUser.getOpenid(), socialUser.getNickname(), socialUser.getAvatar(),
                socialUserBind.getUserId());
    }

    @Override
    public SocialUserRespDTO getSocialUserByCode(Integer userType, Integer socialType, String code, String state) {
        // Get social users
        SocialUserDO socialUser = authSocialUser(socialType, userType, code, state);
        Assert.notNull(socialUser, "Social user cannot be empty");

        // Get bound user
        SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserTypeAndSocialUserId(userType,
                socialUser.getId());
        return new SocialUserRespDTO(socialUser.getOpenid(), socialUser.getNickname(), socialUser.getAvatar(),
                socialUserBind != null ? socialUserBind.getUserId() : null);
    }

    /**
     * Authorize to obtain the corresponding social user
     * If authorization fails, a {@link ServiceException} exception will be thrown
     *
     * @param socialType Type of social platform {@link SocialTypeEnum}
     * @param userType User type
     * @param code     Authorization code
     * @param state    state
     * @return authorized user
     */
    @NotNull
    public SocialUserDO authSocialUser(Integer socialType, Integer userType, String code, String state) {
        // Get it from DB first because the code exists and can be used once.
        // During social login, when User is not bound, login needs to be bound. In this case, code needs to be used twice.
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAnState(socialType, code, state);
        if (socialUser != null) {
            return socialUser;
        }

        // Request to get
        AuthUser authUser = socialClientService.getAuthUser(socialType, userType, code, state);
        Assert.notNull(authUser, "Third-party users cannot be empty");

        // Save to DB
        socialUser = socialUserMapper.selectByTypeAndOpenid(socialType, authUser.getUuid());
        if (socialUser == null) {
            socialUser = new SocialUserDO();
        }
        socialUser.setType(socialType).setCode(code).setState(state) // Need to save code + state Field to ensure that it can be queried later
                .setOpenid(authUser.getUuid()).setToken(authUser.getToken().getAccessToken()).setRawTokenInfo((toJsonString(authUser.getToken())))
                .setNickname(authUser.getNickname()).setAvatar(authUser.getAvatar()).setRawUserInfo(toJsonString(authUser.getRawUserInfo()));
        if (socialUser.getId() == null) {
            socialUserMapper.insert(socialUser);
        } else {
            socialUser.clean(); // Avoid updateTime not updating: https://gitee.com/yudaocode/sar-boot-mini/issues/ID7FUL
            socialUserMapper.updateById(socialUser);
        }
        return socialUser;
    }

    // ==================== Social User CRUD ====================

    @Override
    public SocialUserDO getSocialUser(Long id) {
        return socialUserMapper.selectById(id);
    }

    @Override
    public PageResult<SocialUserDO> getSocialUserPage(SocialUserPageReqVO pageReqVO) {
        return socialUserMapper.selectPage(pageReqVO);
    }

}
