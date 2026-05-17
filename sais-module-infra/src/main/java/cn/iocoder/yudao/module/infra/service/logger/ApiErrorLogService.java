package cn.iocoder.yudao.module.infra.service.logger;

import cn.iocoder.yudao.framework.common.biz.infra.logger.dto.ApiErrorLogCreateReqDTO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiErrorLogDO;

/**
 * API Error Log Service API
 *
 * @author Yudao Source Code
 */
public interface ApiErrorLogService {

    /**
     * Create API error log
     *
     * @param createReqDTO API error log
     */
    void createApiErrorLog(ApiErrorLogCreateReqDTO createReqDTO);

    /**
     * Get API error log
     *
     * @param id ID
     * @return API error log
     */
    ApiErrorLogDO getApiErrorLog(Long id);

    /**
     * Get API error log pagination
     *
     * @param pageReqVO Page query
     * @return API error log pagination
     */
    PageResult<ApiErrorLogDO> getApiErrorLogPage(ApiErrorLogPageReqVO pageReqVO);

    /**
     * Update API error log handled
     *
     * @param id            API log ID
     * @param processStatus Processing results
     * @param processUserId Processor
     */
    void updateApiErrorLogProcess(Long id, Integer processStatus, Long processUserId);

    /**
     * Clear error logs from exceedDay days ago
     *
     * @param exceedDay   How many days will it take to clean up?
     * @param deleteLimit ID of intervals to clear
     */
    Integer cleanErrorLog(Integer exceedDay, Integer deleteLimit);

}
