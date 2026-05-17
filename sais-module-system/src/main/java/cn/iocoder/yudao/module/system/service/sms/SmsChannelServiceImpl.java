package cn.iocoder.yudao.module.system.service.sms;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.yudao.module.system.dal.mysql.sms.SmsChannelMapper;
import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClient;
import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClientFactory;
import cn.iocoder.yudao.module.system.framework.sms.core.property.SmsChannelProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.SMS_CHANNEL_HAS_CHILDREN;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.SMS_CHANNEL_NOT_EXISTS;

/**
 * SMS channel Service implementation class
 *
 * @author zzf
 */
@Service
@Slf4j
public class SmsChannelServiceImpl implements SmsChannelService {

    @Resource
    private SmsClientFactory smsClientFactory;

    @Resource
    private SmsChannelMapper smsChannelMapper;

    @Resource
    private SmsTemplateService smsTemplateService;

    @Override
    public Long createSmsChannel(SmsChannelSaveReqVO createReqVO) {
        SmsChannelDO channel = BeanUtils.toBean(createReqVO, SmsChannelDO.class);
        smsChannelMapper.insert(channel);
        return channel.getId();
    }

    @Override
    public void updateSmsChannel(SmsChannelSaveReqVO updateReqVO) {
        // Check existence
        validateSmsChannelExists(updateReqVO.getId());
        // Update
        SmsChannelDO updateObj = BeanUtils.toBean(updateReqVO, SmsChannelDO.class);
        smsChannelMapper.updateById(updateObj);
    }

    @Override
    public void deleteSmsChannel(Long id) {
        // Check existence
        validateSmsChannelExists(id);
        // Verify whether there is a template using this account
        if (smsTemplateService.getSmsTemplateCountByChannelId(id) > 0) {
            throw exception(SMS_CHANNEL_HAS_CHILDREN);
        }
        // Delete
        smsChannelMapper.deleteById(id);
    }

    @Override
    public void deleteSmsChannelList(List<Long> ids) {
        // 1. Verify whether there is a template using the account
        ids.forEach(id -> {
            if (smsTemplateService.getSmsTemplateCountByChannelId(id) > 0) {
                throw exception(SMS_CHANNEL_HAS_CHILDREN);
            }
        });

        // 2. Batch deletion
        smsChannelMapper.deleteByIds(ids);
    }

    private SmsChannelDO validateSmsChannelExists(Long id) {
        SmsChannelDO channel = smsChannelMapper.selectById(id);
        if (channel == null) {
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
        return channel;
    }

    @Override
    public SmsChannelDO getSmsChannel(Long id) {
        return smsChannelMapper.selectById(id);
    }

    @Override
    public List<SmsChannelDO> getSmsChannelList() {
        return smsChannelMapper.selectList();
    }

    @Override
    public PageResult<SmsChannelDO> getSmsChannelPage(SmsChannelPageReqVO pageReqVO) {
        return smsChannelMapper.selectPage(pageReqVO);
    }

    @Override
    public SmsClient getSmsClient(Long id) {
        SmsChannelDO channel = smsChannelMapper.selectById(id);
        SmsChannelProperties properties = BeanUtils.toBean(channel, SmsChannelProperties.class);
        return smsClientFactory.createOrUpdateSmsClient(properties);
    }

    @Override
    public SmsClient getSmsClient(String code) {
        return smsClientFactory.getSmsClient(code);
    }

}
