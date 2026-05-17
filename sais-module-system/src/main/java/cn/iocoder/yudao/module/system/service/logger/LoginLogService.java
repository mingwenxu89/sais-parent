package cn.iocoder.yudao.module.system.service.logger;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.logger.vo.loginlog.LoginLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.logger.LoginLogDO;

import jakarta.validation.Valid;

/**
 * Login log service API
 */
public interface LoginLogService {

    /**
     * Get login log
     *
     * @param id ID
     * @return Login log
     */
    LoginLogDO getLoginLog(Long id);

    /**
     * Get login log pagination
     *
     * @param pageReqVO Paging conditions
     * @return Login log paging
     */
    PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO pageReqVO);

    /**
     * Create login log
     *
     * @param reqDTO Log information
     */
    void createLoginLog(@Valid LoginLogCreateReqDTO reqDTO);

}
