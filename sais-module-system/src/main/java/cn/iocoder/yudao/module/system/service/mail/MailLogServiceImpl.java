package cn.iocoder.yudao.module.system.service.mail;

import cn.hutool.core.collection.ListUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.log.MailLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailAccountDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.yudao.module.system.dal.mysql.mail.MailLogMapper;
import cn.iocoder.yudao.module.system.enums.mail.MailSendStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static cn.hutool.core.exceptions.ExceptionUtil.getRootCauseMessage;

/**
 * Mail log Service implementation class
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
@Service
@Validated
public class MailLogServiceImpl implements MailLogService {

    @Resource
    private MailLogMapper mailLogMapper;

    @Override
    public PageResult<MailLogDO> getMailLogPage(MailLogPageReqVO pageVO) {
        return mailLogMapper.selectPage(pageVO);
    }

    @Override
    public MailLogDO getMailLog(Long id) {
        return mailLogMapper.selectById(id);
    }

    @Override
    public Long createMailLog(Long userId, Integer userType,
                              Collection<String> toMails, Collection<String> ccMails, Collection<String> bccMails,
                              MailAccountDO account, MailTemplateDO template,
                              String templateContent, Map<String, Object> templateParams, Boolean isSend) {
        MailLogDO.MailLogDOBuilder logDOBuilder = MailLogDO.builder();
        // Set the status according to whether you want to send it
        logDOBuilder.sendStatus(Objects.equals(isSend, true) ? MailSendStatusEnum.INIT.getStatus()
                : MailSendStatusEnum.IGNORE.getStatus())
                // User information
                .userId(userId).userType(userType)
                .toMails(ListUtil.toList(toMails)).ccMails(ListUtil.toList(ccMails)).bccMails(ListUtil.toList(bccMails))
                .accountId(account.getId()).fromMail(account.getMail())
                // Template related fields
                .templateId(template.getId()).templateCode(template.getCode()).templateNickname(template.getNickname())
                .templateTitle(template.getTitle()).templateContent(templateContent).templateParams(templateParams);

        // Insert into database
        MailLogDO logDO = logDOBuilder.build();
        mailLogMapper.insert(logDO);
        return logDO.getId();
    }

    @Override
    public void updateMailSendResult(Long logId, String messageId, Exception exception) {
        // 1. success
        if (exception == null) {
            mailLogMapper.updateById(new MailLogDO().setId(logId).setSendTime(LocalDateTime.now())
                    .setSendStatus(MailSendStatusEnum.SUCCESS.getStatus()).setSendMessageId(messageId));
            return;
        }
        // 2. Failure
        mailLogMapper.updateById(new MailLogDO().setId(logId).setSendTime(LocalDateTime.now())
                .setSendStatus(MailSendStatusEnum.FAILURE.getStatus()).setSendException(getRootCauseMessage(exception)));

    }

}
