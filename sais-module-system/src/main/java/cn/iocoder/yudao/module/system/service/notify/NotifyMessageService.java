package cn.iocoder.yudao.module.system.service.notify;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.notify.vo.message.NotifyMessageMyPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyTemplateDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Site message Service API
 *
 * @author xrcoder
 */
public interface NotifyMessageService {

    /**
     * Create site message
     *
     * @param userId User ID
     * @param userType User type
     * @param template Template information
     * @param templateContent Template content
     * @param templateParams Template parameters
     * @return Site message ID
     */
    Long createNotifyMessage(Long userId, Integer userType,
                             NotifyTemplateDO template, String templateContent, Map<String, Object> templateParams);

    /**
     * Get site message pagination
     *
     * @param pageReqVO Page query
     * @return In-site message paging
     */
    PageResult<NotifyMessageDO> getNotifyMessagePage(NotifyMessagePageReqVO pageReqVO);

    /**
     * Get [my] site message page
     *
     * @param pageReqVO Page query
     * @param userId User ID
     * @param userType User type
     * @return In-site message paging
     */
    PageResult<NotifyMessageDO> getMyMyNotifyMessagePage(NotifyMessageMyPageReqVO pageReqVO, Long userId, Integer userType);

    /**
     * Get site message
     *
     * @param id ID
     * @return Internal Message
     */
    NotifyMessageDO getNotifyMessage(Long id);

    /**
     * Get [my] unread site message list
     *
     * @param userId   User ID
     * @param userType User type
     * @param size     Quantity
     * @return Site message list
     */
    List<NotifyMessageDO> getUnreadNotifyMessageList(Long userId, Integer userType, Integer size);

    /**
     * Count the ID of unread posts on the site by users
     *
     * @param userId   User ID
     * @param userType User type
     * @return Returns the ID of unread messages on the site
     */
    Long getUnreadNotifyMessageCount(Long userId, Integer userType);

    /**
     * Mark site messages as read
     *
     * @param ids    Site message ID collection
     * @param userId User ID
     * @param userType User type
     * @return ID of items updated
     */
    int updateNotifyMessageRead(Collection<Long> ids, Long userId, Integer userType);

    /**
     * Mark all site messages as read
     *
     * @param userId   User ID
     * @param userType User type
     * @return ID of items updated
     */
    int updateAllNotifyMessageRead(Long userId, Integer userType);

}
