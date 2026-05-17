package cn.iocoder.yudao.module.infra.service.db;

import cn.iocoder.yudao.module.infra.controller.admin.db.vo.DataSourceConfigSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Data source configuration Service API
 *
 * @author Yudao Source Code
 */
public interface DataSourceConfigService {

    /**
     * Create data source configuration
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createDataSourceConfig(@Valid DataSourceConfigSaveReqVO createReqVO);

    /**
     * Update data source configuration
     *
     * @param updateReqVO Update information
     */
    void updateDataSourceConfig(@Valid DataSourceConfigSaveReqVO updateReqVO);

    /**
     * Delete data source configuration
     *
     * @param id ID
     */
    void deleteDataSourceConfig(Long id);

    /**
     * Delete data source configurations in batches
     *
     * @param ids IDed list
     */
    void deleteDataSourceConfigList(List<Long> ids);

    /**
     * Get data source configuration
     *
     * @param id ID
     * @return Data source configuration
     */
    DataSourceConfigDO getDataSourceConfig(Long id);

    /**
     * Get the data source configuration list
     *
     * @return Data source configuration list
     */
    List<DataSourceConfigDO> getDataSourceConfigList();

}
