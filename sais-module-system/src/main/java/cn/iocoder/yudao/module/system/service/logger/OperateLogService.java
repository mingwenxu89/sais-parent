package cn.iocoder.yudao.module.system.service.logger;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.biz.system.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.yudao.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.logger.OperateLogDO;

/**
 * Operation log Service API
 *
 * @author Yudao Source Code
 */
public interface OperateLogService {

    /**
     * Record operation log
     *
     * @param createReqDTO Create request
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * Get operation log
     *
     * @param id ID
     * @return Operation log
     */
    OperateLogDO getOperateLog(Long id);

    /**
     * Get the paging list of operation logs
     *
     * @param pageReqVO Paging conditions
     * @return Operation log paging list
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    /**
     * Get the paging list of operation logs
     *
     * @param pageReqVO Paging conditions
     * @return Operation log paging list
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqDTO pageReqVO);

}
