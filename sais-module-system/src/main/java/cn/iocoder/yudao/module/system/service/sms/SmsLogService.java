package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.log.SmsLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsLogDO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsTemplateDO;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * SMS Log Service API
 *
 * @author zzf
 * @since 13:48 2021/3/2
 */
public interface SmsLogService {

    /**
     * Create SMS log
     *
     * @param mobile Mobile phone ID
     * @param userId User ID
     * @param userType User type
     * @param isSend Whether to send
     * @param template SMS template
     * @param templateContent SMS content
     * @param templateParams SMS parameters
     * @return Send log ID
     */
    Long createSmsLog(String mobile, Long userId, Integer userType, Boolean isSend,
                      SmsTemplateDO template, String templateContent, Map<String, Object> templateParams);

    /**
     * Update log sending results
     *
     * @param id Log ID
     * @param success Is the sending successful?
     * @param apiSendCode Encoding of results sent by SMS API
     * @param apiSendMsg SMS API sending failure prompt
     * @param apiRequestId Unique request ID returned by SMS API send
     * @param apiSerialNo Serial ID returned by SMS API sending
     */
    void updateSmsSendResult(Long id, Boolean success,
                             String apiSendCode, String apiSendMsg,
                             String apiRequestId, String apiSerialNo);

    /**
     * Update log reception results
     *
     * @param id Log ID
     * @param apiSerialNo Send ID
     * @param success Whether the reception is successful
     * @param receiveTime User reception time
     * @param apiReceiveCode Encoding of API received results
     * @param apiReceiveMsg Description of API receiving results
     */
    void updateSmsReceiveResult(Long id, String apiSerialNo, Boolean success,
                                LocalDateTime receiveTime, String apiReceiveCode, String apiReceiveMsg);

    /**
     * Get SMS log
     *
     * @param id Log ID
     * @return SMS log
     */
    SmsLogDO getSmsLog(Long id);

    /**
     * Get SMS log pagination
     *
     * @param pageReqVO Page query
     * @return SMS log paging
     */
    PageResult<SmsLogDO> getSmsLogPage(SmsLogPageReqVO pageReqVO);

}
