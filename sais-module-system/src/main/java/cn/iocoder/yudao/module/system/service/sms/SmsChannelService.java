package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClient;

import jakarta.validation.Valid;
import java.util.List;

/**
 * SMS channel Service API
 *
 * @author zzf
 * @since 2021/1/25 9:24
 */
public interface SmsChannelService {

    /**
     * Create SMS channel
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createSmsChannel(@Valid SmsChannelSaveReqVO createReqVO);

    /**
     * Update SMS channel
     *
     * @param updateReqVO Update information
     */
    void updateSmsChannel(@Valid SmsChannelSaveReqVO updateReqVO);

    /**
     * Delete SMS channel
     *
     * @param id ID
     */
    void deleteSmsChannel(Long id);

    /**
     * Delete SMS channels in batches
     *
     * @param ids IDed array
     */
    void deleteSmsChannelList(List<Long> ids);

    /**
     * Get SMS channel
     *
     * @param id ID
     * @return SMS channel
     */
    SmsChannelDO getSmsChannel(Long id);

    /**
     * Get a list of all SMS channels
     *
     * @return SMS channel list
     */
    List<SmsChannelDO> getSmsChannelList();

    /**
     * Get SMS channel pagination
     *
     * @param pageReqVO Page query
     * @return SMS channel paging
     */
    PageResult<SmsChannelDO> getSmsChannelPage(SmsChannelPageReqVO pageReqVO);

    /**
     * Get SMS client
     *
     * @param id ID
     * @return SMS client
     */
    SmsClient getSmsClient(Long id);

    /**
     * Get SMS client
     *
     * @param code encoding
     * @return SMS client
     */
    SmsClient getSmsClient(String code);

}
