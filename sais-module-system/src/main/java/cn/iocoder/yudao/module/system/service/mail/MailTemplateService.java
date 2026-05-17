package cn.iocoder.yudao.module.system.service.mail;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.template.MailTemplatePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.template.MailTemplateSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Email template service API
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
public interface MailTemplateService {

    /**
     * Email template creation
     *
     * @param createReqVO Email message
     * @return ID
     */
    Long createMailTemplate(@Valid MailTemplateSaveReqVO createReqVO);

    /**
     * Email template modification
     *
     * @param updateReqVO Email message
     */
    void updateMailTemplate(@Valid MailTemplateSaveReqVO updateReqVO);

    /**
     * Email template deletion
     *
     * @param id ID
     */
    void deleteMailTemplate(Long id);

    /**
     * Batch delete email templates
     *
     * @param ids ID list
     */
    void deleteMailTemplateList(List<Long> ids);

    /**
     * Get email template
     *
     * @param id ID
     * @return Email template
     */
    MailTemplateDO getMailTemplate(Long id);

    /**
     * Get email template pagination
     *
     * @param pageReqVO Template information
     * @return Email template pagination information
     */
    PageResult<MailTemplateDO> getMailTemplatePage(MailTemplatePageReqVO pageReqVO);

    /**
     * Get email template array
     *
     * @return template array
     */
    List<MailTemplateDO> getMailTemplateList();

    /**
     * Get email template from cache
     *
     * @param code template encoding
     * @return Email template
     */
    MailTemplateDO getMailTemplateByCodeFromCache(String code);

    /**
     * Email template content synthesis
     *
     * @param content Email template
     * @param params Synthesis parameters
     * @return Formatted content
     */
    String formatMailTemplateContent(String content, Map<String, Object> params);

    /**
     * Get the ID of email templates under the specified email account
     *
     * @param accountId Account ID
     * @return Quantity
     */
    long getMailTemplateCountByAccountId(Long accountId);

}
