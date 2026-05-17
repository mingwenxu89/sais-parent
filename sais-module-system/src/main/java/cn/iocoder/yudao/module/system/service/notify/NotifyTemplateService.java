package cn.iocoder.yudao.module.system.service.notify;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.notify.vo.template.NotifyTemplatePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.notify.vo.template.NotifyTemplateSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyTemplateDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Site message template Service API
 *
 * @author xrcoder
 */
public interface NotifyTemplateService {

    /**
     * Create an on-site letter template
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createNotifyTemplate(@Valid NotifyTemplateSaveReqVO createReqVO);

    /**
     * Update site letter template
     *
     * @param updateReqVO Update information
     */
    void updateNotifyTemplate(@Valid NotifyTemplateSaveReqVO updateReqVO);

    /**
     * Delete site letter template
     *
     * @param id ID
     */
    void deleteNotifyTemplate(Long id);

    /**
     * Delete site letter templates in batches
     *
     * @param ids ID list
     */
    void deleteNotifyTemplateList(List<Long> ids);

    /**
     * Get the on-site letter template
     *
     * @param id ID
     * @return Site letter template
     */
    NotifyTemplateDO getNotifyTemplate(Long id);

    /**
     * Get the site letter template from the cache
     *
     * @param code template encoding
     * @return Site letter template
     */
    NotifyTemplateDO getNotifyTemplateByCodeFromCache(String code);

    /**
     * Get site letter template pagination
     *
     * @param pageReqVO Page query
     * @return Site letter template pagination
     */
    PageResult<NotifyTemplateDO> getNotifyTemplatePage(NotifyTemplatePageReqVO pageReqVO);

    /**
     * Format content of in-site messages
     *
     * @param content Contents of site letter template
     * @param params Parameters of site message content
     * @return Formatted content
     */
    String formatNotifyTemplateContent(String content, Map<String, Object> params);

}
