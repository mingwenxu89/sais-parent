package cn.iocoder.yudao.module.system.api.mail;

import cn.iocoder.yudao.module.system.api.mail.dto.MailSendSingleToUserReqDTO;

import jakarta.validation.Valid;

/**
 * Email sending API API
 *
 * @author Yudao Source Code
 */
public interface MailSendApi {

    /**
     * Send a single email to the Admin user
     *
     * When mail is empty, use userId to load the mailbox corresponding to Admin.
     *
     * @param reqDTO Send request
     * @return Send log ID
     */
    Long sendSingleMailToAdmin(@Valid MailSendSingleToUserReqDTO reqDTO);

    /**
     * Send a single email to Member users
     *
     * When mail is empty, use userId to load the corresponding Member's mailbox.
     *
     * @param reqDTO Send request
     * @return Send log ID
     */
    Long sendSingleMailToMember(@Valid MailSendSingleToUserReqDTO reqDTO);

}
