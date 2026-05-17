package cn.iocoder.yudao.module.system.api.logger;

import cn.iocoder.yudao.module.system.api.logger.dto.LoginLogCreateReqDTO;

import jakarta.validation.Valid;

/**
 * Login log API API
 *
 * @author Yudao Source Code
 */
public interface LoginLogApi {

    /**
     * Create login log
     *
     * @param reqDTO Log information
     */
    void createLoginLog(@Valid LoginLogCreateReqDTO reqDTO);

}
