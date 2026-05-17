package cn.iocoder.yudao.module.infra.dal.mysql.db;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Data source configuration Mapper
 *
 * @author Yudao Source Code
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
