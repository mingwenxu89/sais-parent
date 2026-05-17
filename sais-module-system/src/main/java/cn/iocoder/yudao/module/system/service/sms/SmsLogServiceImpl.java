package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.log.SmsLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.yudao.module.system.dal.mysql.sms.SmsLogMapper;
import cn.iocoder.yudao.module.system.enums.sms.SmsReceiveStatusEnum;
import cn.iocoder.yudao.module.system.enums.sms.SmsSendStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * SMS log Service implementation class
 *
 * @author zzf
 */
@Slf4j
@Service
public class SmsLogServiceImpl implements SmsLogService {

    @Resource
    private SmsLogMapper smsLogMapper;

    @Override
    public Long createSmsLog(String mobile, Long userId, Integer userType, Boolean isSend,
                             SmsTemplateDO template, String templateContent, Map<String, Object> templateParams) {
        SmsLogDO.SmsLogDOBuilder logBuilder = SmsLogDO.builder();
        // Set the status according to whether you want to send it
        logBuilder.sendStatus(Objects.equals(isSend, true) ? SmsSendStatusEnum.INIT.getStatus()
                : SmsSendStatusEnum.IGNORE.getStatus());
        // Set mobile phone related fields
        logBuilder.mobile(mobile).userId(userId).userType(userType);
        // Set template related fields
        logBuilder.templateId(template.getId()).templateCode(template.getCode()).templateType(template.getType());
        logBuilder.templateContent(templateContent).templateParams(templateParams)
                .apiTemplateId(template.getApiTemplateId());
        // Set channel related fields
        logBuilder.channelId(template.getChannelId()).channelCode(template.getChannelCode());
        // Set receive related fields
        logBuilder.receiveStatus(SmsReceiveStatusEnum.INIT.getStatus());

        // Insert into database
        SmsLogDO logDO = logBuilder.build();
        smsLogMapper.insert(logDO);
        return logDO.getId();
    }

    @Override
    public void updateSmsSendResult(Long id, Boolean success,
                                    String apiSendCode, String apiSendMsg,
                                    String apiRequestId, String apiSerialNo) {
        SmsSendStatusEnum sendStatus = success ? SmsSendStatusEnum.SUCCESS : SmsSendStatusEnum.FAILURE;
        smsLogMapper.updateById(SmsLogDO.builder().id(id)
                .sendStatus(sendStatus.getStatus()).sendTime(LocalDateTime.now())
                .apiSendCode(apiSendCode).apiSendMsg(apiSendMsg)
                .apiRequestId(apiRequestId).apiSerialNo(apiSerialNo).build());
    }

    @Override
    public void updateSmsReceiveResult(Long id, String apiSerialNo, Boolean success, LocalDateTime receiveTime,
                                       String apiReceiveCode, String apiReceiveMsg) {
        SmsReceiveStatusEnum receiveStatus = Objects.equals(success, true) ?
                SmsReceiveStatusEnum.SUCCESS : SmsReceiveStatusEnum.FAILURE;
        if (id == null || id == 0) {
            SmsLogDO log = smsLogMapper.selectByApiSerialNo(apiSerialNo);
            if (log == null) {
                return;
            }
            id = log.getId();
        }
        smsLogMapper.updateById(SmsLogDO.builder().id(id).receiveStatus(receiveStatus.getStatus())
                .receiveTime(receiveTime).apiReceiveCode(apiReceiveCode).apiReceiveMsg(apiReceiveMsg).build());
    }

    @Override
    public SmsLogDO getSmsLog(Long id) {
        return smsLogMapper.selectById(id);
    }

    @Override
    public PageResult<SmsLogDO> getSmsLogPage(SmsLogPageReqVO pageReqVO) {
        return smsLogMapper.selectPage(pageReqVO);
    }

}
