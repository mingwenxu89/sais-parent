package cn.iocoder.yudao.module.infra.service.codegen.inner;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.infra.convert.codegen.CodegenConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenColumnHtmlTypeEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenColumnListConditionEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenTemplateTypeEnum;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static cn.hutool.core.text.CharSequenceUtil.*;
import static cn.hutool.core.util.RandomUtil.randomEle;
import static cn.hutool.core.util.RandomUtil.randomInt;

/**
 * The Builder of the code generator, responsible for:
 * 1. Define the database table {@link TableInfo} and build it into {@link CodegenTableDO}
 * 2. Define the database column {@link TableField} into {@link CodegenColumnDO}
 */
@Component
public class CodegenBuilder {

    /**
     * Default mapping of field names to {@link CodegenColumnListConditionEnum}
     * Note that fields are matched in a suffix manner
     */
    private static final Map<String, CodegenColumnListConditionEnum> COLUMN_LIST_OPERATION_CONDITION_MAPPINGS =
            MapUtil.<String, CodegenColumnListConditionEnum>builder()
                    .put("name", CodegenColumnListConditionEnum.LIKE)
                    .put("time", CodegenColumnListConditionEnum.BETWEEN)
                    .put("date", CodegenColumnListConditionEnum.BETWEEN)
                    .build();

    /**
     * Default mapping of field names to {@link CodegenColumnHtmlTypeEnum}
     * Note that fields are matched in a suffix manner
     */
    private static final Map<String, CodegenColumnHtmlTypeEnum> COLUMN_HTML_TYPE_MAPPINGS =
            MapUtil.<String, CodegenColumnHtmlTypeEnum>builder()
                    .put("status", CodegenColumnHtmlTypeEnum.RADIO)
                    .put("sex", CodegenColumnHtmlTypeEnum.RADIO)
                    .put("type", CodegenColumnHtmlTypeEnum.SELECT)
                    .put("image", CodegenColumnHtmlTypeEnum.IMAGE_UPLOAD)
                    .put("file", CodegenColumnHtmlTypeEnum.FILE_UPLOAD)
                    .put("content", CodegenColumnHtmlTypeEnum.EDITOR)
                    .put("description", CodegenColumnHtmlTypeEnum.EDITOR)
                    .put("demo", CodegenColumnHtmlTypeEnum.EDITOR)
                    .put("time", CodegenColumnHtmlTypeEnum.DATETIME)
                    .put("date", CodegenColumnHtmlTypeEnum.DATETIME)
                    .build();

    /**
     * Field name of multi-tenant ID
     */
    public static final String TENANT_ID_FIELD = "tenantId";
    /**
     * Fields of {@link cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO}
     */
    public static final Set<String> BASE_DO_FIELDS = new HashSet<>();
    /**
     * New operation, no fields required to be passed
     */
    private static final Set<String> CREATE_OPERATION_EXCLUDE_COLUMN = Sets.newHashSet("id");
    /**
     * Modification operation, no passed fields are required
     */
    private static final Set<String> UPDATE_OPERATION_EXCLUDE_COLUMN = Sets.newHashSet();
    /**
     * Conditions for list operations, fields that DO not need to be passed
     */
    private static final Set<String> LIST_OPERATION_EXCLUDE_COLUMN = Sets.newHashSet("id");
    /**
     * The result of a list operation, no returned fields are required
     */
    private static final Set<String> LIST_OPERATION_RESULT_EXCLUDE_COLUMN = Sets.newHashSet();

    static {
        Arrays.stream(ReflectUtil.getFields(BaseDO.class)).forEach(field -> BASE_DO_FIELDS.add(field.getName()));
        BASE_DO_FIELDS.add(TENANT_ID_FIELD);
        // Handle OPERATION related fields
        CREATE_OPERATION_EXCLUDE_COLUMN.addAll(BASE_DO_FIELDS);
        UPDATE_OPERATION_EXCLUDE_COLUMN.addAll(BASE_DO_FIELDS);
        LIST_OPERATION_EXCLUDE_COLUMN.addAll(BASE_DO_FIELDS);
        LIST_OPERATION_EXCLUDE_COLUMN.remove("createTime"); // The creation time may still need to be passed.
        LIST_OPERATION_RESULT_EXCLUDE_COLUMN.addAll(BASE_DO_FIELDS);
        LIST_OPERATION_RESULT_EXCLUDE_COLUMN.remove("createTime"); // The creation time still needs to be returned.
    }

    public CodegenTableDO buildTable(TableInfo tableInfo) {
        CodegenTableDO table = CodegenConvert.INSTANCE.convert(tableInfo);
        initTableDefault(table);
        return table;
    }

    /**
     * Initialize the default fields of the Table table
     *
     * @param table table definition
     */
    private void initTableDefault(CodegenTableDO table) {
        // Take system_dept as an example. moduleName is system, businessName is dept, className is Dept
        // If you want to prefix it with System, you can manually change the entity class name to SystemDept in [Code Generation - Modify Generation Configuration - Basic Information]
        String tableName = table.getTableName().toLowerCase();
        // In the first step, the _ prefix is used as the module name; in the second step, the moduleName must be lowercase;
        table.setModuleName(subBefore(tableName, '_', false).toLowerCase());
        // In the first step, after the first _ prefix, it is used as the module name; in the second step, if there may be multiple _, it is converted into camel case; in the third step, the businessName must be lowercase;
        table.setBusinessName(toCamelCase(subAfter(tableName, '_', false)).toLowerCase());
        // Camel case + capitalize the first letter; the first step, after the first _ prefix, is used as the class name; the second step, camel case naming
        table.setClassName(upperFirst(toCamelCase(subAfter(tableName, '_', false))));
        // Remove the ending table as a class description
        table.setClassComment(StrUtil.removeSuffixIgnoreCase(table.getTableComment(), "table"));
        table.setTemplateType(CodegenTemplateTypeEnum.ONE.getType());
    }

