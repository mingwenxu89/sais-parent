package cn.iocoder.yudao.framework.common.biz.infra.logger;

import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiAccessLogCreateReqDTO;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;

/**
 * API API for API access logs
 *
 * @author Yudao Source Code
 */
public interface ApiAccessLogCommonApi {

    /**
     * Create API access log
     *
     * @param createDTO Create information
     */
    void createApiAccessLog(@Valid ApiAccessLogCreateReqDTO createDTO);

    /**
     * [Asynchronous] Create API access log
     *
     * @param createDTO Access log DTO
     */
    @Async
    default void createApiAccessLogAsync(ApiAccessLogCreateReqDTO createDTO) {
        createApiAccessLog(createDTO);
    }

}
