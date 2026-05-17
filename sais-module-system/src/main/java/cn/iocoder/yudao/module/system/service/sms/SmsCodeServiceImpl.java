package cn.iocoder.yudao.module.system.service.sms;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeValidateReqDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsCodeDO;
import cn.iocoder.yudao.module.system.dal.mysql.sms.SmsCodeMapper;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import cn.iocoder.yudao.module.system.framework.sms.config.SmsCodeProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.isToday;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * SMS captcha Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
public class SmsCodeServiceImpl implements SmsCodeService {

    @Resource
    private SmsCodeProperties smsCodeProperties;

    @Resource
    private SmsCodeMapper smsCodeMapper;

    @Resource
    private SmsSendService smsSendService;

    @Override
    public void sendSmsCode(SmsCodeSendReqDTO reqDTO) {
        SmsSceneEnum sceneEnum = SmsSceneEnum.getCodeByScene(reqDTO.getScene());
        Assert.notNull(sceneEnum, "Captcha scenario ({}) configuration cannot be found", reqDTO.getScene());
        // Create captcha
        String code = createSmsCode(reqDTO.getMobile(), reqDTO.getScene(), reqDTO.getCreateIp());
        // Send captcha
        smsSendService.sendSingleSms(reqDTO.getMobile(), null, null,
                sceneEnum.getTemplateCode(), MapUtil.of("code", code));
    }

    private String createSmsCode(String mobile, Integer scene, String ip) {
        // Verify whether the captcha can be sent without filtering the scenario
        SmsCodeDO lastSmsCode = smsCodeMapper.selectLastByMobile(mobile, null, null);
        if (lastSmsCode != null) {
            if (LocalDateTimeUtil.between(lastSmsCode.getCreateTime(), LocalDateTime.now()).toMillis()
                    < smsCodeProperties.getSendFrequency().toMillis()) { // Sent too frequently
                throw exception(SMS_CODE_SEND_TOO_FAST);
            }
            if (isToday(lastSmsCode.getCreateTime()) && // It must be today to calculate the limit exceeding the day's limit
                    lastSmsCode.getTodayIndex() >= smsCodeProperties.getSendMaximumQuantityPerDay()) { // Exceeded the sending limit for the day.
                throw exception(SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY);
            }
            // TODO Taro: Improved, the ID of messages each IP can send per day
            // TODO Taro: Improve the ID of times each IP can send per hour
        }

        // Create captcha record
        String code = String.format("%0" + smsCodeProperties.getEndCode().toString().length() + "d",
                randomInt(smsCodeProperties.getBeginCode(), smsCodeProperties.getEndCode() + 1));
        SmsCodeDO newSmsCode = SmsCodeDO.builder().mobile(mobile).code(code).scene(scene)
                .todayIndex(lastSmsCode != null && isToday(lastSmsCode.getCreateTime()) ? lastSmsCode.getTodayIndex() + 1 : 1)
                .createIp(ip).used(false).build();
        smsCodeMapper.insert(newSmsCode);
        return code;
    }

    @Override
    public void useSmsCode(SmsCodeUseReqDTO reqDTO) {
        // Check whether the captcha is valid
        SmsCodeDO lastSmsCode = validateSmsCode0(reqDTO.getMobile(), reqDTO.getCode(), reqDTO.getScene());
        // Use captcha
        smsCodeMapper.updateById(SmsCodeDO.builder().id(lastSmsCode.getId())
                .used(true).usedTime(LocalDateTime.now()).usedIp(reqDTO.getUsedIp()).build());
    }

    @Override
    public void validateSmsCode(SmsCodeValidateReqDTO reqDTO) {
        validateSmsCode0(reqDTO.getMobile(), reqDTO.getCode(), reqDTO.getScene());
    }

    private SmsCodeDO validateSmsCode0(String mobile, String code, Integer scene) {
        // Verify captcha
        SmsCodeDO lastSmsCode = smsCodeMapper.selectLastByMobile(mobile, code, scene);
        // If the captcha does not exist, an exception is thrown
        if (lastSmsCode == null) {
            throw exception(SMS_CODE_NOT_FOUND);
        }
        // time exceeded
        if (LocalDateTimeUtil.between(lastSmsCode.getCreateTime(), LocalDateTime.now()).toMillis()
                >= smsCodeProperties.getExpireTimes().toMillis()) { // Captcha has expired
            throw exception(SMS_CODE_EXPIRED);
        }
        // Determine whether the captcha has been used
        if (Boolean.TRUE.equals(lastSmsCode.getUsed())) {
            throw exception(SMS_CODE_USED);
        }
        return lastSmsCode;
    }

}
