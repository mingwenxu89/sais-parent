package cn.iocoder.yudao.module.system.convert.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthPermissionInfoRespVO;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthSmsLoginReqVO;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;
import cn.iocoder.yudao.module.system.controller.admin.auth.vo.AuthSocialLoginReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.enums.permission.MenuTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.filterList;
import static cn.iocoder.yudao.module.system.dal.dataobject.permission.MenuDO.ID_ROOT;

@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    default AuthPermissionInfoRespVO convert(AdminUserDO user, List<RoleDO> roleList, List<MenuDO> menuList) {
        return AuthPermissionInfoRespVO.builder()
                .user(BeanUtils.toBean(user, AuthPermissionInfoRespVO.UserVO.class))
                .roles(convertSet(roleList, RoleDO::getCode))
                // Permission identification information
                .permissions(convertSet(menuList, MenuDO::getPermission))
                // menu tree
                .menus(buildMenuTree(menuList))
                .build();
    }

    /**
     * Construct menu list into menu tree
     *
     * @param menuList Menu list
     * @return menu tree
     */
    default List<AuthPermissionInfoRespVO.MenuVO> buildMenuTree(List<MenuDO> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return Collections.emptyList();
        }
        // remove button
        menuList.removeIf(menu -> menu.getType().equals(MenuTypeEnum.BUTTON.getType()));
        // Sorting to ensure the orderliness of the menu
        menuList.sort(Comparator.comparing(MenuDO::getSort));

        // Build menu tree
        // The reason for using LinkedHashMap is for sorting. In fact, you can also use the Stream API, but it is too ugly.
        Map<Long, AuthPermissionInfoRespVO.MenuVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(menu.getId(),
                BeanUtils.toBean(menu, AuthPermissionInfoRespVO.MenuVO.class)));
        // Dealing with father-son relationship
        treeNodeMap.values().stream().filter(node -> ObjUtil.notEqual(node.getParentId(), ID_ROOT)).forEach(childNode -> {
            // Get parent node
            AuthPermissionInfoRespVO.MenuVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) cannot find parent resource({})]",
                        childNode.getId(), childNode.getParentId());
                return;
            }
            // Add yourself to the parent node
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // Get all root nodes
        return filterList(treeNodeMap.values(), node -> ID_ROOT.equals(node.getParentId()));
    }

    SocialUserBindReqDTO convert(Long userId, Integer userType, AuthSocialLoginReqVO reqVO);

    SmsCodeSendReqDTO convert(AuthSmsSendReqVO reqVO);

    SmsCodeUseReqDTO convert(AuthSmsLoginReqVO reqVO, Integer scene, String usedIp);

}
