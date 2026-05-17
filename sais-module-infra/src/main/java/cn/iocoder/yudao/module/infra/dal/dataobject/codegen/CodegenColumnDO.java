package cn.iocoder.yudao.module.infra.dal.dataobject.codegen;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenColumnHtmlTypeEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenColumnListConditionEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import lombok.Data;

/**
 * Code generation column field definition
 *
 * @author Yudao Source Code
 */
@TableName(value = "infra_codegen_column", autoResultMap = true)
@KeySequence("infra_codegen_column_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@TenantIgnore
public class CodegenColumnDO extends BaseDO {

    /**
     * ID ID
     */
    @TableId
    private Long id;
    /**
     * table ID
     * <p>
     * Association {@link CodegenTableDO#getId()}
     */
    private Long tableId;

    // ========== Table related fields ==========

    /**
     * Field name
     *
     * Association {@link TableField#getName()}
     */
    private String columnName;
    /**
     * Database field type
     *
     * Association {@link TableField.MetaInfo#getJdbcType()}
     */
    private String dataType;
    /**
     * Field description
     *
     * Association {@link TableField#getComment()}
     */
    private String columnComment;
    /**
     * Whether to allow empty
     *
     * Association {@link TableField.MetaInfo#isNullable()}
     */
    private Boolean nullable;
    /**
     * Is it a primary key?
     *
     * Association {@link TableField#isKeyFlag()}
     */
    private Boolean primaryKey;
    /**
     * sort
     */
    private Integer ordinalPosition;

    // ========== Java related fields ==========

    /**
     * Java property types
     *
     * For example, String, Boolean, etc.
     *
     * Association {@link TableField#getColumnType()}
     */
    private String javaType;
    /**
     * Java attribute name
     *
     * Association {@link TableField#getPropertyName()}
     */
    private String javaField;
    /**
     * dict type
     * <p>
     * Associated DictTypeDO's type attribute
     */
    private String dictType;
    /**
     * Data example, mainly used to generate the example field of Swagger annotations
     */
    private String example;

    // ========== CRUD related fields ==========

    /**
     * Whether to create fields for the Create operation
     */
    private Boolean createOperation;
    /**
     * Whether to update the fields for Update operation
     */
    private Boolean updateOperation;
    /**
     * Whether it is a field for List query operation
     */
    private Boolean listOperation;
    /**
     * List query operation condition type
     * <p>
     * Enum {@link CodegenColumnListConditionEnum}
     */
    private String listOperationCondition;
    /**
     * Whether it is the return field of List query operation
     */
    private Boolean listOperationResult;

    // ========== UI related fields ==========

    /**
     * display type
     * <p>
     * Enum {@link CodegenColumnHtmlTypeEnum}
     */
    private String htmlType;

}
