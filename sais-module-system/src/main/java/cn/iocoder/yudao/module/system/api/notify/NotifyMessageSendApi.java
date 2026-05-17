package cn.iocoder.yudao.module.system.api.notify;

import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;

import jakarta.validation.Valid;

/**
 * Site message sending API API
 *
 * @author xrcoder
 */
public interface NotifyMessageSendApi {

    /**
     * Send a single site message to Admin users
     *
     * @param reqDTO Send request
     * @return Send message ID
     */
    Long sendSingleMessageToAdmin(@Valid NotifySendSingleToUserReqDTO reqDTO);

    /**
     * Send a single site message to Member users
     *
     * @param reqDTO Send request
     * @return Send message ID
     */
    Long sendSingleMessageToMember(@Valid NotifySendSingleToUserReqDTO reqDTO);

}
