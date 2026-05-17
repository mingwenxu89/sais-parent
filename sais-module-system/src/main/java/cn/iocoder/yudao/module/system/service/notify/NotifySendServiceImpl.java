package cn.iocoder.yudao.module.system.service.notify;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyTemplateDO;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * Service implementation class for sending intra-site messages
 *
 * @author xrcoder
 */
@Service
@Validated
@Slf4j
public class NotifySendServiceImpl implements NotifySendService {

    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Resource
    private NotifyMessageService notifyMessageService;

    @Override
    public Long sendSingleNotifyToAdmin(Long userId, String templateCode, Map<String, Object> templateParams) {
        return sendSingleNotify(userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleNotifyToMember(Long userId, String templateCode, Map<String, Object> templateParams) {
        return sendSingleNotify(userId, UserTypeEnum.MEMBER.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleNotify(Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        // Validation template
        NotifyTemplateDO template = validateNotifyTemplate(templateCode);
        if (Objects.equals(template.getStatus(), CommonStatusEnum.DISABLE.getStatus())) {
            log.info("[sendSingleNotify][The template ({}) has been closed and cannot be sent to the user ({}/{})]", templateCode, userId, userType);
            return null;
        }
        // Check parameters
        validateTemplateParams(template, templateParams);

        // Send site message
        String content = notifyTemplateService.formatNotifyTemplateContent(template.getContent(), templateParams);
        return notifyMessageService.createNotifyMessage(userId, userType, template, content, templateParams);
    }

    @VisibleForTesting
    public NotifyTemplateDO validateNotifyTemplate(String templateCode) {
        // Get a site letter template. Taking into account efficiency, obtain from cache
        NotifyTemplateDO template = notifyTemplateService.getNotifyTemplateByCodeFromCache(templateCode);
        // The site letter template does not exist
        if (template == null) {
            throw exception(NOTICE_NOT_FOUND);
        }
        return template;
    }

    /**
     * Verify whether the internal message template parameters are correct
     *
     * @param template Email template
     * @param templateParams Parameter list
     */
    @VisibleForTesting
    public void validateTemplateParams(NotifyTemplateDO template, Map<String, Object> templateParams) {
        template.getParams().forEach(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw exception(NOTIFY_SEND_TEMPLATE_PARAM_MISS, key);
            }
        });
    }
}
