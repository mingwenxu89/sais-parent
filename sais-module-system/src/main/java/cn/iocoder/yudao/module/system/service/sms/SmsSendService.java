package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.module.system.mq.message.sms.SmsSendMessage;

import java.util.List;
import java.util.Map;

/**
 * SMS sending Service API
 *
 * @author Yudao Source Code
 */
public interface SmsSendService {

    /**
     * Send a single text message to the user in the management background
     *
     * When mobile is empty, use userId to load the mobile phone ID of the corresponding administrator.
     *
     * @param mobile Mobile phone ID
     * @param userId User ID
     * @param templateCode SMS template ID
     * @param templateParams SMS template parameters
     * @return Send log ID
     */
    Long sendSingleSmsToAdmin(String mobile, Long userId,
                              String templateCode, Map<String, Object> templateParams);

    /**
     * Send a single text message to the user of the user APP
     *
     * When mobile is empty, use userId to load the mobile phone ID of the corresponding member.
     *
     * @param mobile Mobile phone ID
     * @param userId User ID
     * @param templateCode SMS template ID
     * @param templateParams SMS template parameters
     * @return Send log ID
     */
    Long sendSingleSmsToMember(String mobile, Long userId,
                               String templateCode, Map<String, Object> templateParams);

    /**
     * Send a single text message to the user
     *
     * @param mobile Mobile phone ID
     * @param userId User ID
     * @param userType User type
     * @param templateCode SMS template ID
     * @param templateParams SMS template parameters
     * @return Send log ID
     */
    Long sendSingleSms(String mobile, Long userId, Integer userType,
                       String templateCode, Map<String, Object> templateParams);

    default void sendBatchSms(List<String> mobiles, List<Long> userIds, Integer userType,
                              String templateCode, Map<String, Object> templateParams) {
        throw new UnsupportedOperationException("This operation is not supported for the time being. If you are interested, you can implement this function!");
    }

    /**
     * Perform real SMS sending
     * Note that this method is only available to MQ Consumer
     *
     * @param message SMS
     */
    void doSendSms(SmsSendMessage message);

    /**
     * Receive SMS reception result
     *
     * @param channelCode Channel code
     * @param text Result content
     * @throws Throwable throws an exception when processing fails
     */
    void receiveSmsStatus(String channelCode, String text) throws Throwable;

}
