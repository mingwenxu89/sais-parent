package cn.iocoder.yudao.module.infra.service.codegen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenCreateListReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenUpdateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.CodegenTablePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.DatabaseTableRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import cn.iocoder.yudao.module.infra.dal.mysql.codegen.CodegenColumnMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.codegen.CodegenTableMapper;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenSceneEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenTemplateTypeEnum;
import cn.iocoder.yudao.module.infra.framework.codegen.config.CodegenProperties;
import cn.iocoder.yudao.module.infra.service.codegen.inner.CodegenBuilder;
import cn.iocoder.yudao.module.infra.service.codegen.inner.CodegenEngine;
import cn.iocoder.yudao.module.infra.service.db.DataSourceConfigService;
import cn.iocoder.yudao.module.infra.service.db.DatabaseTableService;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.*;

/**
 * Code generation Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class CodegenServiceImpl implements CodegenService {

    @Resource
    private DatabaseTableService databaseTableService;
    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @Resource
    private CodegenTableMapper codegenTableMapper;
    @Resource
    private CodegenColumnMapper codegenColumnMapper;

    @Resource
    private CodegenBuilder codegenBuilder;
    @Resource
    private CodegenEngine codegenEngine;

    @Resource
    private CodegenProperties codegenProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> createCodegenList(String author, CodegenCreateListReqVO reqVO) {
        List<Long> ids = new ArrayList<>(reqVO.getTableNames().size());
        // Traverse to add. Although the efficiency will be lower, there is no need to make a complete batch because it will not be such a large amount.
        reqVO.getTableNames().forEach(tableName -> ids.add(createCodegen(author, reqVO.getDataSourceConfigId(), tableName)));
        return ids;
    }

    private Long createCodegen(String author, Long dataSourceConfigId, String tableName) {
        // From the database, obtain the database table structure
        TableInfo tableInfo = databaseTableService.getTable(dataSourceConfigId, tableName);
        // import
        return createCodegen0(author, dataSourceConfigId, tableInfo);
    }

    private Long createCodegen0(String author, Long dataSourceConfigId, TableInfo tableInfo) {
        // Verify that imported tables and fields are not empty
        validateTableInfo(tableInfo);
        // Check if it already exists
        if (codegenTableMapper.selectByTableNameAndDataSourceConfigId(tableInfo.getName(),
                dataSourceConfigId) != null) {
            throw exception(CODEGEN_TABLE_EXISTS);
        }

        // Build a CodegenTableDO object and insert it into DB
        CodegenTableDO table = codegenBuilder.buildTable(tableInfo);
        table.setDataSourceConfigId(dataSourceConfigId);
        table.setScene(CodegenSceneEnum.ADMIN.getScene()); // In the default configuration, the template of the management background is used.
        table.setFrontType(codegenProperties.getFrontType());
        table.setAuthor(author);
        codegenTableMapper.insert(table);

        // Build the CodegenColumnDO array and insert it into DB
        List<CodegenColumnDO> columns = codegenBuilder.buildColumns(table.getId(), tableInfo.getFields());
        // If there is no primary key, use the first field as the primary key
        if (!tableInfo.isHavePrimaryKey()) {
            columns.get(0).setPrimaryKey(true);
        }
        codegenColumnMapper.insertBatch(columns);
        return table.getId();
    }

    @VisibleForTesting
    void validateTableInfo(TableInfo tableInfo) {
        if (tableInfo == null) {
            throw exception(CODEGEN_IMPORT_TABLE_NULL);
        }
        if (StrUtil.isEmpty(tableInfo.getComment())) {
            throw exception(CODEGEN_TABLE_INFO_TABLE_COMMENT_IS_NULL);
        }
        if (CollUtil.isEmpty(tableInfo.getFields())) {
            throw exception(CODEGEN_IMPORT_COLUMNS_NULL);
        }
        tableInfo.getFields().forEach(field -> {
            if (StrUtil.isEmpty(field.getComment())) {
                throw exception(CODEGEN_TABLE_INFO_COLUMN_COMMENT_IS_NULL, field.getName());
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCodegen(CodegenUpdateReqVO updateReqVO) {
        // Check if it already exists
        if (codegenTableMapper.selectById(updateReqVO.getTable().getId()) == null) {
            throw exception(CODEGEN_TABLE_NOT_EXISTS);
        }
        // Verify that the main table field exists
        if (Objects.equals(updateReqVO.getTable().getTemplateType(), CodegenTemplateTypeEnum.SUB.getType())) {
            if (codegenTableMapper.selectById(updateReqVO.getTable().getMasterTableId()) == null) {
                throw exception(CODEGEN_MASTER_TABLE_NOT_EXISTS, updateReqVO.getTable().getMasterTableId());
            }
            if (CollUtil.findOne(updateReqVO.getColumns(),  // The field associated with the main table does not exist
                    column -> column.getId().equals(updateReqVO.getTable().getSubJoinColumnId())) == null) {
                throw exception(CODEGEN_SUB_COLUMN_NOT_EXISTS, updateReqVO.getTable().getSubJoinColumnId());
            }
        }

        // Update table table definition
        CodegenTableDO updateTableObj = BeanUtils.toBean(updateReqVO.getTable(), CodegenTableDO.class);
        codegenTableMapper.updateById(updateTableObj);
        // Update column field definition
        List<CodegenColumnDO> updateColumnObjs = BeanUtils.toBean(updateReqVO.getColumns(), CodegenColumnDO.class);
        updateColumnObjs.forEach(updateColumnObj -> codegenColumnMapper.updateById(updateColumnObj));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCodegenFromDB(Long tableId) {
        // Check if it already exists
        CodegenTableDO table = codegenTableMapper.selectById(tableId);
        if (table == null) {
            throw exception(CODEGEN_TABLE_NOT_EXISTS);
        }
        // From the database, obtain the database table structure
        TableInfo tableInfo = databaseTableService.getTable(table.getDataSourceConfigId(), table.getTableName());
        // Perform synchronization
        syncCodegen0(tableId, tableInfo);
    }

    private void syncCodegen0(Long tableId, TableInfo tableInfo) {
        // 1. Verify that the imported tables and fields are not empty
        validateTableInfo(tableInfo);
        List<TableField> tableFields = tableInfo.getFields();

        // 2. Construct the CodegenColumnDO array and synchronize only the newly added fields
        List<CodegenColumnDO> codegenColumns = codegenColumnMapper.selectListByTableId(tableId);
        Set<String> codegenColumnNames = convertSet(codegenColumns, CodegenColumnDO::getColumnName);

        // 3.1 Calculate the fields that need to be [modified], re-insert them when inserting, and delete the original ones when deleting
        Map<String, CodegenColumnDO> codegenColumnDOMap = convertMap(codegenColumns, CodegenColumnDO::getColumnName);
        BiPredicate<TableField, CodegenColumnDO> primaryKeyPredicate =
                (tableField, codegenColumn) -> tableField.getMetaInfo().getJdbcType().name().equals(codegenColumn.getDataType())
                        && tableField.getMetaInfo().isNullable() == codegenColumn.getNullable()
                        && tableField.isKeyFlag() == codegenColumn.getPrimaryKey()
                        && tableField.getComment().equals(codegenColumn.getColumnComment());
        Set<String> modifyFieldNames = IntStream.range(0, tableFields.size()).mapToObj(index -> {
            TableField tableField = tableFields.get(index);
            String columnName = tableField.getColumnName();
            CodegenColumnDO codegenColumn = codegenColumnDOMap.get(columnName);
            if (codegenColumn == null) {
                return null;
            }
            if (!primaryKeyPredicate.test(tableField, codegenColumn) || codegenColumn.getOrdinalPosition() != index) {
                return columnName;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toSet());
        // 3.2 Calculate the fields that need to be [delete]
        Set<String> tableFieldNames = convertSet(tableFields, TableField::getName);
        Set<Long> deleteColumnIds = codegenColumns.stream()
                .filter(column -> (!tableFieldNames.contains(column.getColumnName())) || modifyFieldNames.contains(column.getColumnName()))
                .map(CodegenColumnDO::getId).collect(Collectors.toSet());
        // Remove existing fields
        tableFields.removeIf(column -> codegenColumnNames.contains(column.getColumnName()) && (!modifyFieldNames.contains(column.getColumnName())));
        if (CollUtil.isEmpty(tableFields) && CollUtil.isEmpty(deleteColumnIds)) {
            throw exception(CODEGEN_SYNC_NONE_CHANGE);
        }

        // 4.1 Insert new fields
        List<CodegenColumnDO> columns = codegenBuilder.buildColumns(tableId, tableFields);
        codegenColumnMapper.insertBatch(columns);
        // 4.2 Delete non-existing fields
        if (CollUtil.isNotEmpty(deleteColumnIds)) {
            codegenColumnMapper.deleteByIds(deleteColumnIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCodegen(Long tableId) {
        // Check if it already exists
        if (codegenTableMapper.selectById(tableId) == null) {
            throw exception(CODEGEN_TABLE_NOT_EXISTS);
        }

        // Delete table table definition
        codegenTableMapper.deleteById(tableId);
        // Delete column field definition
        codegenColumnMapper.deleteListByTableId(tableId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCodegenList(List<Long> tableIds) {
        // Delete table table definitions in batches
        codegenTableMapper.deleteByIds(tableIds);
        // Delete column field definitions in batches
        codegenColumnMapper.deleteListByTableId(tableIds);
    }

    @Override
    public List<CodegenTableDO> getCodegenTableList(Long dataSourceConfigId) {
        return codegenTableMapper.selectListByDataSourceConfigId(dataSourceConfigId);
    }

    @Override
    public PageResult<CodegenTableDO> getCodegenTablePage(CodegenTablePageReqVO pageReqVO) {
        return codegenTableMapper.selectPage(pageReqVO);
    }

    @Override
    public CodegenTableDO getCodegenTable(Long id) {
        return codegenTableMapper.selectById(id);
    }

    @Override
    public List<CodegenColumnDO> getCodegenColumnListByTableId(Long tableId) {
        return codegenColumnMapper.selectListByTableId(tableId);
    }

    @Override
    public Map<String, String> generationCodes(Long tableId) {
        // Check if it already exists
        CodegenTableDO table = codegenTableMapper.selectById(tableId);
        if (table == null) {
            throw exception(CODEGEN_TABLE_NOT_EXISTS);
        }
        List<CodegenColumnDO> columns = codegenColumnMapper.selectListByTableId(tableId);
        if (CollUtil.isEmpty(columns)) {
            throw exception(CODEGEN_COLUMN_NOT_EXISTS);
        }

        // If it is the main sub-table, load the corresponding sub-table information.
        List<CodegenTableDO> subTables = null;
        List<List<CodegenColumnDO>> subColumnsList = null;
        if (CodegenTemplateTypeEnum.isMaster(table.getTemplateType())) {
            // Checksum table exists
            subTables = codegenTableMapper.selectListByTemplateTypeAndMasterTableId(
                    CodegenTemplateTypeEnum.SUB.getType(), tableId);
            if (CollUtil.isEmpty(subTables)) {
                throw exception(CODEGEN_MASTER_GENERATION_FAIL_NO_SUB_TABLE);
            }
            // Check that the associated fields of the subtable exist
            subColumnsList = new ArrayList<>();
            for (CodegenTableDO subTable : subTables) {
                List<CodegenColumnDO> subColumns = codegenColumnMapper.selectListByTableId(subTable.getId());
                if (CollUtil.findOne(subColumns, column -> column.getId().equals(subTable.getSubJoinColumnId())) == null) {
                    throw exception(CODEGEN_SUB_COLUMN_NOT_EXISTS, subTable.getId());
                }
                subColumnsList.add(subColumns);
            }
        }

        // Get the database type corresponding to the data source
        DataSourceConfigDO dataSourceConfig = dataSourceConfigService.getDataSourceConfig(table.getDataSourceConfigId());
        DbType dbType = JdbcUtils.getDbType(dataSourceConfig.getUrl());
        // Execute build
        return codegenEngine.execute(dbType, table, columns, subTables, subColumnsList);
    }

    @Override
    public List<DatabaseTableRespVO> getDatabaseTableList(Long dataSourceConfigId, String name, String comment) {
        List<TableInfo> tables = databaseTableService.getTableList(dataSourceConfigId, name, comment);
        // Remove existing ones in Codegen
        Set<String> existsTables = convertSet(
                codegenTableMapper.selectListByDataSourceConfigId(dataSourceConfigId), CodegenTableDO::getTableName);
        tables.removeIf(table -> existsTables.contains(table.getName()));
        return BeanUtils.toBean(tables, DatabaseTableRespVO.class);
    }

}
