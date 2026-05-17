package cn.iocoder.yudao.module.system.api.sms;

import cn.iocoder.yudao.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;

import jakarta.validation.Valid;

/**
 * SMS sending API API
 *
 * @author Yudao Source Code
 */
public interface SmsSendApi {

    /**
     * Send a single text message to Admin user
     *
     * When mobile is empty, use userId to load the mobile phone ID corresponding to Admin.
     *
     * @param reqDTO Send request
     * @return Send log ID
     */
    Long sendSingleSmsToAdmin(@Valid SmsSendSingleToUserReqDTO reqDTO);

    /**
     * Send a single text message to Member users
     *
     * When mobile is empty, use userId to load the mobile phone ID corresponding to the Member.
     *
     * @param reqDTO Send request
     * @return Send log ID
     */
    Long sendSingleSmsToMember(@Valid SmsSendSingleToUserReqDTO reqDTO);

}
