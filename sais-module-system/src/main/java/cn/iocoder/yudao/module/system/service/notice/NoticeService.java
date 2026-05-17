package cn.iocoder.yudao.module.system.service.notice;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.notice.vo.NoticePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.notice.vo.NoticeSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.notice.NoticeDO;

import java.util.List;

/**
 * Notification Announcement Service API
 */
public interface NoticeService {

    /**
     * Create notification announcement
     *
     * @param createReqVO Notices and Announcements
     * @return ID
     */
    Long createNotice(NoticeSaveReqVO createReqVO);

    /**
     * Update notification announcement
     *
     * @param reqVO Notices and Announcements
     */
    void updateNotice(NoticeSaveReqVO reqVO);

    /**
     * Delete notification announcement
     *
     * @param id ID
     */
    void deleteNotice(Long id);

    /**
     * Bulk deletion notification announcement
     *
     * @param ids ID list
     */
    void deleteNoticeList(List<Long> ids);

    /**
     * Get a paginated list of notifications and announcements
     *
     * @param reqVO Paging conditions
     * @return Department paging list
     */
    PageResult<NoticeDO> getNoticePage(NoticePageReqVO reqVO);

    /**
     * Get notification announcements
     *
     * @param id ID
     * @return Notices and Announcements
     */
    NoticeDO getNotice(Long id);

}
