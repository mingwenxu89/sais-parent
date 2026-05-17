package cn.iocoder.yudao.module.system.service.mail;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.log.MailLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Mail Log Service API
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
public interface MailLogService {

    /**
     * Email log paging
     *
     * @param pageVO Paging parameters
     * @return Paginated results
     */
    PageResult<MailLogDO> getMailLogPage(MailLogPageReqVO pageVO);

    /**
     * Get the email log with the specified ID
     *
     * @param id Log ID
     * @return Mail log
     */
    MailLogDO getMailLog(Long id);

    /**
     * Create mail log
     *
     * @param userId          user code
     * @param userType        User type
     * @param toMails         Recipient email
     * @param ccMails         Recipient email
     * @param bccMails        Recipient email
     * @param account         Email account information
     * @param template        Template information
     * @param templateContent Template content
     * @param templateParams  Template parameters
     * @param isSend          Whether sent successfully
     * @return Log ID
     */
    Long createMailLog(Long userId, Integer userType,
                       Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                       MailAccountDO account, MailTemplateDO template,
                       String templateContent, Map<String, Object> templateParams, Boolean isSend);

    /**
     * Update email sending results
     *
     * @param logId  Log ID
     * @param messageId Message ID after sending
     * @param exception Send exception
     */
    void updateMailSendResult(Long logId, String messageId, Exception exception);

}