    public List<CodegenColumnDO> buildColumns(Long tableId, List<TableField> tableFields) {
        List<CodegenColumnDO> columns = CodegenConvert.INSTANCE.convertList(tableFields);
        int index = 1;
        for (CodegenColumnDO column : columns) {
            column.setTableId(tableId);
            column.setOrdinalPosition(index++);
            // Special handling: Byte => Integer
            if (Byte.class.getSimpleName().equals(column.getJavaType())) {
                column.setJavaType(Integer.class.getSimpleName());
            }
            // Initialize the default fields of the Column column
            processColumnOperation(column); // Handling default values for CRUD related fields
            processColumnUI(column); // Handle default values for UI related fields
            processColumnExample(column); // Swagger example for handling fields
        }
        return columns;
    }

    private void processColumnOperation(CodegenColumnDO column) {
        // Handle the createOperation field
        column.setCreateOperation(!CREATE_OPERATION_EXCLUDE_COLUMN.contains(column.getJavaField())
                && !column.getPrimaryKey()); // For primary keys, no need to pass when creating
        // Handle updateOperation field
        column.setUpdateOperation(!UPDATE_OPERATION_EXCLUDE_COLUMN.contains(column.getJavaField())
                || column.getPrimaryKey()); // For the primary key, you need to pass it when updating
        // Handle listOperation fields
        column.setListOperation(!LIST_OPERATION_EXCLUDE_COLUMN.contains(column.getJavaField())
                && !column.getPrimaryKey()); // For primary keys, list filtering does not need to be passed
        // Handling the listOperationCondition field
        COLUMN_LIST_OPERATION_CONDITION_MAPPINGS.entrySet().stream()
                .filter(entry -> StrUtil.endWithIgnoreCase(column.getJavaField(), entry.getKey()))
                .findFirst().ifPresent(entry -> column.setListOperationCondition(entry.getValue().getCondition()));
        if (column.getListOperationCondition() == null) {
            column.setListOperationCondition(CodegenColumnListConditionEnum.EQ.getCondition());
        }
        // Handling the listOperationResult field
        column.setListOperationResult(!LIST_OPERATION_RESULT_EXCLUDE_COLUMN.contains(column.getJavaField()));
    }

    private void processColumnUI(CodegenColumnDO column) {
        // Match based on suffix
        COLUMN_HTML_TYPE_MAPPINGS.entrySet().stream()
                .filter(entry -> StrUtil.endWithIgnoreCase(column.getJavaField(), entry.getKey()))
                .findFirst().ifPresent(entry -> column.setHtmlType(entry.getValue().getType()));
        // If it is a Boolean type, set it to radio type.
        if (Boolean.class.getSimpleName().equals(column.getJavaType())) {
            column.setHtmlType(CodegenColumnHtmlTypeEnum.RADIO.getType());
        }
        // If it is LocalDateTime type, set to datetime type
        if (LocalDateTime.class.getSimpleName().equals(column.getJavaType())) {
            column.setHtmlType(CodegenColumnHtmlTypeEnum.DATETIME.getType());
        }
        // To be clear, the default setting is input type.
        if (column.getHtmlType() == null) {
            column.setHtmlType(CodegenColumnHtmlTypeEnum.INPUT.getType());
        }
    }

    /**
     * Swagger example for handling fields
     *
     * @param column Field
     */
    private void processColumnExample(CodegenColumnDO column) {
        // id, price, count, etc. may be suffixes of integers
        if (StrUtil.endWithAnyIgnoreCase(column.getJavaField(), "id", "price", "count")) {
            column.setExample(String.valueOf(randomInt(1, Short.MAX_VALUE)));
            return;
        }
        // name
        if (StrUtil.endWithIgnoreCase(column.getJavaField(), "name")) {
            column.setExample(randomEle(new String[]{"Zhang San", "John Doe", "Wang Wu", "Zhao Liu", "Yunai"}));
            return;
        }
        // status
        if (StrUtil.endWithAnyIgnoreCase(column.getJavaField(), "status", "type")) {
            column.setExample(randomEle(new String[]{"1", "2"}));
            return;
        }
        // url
        if (StrUtil.endWithIgnoreCase(column.getColumnName(), "url")) {
            column.setExample("https://www.iocoder.cn");
            return;
        }
        // reason
        if (StrUtil.endWithIgnoreCase(column.getColumnName(), "reason")) {
            column.setExample(randomEle(new String[]{"don't like", "Wrong", "Not good", "Not fragrant"}));
            return;
        }
        // description、memo、remark
        if (StrUtil.endWithAnyIgnoreCase(column.getColumnName(), "description", "memo", "remark")) {
            column.setExample(randomEle(new String[]{"Guess", "Whatever", "you are right"}));
        }
    }

}
