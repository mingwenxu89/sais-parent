package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.template.SmsTemplatePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.template.SmsTemplateSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsTemplateDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * SMS template Service API
 *
 * @author zzf
 * @since 2021/1/25 9:24
 */
public interface SmsTemplateService {

    /**
     * Create SMS template
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createSmsTemplate(@Valid SmsTemplateSaveReqVO createReqVO);

    /**
     * Update SMS template
     *
     * @param updateReqVO Update information
     */
    void updateSmsTemplate(@Valid SmsTemplateSaveReqVO updateReqVO);

    /**
     * Delete SMS template
     *
     * @param id ID
     */
    void deleteSmsTemplate(Long id);

    /**
     * Delete SMS templates in batches
     *
     * @param ids IDed array
     */
    void deleteSmsTemplateList(List<Long> ids);

    /**
     * Get SMS template
     *
     * @param id ID
     * @return SMS template
     */
    SmsTemplateDO getSmsTemplate(Long id);

    /**
     * Get SMS template from cache
     *
     * @param code template encoding
     * @return SMS template
     */
    SmsTemplateDO getSmsTemplateByCodeFromCache(String code);

    /**
     * Get SMS template pagination
     *
     * @param pageReqVO Page query
     * @return SMS template pagination
     */
    PageResult<SmsTemplateDO> getSmsTemplatePage(SmsTemplatePageReqVO pageReqVO);

    /**
     * Get the ID of SMS templates under the specified SMS channel
     *
     * @param channelId SMS channel ID
     * @return Quantity
     */
    Long getSmsTemplateCountByChannelId(Long channelId);

    /**
     * Format text message content
     *
     * @param content Contents of SMS template
     * @param params content parameters
     * @return Formatted content
     */
    String formatSmsTemplateContent(String content, Map<String, Object> params);

}
