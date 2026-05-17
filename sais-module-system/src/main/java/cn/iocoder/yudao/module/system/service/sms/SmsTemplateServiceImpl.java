package cn.iocoder.yudao.module.system.service.sms;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.template.SmsTemplatePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.template.SmsTemplateSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsTemplateDO;
import cn.iocoder.yudao.module.system.dal.mysql.sms.SmsTemplateMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import cn.iocoder.yudao.module.system.framework.sms.core.client.SmsClient;
import cn.iocoder.yudao.module.system.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * SMS template Service implementation class
 *
 * @author zzf
 * @since 2021/1/25 9:25
 */
@Service
@Slf4j
public class SmsTemplateServiceImpl implements SmsTemplateService {

    /**
     * Regular expression to match variables in {}
     */
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");

    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    @Resource
    private SmsChannelService smsChannelService;

    @Override
    public Long createSmsTemplate(SmsTemplateSaveReqVO createReqVO) {
        // Verify SMS channel
        SmsChannelDO channelDO = validateSmsChannel(createReqVO.getChannelId());
        // Verify whether the SMS code is repeated
        validateSmsTemplateCodeDuplicate(null, createReqVO.getCode());
        // Verification SMS template
        validateApiTemplate(createReqVO.getChannelId(), createReqVO.getApiTemplateId());

        // Insert
        SmsTemplateDO template = BeanUtils.toBean(createReqVO, SmsTemplateDO.class);
        template.setParams(parseTemplateContentParams(template.getContent()));
        template.setChannelCode(channelDO.getCode());
        smsTemplateMapper.insert(template);
        // Return
        return template.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.SMS_TEMPLATE,
            allEntries = true) // allEntries Clear all caches, because the code field may be modified and it is difficult to clean.
    public void updateSmsTemplate(SmsTemplateSaveReqVO updateReqVO) {
        // Check existence
        validateSmsTemplateExists(updateReqVO.getId());
        // Verify SMS channel
        SmsChannelDO channelDO = validateSmsChannel(updateReqVO.getChannelId());
        // Verify whether the SMS code is repeated
        validateSmsTemplateCodeDuplicate(updateReqVO.getId(), updateReqVO.getCode());
        // Verification SMS template
        validateApiTemplate(updateReqVO.getChannelId(), updateReqVO.getApiTemplateId());

        // Update
        SmsTemplateDO updateObj = BeanUtils.toBean(updateReqVO, SmsTemplateDO.class);
        updateObj.setParams(parseTemplateContentParams(updateObj.getContent()));
        updateObj.setChannelCode(channelDO.getCode());
        smsTemplateMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.SMS_TEMPLATE,
            allEntries = true) // allEntries Clear all caches, because the id is not a direct cache code and is not easy to clean.
    public void deleteSmsTemplate(Long id) {
        // Check existence
        validateSmsTemplateExists(id);
        // Update
        smsTemplateMapper.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.SMS_TEMPLATE,
            allEntries = true) // allEntries Clear all caches, because the id is not a direct cache code and is not easy to clean.
    public void deleteSmsTemplateList(List<Long> ids) {
        smsTemplateMapper.deleteByIds(ids);
    }

    private void validateSmsTemplateExists(Long id) {
        if (smsTemplateMapper.selectById(id) == null) {
            throw exception(SMS_TEMPLATE_NOT_EXISTS);
        }
    }

    @Override
    public SmsTemplateDO getSmsTemplate(Long id) {
        return smsTemplateMapper.selectById(id);
    }

    @Override
    @Cacheable(cacheNames = RedisKeyConstants.SMS_TEMPLATE, key = "#code",
            unless = "#result == null")
    public SmsTemplateDO getSmsTemplateByCodeFromCache(String code) {
        return smsTemplateMapper.selectByCode(code);
    }

    @Override
    public PageResult<SmsTemplateDO> getSmsTemplatePage(SmsTemplatePageReqVO pageReqVO) {
        return smsTemplateMapper.selectPage(pageReqVO);
    }

    @Override
    public Long getSmsTemplateCountByChannelId(Long channelId) {
        return smsTemplateMapper.selectCountByChannelId(channelId);
    }

    @VisibleForTesting
    public SmsChannelDO validateSmsChannel(Long channelId) {
        SmsChannelDO channelDO = smsChannelService.getSmsChannel(channelId);
        if (channelDO == null) {
            throw exception(SMS_CHANNEL_NOT_EXISTS);
        }
        if (CommonStatusEnum.isDisable(channelDO.getStatus())) {
            throw exception(SMS_CHANNEL_DISABLE);
        }
        return channelDO;
    }

    @VisibleForTesting
    public void validateSmsTemplateCodeDuplicate(Long id, String code) {
        SmsTemplateDO template = smsTemplateMapper.selectByCode(code);
        if (template == null) {
            return;
        }
        // If id is empty, it means that there is no need to compare whether it is a dict type with the same id.
        if (id == null) {
            throw exception(SMS_TEMPLATE_CODE_DUPLICATE, code);
        }
        if (!template.getId().equals(id)) {
            throw exception(SMS_TEMPLATE_CODE_DUPLICATE, code);
        }
    }

    /**
     * Verify whether the template of the API SMS platform is valid
     *
     * @param channelId Channel ID
     * @param apiTemplateId API template ID
     */
    @VisibleForTesting
    void validateApiTemplate(Long channelId, String apiTemplateId) {
        // Get SMS template
        SmsClient smsClient = smsChannelService.getSmsClient(channelId);
        Assert.notNull(smsClient, String.format("SMS client (%d) does not exist", channelId));
        SmsTemplateRespDTO template;
        try {
            template = smsClient.getSmsTemplate(apiTemplateId);
        } catch (Throwable ex) {
            throw exception(SMS_TEMPLATE_API_ERROR, ExceptionUtil.getRootCauseMessage(ex));
        }
        // Verify SMS template
        if (template == null) {
            throw exception(SMS_TEMPLATE_API_NOT_FOUND);
        }
        if (Objects.equals(template.getAuditStatus(), SmsTemplateAuditStatusEnum.CHECKING.getStatus())) {
            throw exception(SMS_TEMPLATE_API_AUDIT_CHECKING);
        }
        if (Objects.equals(template.getAuditStatus(), SmsTemplateAuditStatusEnum.FAIL.getStatus())) {
            throw exception(SMS_TEMPLATE_API_AUDIT_FAIL, template.getAuditReason());
        }
        Assert.equals(template.getAuditStatus(), SmsTemplateAuditStatusEnum.SUCCESS.getStatus(),
                String.format("SMS template (%s) review status (%d) is incorrect", apiTemplateId, template.getAuditStatus()));
    }

    @Override
    public String formatSmsTemplateContent(String content, Map<String, Object> params) {
        return StrUtil.format(content, params);
    }

    @VisibleForTesting
    List<String> parseTemplateContentParams(String content) {
        return ReUtil.findAllGroup1(PATTERN_PARAMS, content);
    }

}
