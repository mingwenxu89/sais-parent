package cn.iocoder.yudao.module.system.api.logger;

import cn.iocoder.yudao.framework.common.biz.system.logger.OperateLogCommonApi;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.iocoder.yudao.module.system.api.logger.dto.OperateLogRespDTO;

/**
 * Operation log API API
 *
 * @author Yudao Source Code
 */
public interface OperateLogApi extends OperateLogCommonApi {

    /**
     * Get the operation log pagination of the specified data of the specified module
     *
     * @param pageReqDTO Request
     * @return Operation log paging
     */
    PageResult<OperateLogRespDTO> getOperateLogPage(OperateLogPageReqDTO pageReqDTO);

}
