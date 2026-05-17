package cn.iocoder.yudao.framework.common.biz.infra.logger;

import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiErrorLogCreateReqDTO;
import org.springframework.scheduling.annotation.Async;

import jakarta.validation.Valid;

/**
 * API interface for API error log
 *
 * @author Yudao Source Code
 */
public interface ApiErrorLogCommonApi {

 /**
     * Create API error log
 *
     * @param createDTO Create information
 */
 void createApiErrorLog(@Valid ApiErrorLogCreateReqDTO createDTO);

 /**
     * [Asynchronous] Create API exception log
 *
     * @param createDTO Exception log DTO
 */
 @Async
 default void createApiErrorLogAsync(ApiErrorLogCreateReqDTO createDTO) {
 createApiErrorLog(createDTO);
 }

}
