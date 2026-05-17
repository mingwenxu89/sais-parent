package cn.iocoder.yudao.module.system.service.mail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.mq.message.mail.MailSendMessage;
import cn.iocoder.yudao.module.system.mq.producer.mail.MailProducer;
import cn.iocoder.yudao.module.system.service.member.MemberService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * Email sending Service implementation class
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
@Service
@Validated
@Slf4j
public class MailSendServiceImpl implements MailSendService {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private MemberService memberService;

    @Resource
    private MailAccountService mailAccountService;
    @Resource
    private MailTemplateService mailTemplateService;

    @Resource
    private MailLogService mailLogService;
    @Resource
    private MailProducer mailProducer;

    @Override
    public Long sendSingleMail(Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                               Long userId, Integer userType,
                               String templateCode, Map<String, Object> templateParams,
                               File... attachments) {
        // 1.1 Verify whether the email template is legal
        MailTemplateDO template = validateMailTemplate(templateCode);
        // 1.2 Verify whether the email account is legal
        MailAccountDO account = validateMailAccount(template.getAccountId());
        // 1.3 Verify whether email parameters are missing
        validateTemplateParams(template, templateParams);

        // 2. Assemble the mailbox
        String userMail = getUserMail(userId, userType);
        Collection<String> toMailSet = new LinkedHashSet<>();
        Collection<String> ccMailSet = new LinkedHashSet<>();
        Collection<String> bccMailSet = new LinkedHashSet<>();
        if (Validator.isEmail(userMail)) {
            toMailSet.add(userMail);
        }
        if (CollUtil.isNotEmpty(toMails)) {
            toMails.stream().filter(Validator::isEmail).forEach(toMailSet::add);
        }
        if (CollUtil.isNotEmpty(ccMails)) {
            ccMails.stream().filter(Validator::isEmail).forEach(ccMailSet::add);
        }
        if (CollUtil.isNotEmpty(bccMails)) {
            bccMails.stream().filter(Validator::isEmail).forEach(bccMailSet::add);
        }
        if (CollUtil.isEmpty(toMailSet)) {
            throw exception(MAIL_SEND_MAIL_NOT_EXISTS);
        }

        // Create a delivery log. If the template is disabled, no SMS will be sent, only logs will be recorded
        Boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(template.getStatus());
        String title = mailTemplateService.formatMailTemplateContent(template.getTitle(), templateParams);
        String content = mailTemplateService.formatMailTemplateContent(template.getContent(), templateParams);
        Long sendLogId = mailLogService.createMailLog(userId, userType, toMailSet, ccMailSet, bccMailSet,
                account, template, content, templateParams, isSend);
        // Send MQ messages and send SMS messages asynchronously
        if (isSend) {
            mailProducer.sendMailSendMessage(sendLogId, toMailSet, ccMailSet, bccMailSet,
                    account.getId(), template.getNickname(), title, content, attachments);
        }
        return sendLogId;
    }

    private String getUserMail(Long userId, Integer userType) {
        if (userId == null || userType == null) {
            return null;
        }
        if (UserTypeEnum.ADMIN.getValue().equals(userType)) {
            AdminUserDO user = adminUserService.getUser(userId);
            if (user != null) {
                return user.getEmail();
            }
        }
        if (UserTypeEnum.MEMBER.getValue().equals(userType)) {
            return memberService.getMemberUserEmail(userId);
        }
        return null;
    }

    @Override
    public void doSendMail(MailSendMessage message) {
        // 1. Create a sending account
        MailAccountDO account = validateMailAccount(message.getAccountId());
        MailAccount mailAccount  = buildMailAccount(account, message.getNickname());
        // 2. Send email
        try {
            String messageId = MailUtil.send(mailAccount, message.getToMails(), message.getCcMails(), message.getBccMails(),
                    message.getTitle(), message.getContent(), true, message.getAttachments());
            // 3. Update result (success)
            mailLogService.updateMailSendResult(message.getLogId(), messageId, null);
        } catch (Exception e) {
            // 3. Update results (exception)
            mailLogService.updateMailSendResult(message.getLogId(), null, e);
        }
    }

    private MailAccount buildMailAccount(MailAccountDO account, String nickname) {
        String from = StrUtil.isNotEmpty(nickname) ? nickname + " <" + account.getMail() + ">" : account.getMail();
        return new MailAccount().setFrom(from).setAuth(true)
                .setUser(account.getUsername()).setPass(account.getPassword())
                .setHost(account.getHost()).setPort(account.getPort())
                .setSslEnable(account.getSslEnable()).setStarttlsEnable(account.getStarttlsEnable());
    }

    @VisibleForTesting
    MailTemplateDO validateMailTemplate(String templateCode) {
        // Get email templates. Taking into account efficiency, obtain from cache
        MailTemplateDO template = mailTemplateService.getMailTemplateByCodeFromCache(templateCode);
        // Email template does not exist
        if (template == null) {
            throw exception(MAIL_TEMPLATE_NOT_EXISTS);
        }
        return template;
    }

    @VisibleForTesting
    MailAccountDO validateMailAccount(Long accountId) {
        // Get an email account. Taking into account efficiency, obtain from cache
        MailAccountDO account = mailAccountService.getMailAccountFromCache(accountId);
        // Email account does not exist
        if (account == null) {
            throw exception(MAIL_ACCOUNT_NOT_EXISTS);
        }
        return account;
    }

    /**
     * Verify whether email parameters are missing
     *
     * @param template Email template
     * @param templateParams Parameter list
     */
    @VisibleForTesting
    void validateTemplateParams(MailTemplateDO template, Map<String, Object> templateParams) {
        template.getParams().forEach(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw exception(MAIL_SEND_TEMPLATE_PARAM_MISS, key);
            }
        });
    }

}
