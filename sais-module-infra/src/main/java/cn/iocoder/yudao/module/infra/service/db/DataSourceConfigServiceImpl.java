package cn.iocoder.yudao.module.infra.service.db;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import cn.iocoder.yudao.module.infra.controller.admin.db.vo.DataSourceConfigSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import cn.iocoder.yudao.module.infra.dal.mysql.db.DataSourceConfigMapper;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.DATA_SOURCE_CONFIG_NOT_EXISTS;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.DATA_SOURCE_CONFIG_NOT_OK;

/**
 * Data source configuration Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
public class DataSourceConfigServiceImpl implements DataSourceConfigService {

    @Resource
    private DataSourceConfigMapper dataSourceConfigMapper;

    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Override
    public Long createDataSourceConfig(DataSourceConfigSaveReqVO createReqVO) {
        DataSourceConfigDO config = BeanUtils.toBean(createReqVO, DataSourceConfigDO.class);
        validateConnectionOK(config);

        // Insert
        dataSourceConfigMapper.insert(config);
        // Return
        return config.getId();
    }

    @Override
    public void updateDataSourceConfig(DataSourceConfigSaveReqVO updateReqVO) {
        // Check existence
        validateDataSourceConfigExists(updateReqVO.getId());
        DataSourceConfigDO updateObj = BeanUtils.toBean(updateReqVO, DataSourceConfigDO.class);
        validateConnectionOK(updateObj);

        // Update
        dataSourceConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteDataSourceConfig(Long id) {
        // Check existence
        validateDataSourceConfigExists(id);
        // Delete
        dataSourceConfigMapper.deleteById(id);
    }

    @Override
    public void deleteDataSourceConfigList(List<Long> ids) {
        dataSourceConfigMapper.deleteByIds(ids);
    }

    private void validateDataSourceConfigExists(Long id) {
        if (dataSourceConfigMapper.selectById(id) == null) {
            throw exception(DATA_SOURCE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public DataSourceConfigDO getDataSourceConfig(Long id) {
        // If id is 0, it defaults to the data source of master
        if (Objects.equals(id, DataSourceConfigDO.ID_MASTER)) {
            return buildMasterDataSourceConfig();
        }
        // Read from DB
        return dataSourceConfigMapper.selectById(id);
    }

    @Override
    public List<DataSourceConfigDO> getDataSourceConfigList() {
        List<DataSourceConfigDO> result = dataSourceConfigMapper.selectList();
        // Supplement master data source
        result.add(0, buildMasterDataSourceConfig());
        return result;
    }

    private void validateConnectionOK(DataSourceConfigDO config) {
        boolean success = JdbcUtils.isConnectionOK(config.getUrl(), config.getUsername(), config.getPassword());
        if (!success) {
            throw exception(DATA_SOURCE_CONFIG_NOT_OK);
        }
    }

    private DataSourceConfigDO buildMasterDataSourceConfig() {
        String primary = dynamicDataSourceProperties.getPrimary();
        DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource().get(primary);
        return new DataSourceConfigDO().setId(DataSourceConfigDO.ID_MASTER).setName(primary)
                .setUrl(dataSourceProperty.getUrl())
                .setUsername(dataSourceProperty.getUsername())
                .setPassword(dataSourceProperty.getPassword());
    }

}
