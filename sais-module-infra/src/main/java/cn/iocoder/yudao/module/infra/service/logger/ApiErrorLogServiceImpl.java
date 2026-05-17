package cn.iocoder.yudao.module.infra.service.logger;

import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import cn.iocoder.yudao.module.infra.dal.mysql.logger.ApiErrorLogMapper;
import cn.iocoder.yudao.module.infra.enums.logger.ApiErrorLogProcessStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiErrorLogDO.REQUEST_PARAMS_MAX_LENGTH;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.API_ERROR_LOG_NOT_FOUND;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.API_ERROR_LOG_PROCESSED;

/**
 * API error log Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
@Slf4j
public class ApiErrorLogServiceImpl implements ApiErrorLogService {

    @Resource
    private ApiErrorLogMapper apiErrorLogMapper;

    @Override
    public void createApiErrorLog(ApiErrorLogCreateReqDTO createDTO) {
        ApiErrorLogDO apiErrorLog = BeanUtils.toBean(createDTO, ApiErrorLogDO.class)
                .setProcessStatus(ApiErrorLogProcessStatusEnum.INIT.getStatus());
        apiErrorLog.setRequestParams(StrUtils.maxLength(apiErrorLog.getRequestParams(), REQUEST_PARAMS_MAX_LENGTH));
        try {
            if (TenantContextHolder.getTenantId() != null) {
                apiErrorLogMapper.insert(apiErrorLog);
            } else {
                // In extreme cases, when there is no tenant in the context, the tenant context is ignored at this time to avoid insertion failure!
                TenantUtils.executeIgnore(() -> apiErrorLogMapper.insert(apiErrorLog));
            }
        } catch (Exception ex) {
            // Cover-up processing, currently only occurs in sar-cloud: https://gitee.com/yudaocode/sar-cloud-mini/issues/IC1O0A
            log.error("[createApiErrorLog][Exception occurred while logging ({})]", createDTO, ex);
        }
    }

    @Override
    public PageResult<ApiErrorLogDO> getApiErrorLogPage(ApiErrorLogPageReqVO pageReqVO) {
        return apiErrorLogMapper.selectPage(pageReqVO);
    }

    @Override
    public ApiErrorLogDO getApiErrorLog(Long id) {
        return apiErrorLogMapper.selectById(id);
    }

    @Override
    public void updateApiErrorLogProcess(Long id, Integer processStatus, Long processUserId) {
        ApiErrorLogDO errorLog = apiErrorLogMapper.selectById(id);
        if (errorLog == null) {
            throw exception(API_ERROR_LOG_NOT_FOUND);
        }
        if (!ApiErrorLogProcessStatusEnum.INIT.getStatus().equals(errorLog.getProcessStatus())) {
            throw exception(API_ERROR_LOG_PROCESSED);
        }
        // Mark processing
        apiErrorLogMapper.updateById(ApiErrorLogDO.builder().id(id).processStatus(processStatus)
                .processUserId(processUserId).processTime(LocalDateTime.now()).build());
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Integer cleanErrorLog(Integer exceedDay, Integer deleteLimit) {
        int count = 0;
        LocalDateTime expireDate = LocalDateTime.now().minusDays(exceedDay);
        // Delete in a loop until there is no data that meets the condition
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            int deleteCount = apiErrorLogMapper.deleteByCreateTimeLt(expireDate, deleteLimit);
            count += deleteCount;
            // Reaching the expected ID of deletions means it’s over
            if (deleteCount < deleteLimit) {
                break;
            }
        }
        return count;
    }

}
