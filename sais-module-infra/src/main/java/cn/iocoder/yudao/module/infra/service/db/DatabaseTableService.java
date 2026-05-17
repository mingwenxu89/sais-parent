package cn.iocoder.yudao.module.infra.service.db;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;

/**
 * Database table service
 *
 * @author Yudao Source Code
 */
public interface DatabaseTableService {

    /**
     * Get the table list and perform fuzzy matching based on table name + table description
     *
     * @param dataSourceConfigId Data source configuration ID
     * @param nameLike           Table name, fuzzy match
     * @param commentLike        Table description, fuzzy matching
     * @return table list
     */
    List<TableInfo> getTableList(Long dataSourceConfigId, String nameLike, String commentLike);

    /**
     * Get the specified table name
     *
     * @param dataSourceConfigId Data source configuration ID
     * @param tableName          table name
     * @return table
     */
    TableInfo getTable(Long dataSourceConfigId, String tableName);

}
