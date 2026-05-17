package cn.iocoder.yudao.module.infra.service.logger;

import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiAccessLogDO;

/**
 * API access log Service API
 *
 * @author Yudao Source Code
 */
public interface ApiAccessLogService {

    /**
     * Create API access log
     *
     * @param createReqDTO API access log
     */
    void createApiAccessLog(ApiAccessLogCreateReqDTO createReqDTO);

    /**
     * Get API access logs
     *
     * @param id ID
     * @return API access log
     */
    ApiAccessLogDO getApiAccessLog(Long id);

    /**
     * Get API access log pagination
     *
     * @param pageReqVO Page query
     * @return API access log pagination
     */
    PageResult<ApiAccessLogDO> getApiAccessLogPage(ApiAccessLogPageReqVO pageReqVO);

    /**
     * Clear access logs from exceedDay days ago
     *
     * @param exceedDay   How many days will it take to clean up?
     * @param deleteLimit ID of intervals to clear
     */
    Integer cleanAccessLog(Integer exceedDay, Integer deleteLimit);

}
