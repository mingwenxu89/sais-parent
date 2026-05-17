package cn.iocoder.yudao.module.infra.service.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Database table Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class DatabaseTableServiceImpl implements DatabaseTableService {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @Override
    public List<TableInfo> getTableList(Long dataSourceConfigId, String nameLike, String commentLike) {
        List<TableInfo> tables = getTableList0(dataSourceConfigId, null);
        return tables.stream().filter(tableInfo -> (StrUtil.isEmpty(nameLike) || tableInfo.getName().contains(nameLike))
                        && (StrUtil.isEmpty(commentLike) || tableInfo.getComment().contains(commentLike)))
                .collect(Collectors.toList());
    }

    @Override
    public TableInfo getTable(Long dataSourceConfigId, String name) {
        return CollUtil.getFirst(getTableList0(dataSourceConfigId, name));
    }

    private List<TableInfo> getTableList0(Long dataSourceConfigId, String name) {
        // Get data source configuration
        DataSourceConfigDO config = dataSourceConfigService.getDataSourceConfig(dataSourceConfigId);
        Assert.notNull(config, "Data source ({}) does not exist!", dataSourceConfigId);

        // Use MyBatis Plus Generator to parse table structure
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(config.getUrl(), config.getUsername(),
                config.getPassword());
        if (JdbcUtils.isSQLServer(config.getUrl())) { // Special: SQLServer jdbc is non-standard, see https://github.com/baomidou/mybatis-plus/issues/5419
            dataSourceConfigBuilder.databaseQueryClass(SQLQuery.class);
        }
        StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder().enableSkipView(); // Ignore the view, it is generally not used in business
        if (StrUtil.isNotEmpty(name)) {
            strategyConfig.addInclude(name);
        } else {
            // Remove table names from workflow and scheduled task prefixes
            strategyConfig.addExclude("ACT_[\\S\\s]+|QRTZ_[\\S\\s]+|FLW_[\\S\\s]+|act_[\\S\\s]+|qrtz_[\\S\\s]+|flw_[\\S\\s]+");
            // Remove Oracle related system tables
            strategyConfig.addExclude("IMPDP_[\\S\\s]+|ALL_[\\S\\s]+|HS_[\\S\\s]+|impdp_[\\S\\s]+|all_[\\S\\s]+|hs_[\\S\\s]+");
            strategyConfig.addExclude("[\\S\\s]+\\$[\\S\\s]+|[\\S\\s]+\\$"); // There cannot be $ in the table. Generally, it is a system table.
        }

        GlobalConfig globalConfig = new GlobalConfig.Builder().dateType(DateType.TIME_PACK).build(); // Only use LocalDateTime type, not LocalDate
        ConfigBuilder builder = new ConfigBuilder(null, dataSourceConfigBuilder.build(), strategyConfig.build(),
                null, globalConfig, null);
        // Sort by name
        List<TableInfo> tables = builder.getTableInfoList();
        tables.sort(Comparator.comparing(TableInfo::getName));
        return tables;
    }

}
