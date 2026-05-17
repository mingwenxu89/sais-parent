package cn.iocoder.yudao.framework.common.biz.system.logger;

import cn.iocoder.yudao.framework.common.biz.system.logger.dto.OperateLogCreateReqDTO;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;

/**
 * Operation log API API
 *
 * @author Yudao Source Code
 */
public interface OperateLogCommonApi {

    /**
     * Create operation log
     *
     * @param createReqDTO Request
     */
    void createOperateLog(@Valid OperateLogCreateReqDTO createReqDTO);

    /**
     * [Asynchronous] Create operation log
     *
     * @param createReqDTO Request
     */
    @Async
    default void createOperateLogAsync(OperateLogCreateReqDTO createReqDTO) {
        createOperateLog(createReqDTO);
    }

}
