package cn.iocoder.yudao.module.infra.service.config;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.config.vo.ConfigSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Parameter configuration Service API
 *
 * @author Yudao Source Code
 */
public interface ConfigService {

    /**
     * Create parameter configuration
     *
     * @param createReqVO Create information
     * @return Configuration ID
     */
    Long createConfig(@Valid ConfigSaveReqVO createReqVO);

    /**
     * Update parameter configuration
     *
     * @param updateReqVO Update information
     */
    void updateConfig(@Valid ConfigSaveReqVO updateReqVO);

    /**
     * Delete parameter configuration
     *
     * @param id Configuration ID
     */
    void deleteConfig(Long id);

    /**
     * Delete parameter configurations in batches
     *
     * @param ids Configuration ID list
     */
    void deleteConfigList(List<Long> ids);

    /**
     * Get parameter configuration
     *
     * @param id Configuration ID
     * @return Parameter configuration
     */
    ConfigDO getConfig(Long id);

    /**
     * According to the parameter key, obtain the parameter configuration
     *
     * @param key configuration key
     * @return Parameter configuration
     */
    ConfigDO getConfigByKey(String key);

    /**
     * Get the paginated list of parameter configurations
     *
     * @param reqVO Paging conditions
     * @return Paginated list
     */
    PageResult<ConfigDO> getConfigPage(ConfigPageReqVO reqVO);

}
