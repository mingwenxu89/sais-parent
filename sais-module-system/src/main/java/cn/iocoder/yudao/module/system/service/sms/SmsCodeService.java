package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeValidateReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeUseReqDTO;

import jakarta.validation.Valid;

/**
 * SMS captcha Service API
 *
 * @author Yudao Source Code
 */
public interface SmsCodeService {

    /**
     * Create an SMS captcha and send it
     *
     * @param reqDTO Send request
     */
    void sendSmsCode(@Valid SmsCodeSendReqDTO reqDTO);

    /**
     * Verify the SMS captcha and use it
     * If correct, mark the captcha as used
     * If error occurs, throw {@link ServiceException} exception
     *
     * @param reqDTO Use request
     */
    void useSmsCode(@Valid SmsCodeUseReqDTO reqDTO);

    /**
     * Check if the captcha is valid
     *
     * @param reqDTO Verification request
     */
    void validateSmsCode(@Valid SmsCodeValidateReqDTO reqDTO);

}
