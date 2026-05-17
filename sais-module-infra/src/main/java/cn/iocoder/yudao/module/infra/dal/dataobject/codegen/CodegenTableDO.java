package cn.iocoder.yudao.module.infra.dal.dataobject.codegen;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.dal.dataobject.db.DataSourceConfigDO;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenFrontTypeEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenSceneEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenTemplateTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;

/**
 * Code generation table table definition
 *
 * @author Yudao Source Code
 */
@TableName(value = "infra_codegen_table", autoResultMap = true)
@KeySequence("infra_codegen_table_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@TenantIgnore
public class CodegenTableDO extends BaseDO {

    /**
     * ID ID
     */
    @TableId
    private Long id;

    /**
     * Data source ID
     *
     * Association {@link DataSourceConfigDO#getId()}
     */
    private Long dataSourceConfigId;
    /**
     * Generate scene
     *
     * Enum {@link CodegenSceneEnum}
     */
    private Integer scene;

    // ========== Table related fields ==========

    /**
     * table name
     *
     * Association {@link TableInfo#getName()}
     */
    private String tableName;
    /**
     * Table description
     *
     * Association {@link TableInfo#getComment()}
     */
    private String tableComment;
    /**
     * Remark
     */
    private String remark;

    // ========== Class related fields ==========

    /**
     * Module name, i.e. first-level directory
     *
     * For example, system, infra, tool, etc.
     */
    private String moduleName;
    /**
     * Business name, i.e. secondary directory
     *
     * For example, user, permission, dict, etc.
     */
    private String businessName;
    /**
     * Class name (first letter capitalized)
     *
     * For example, SysUser, SysMenu, SysDictData, etc.
     */
    private String className;
    /**
     * Class description
     */
    private String classComment;
    /**
     * Author
     */
    private String author;

    // ========== Generate related fields ==========

    /**
     * template type
     *
     * Enum {@link CodegenTemplateTypeEnum}
     */
    private Integer templateType;
    /**
     * Code generated frontend type
     *
     * Enum {@link CodegenFrontTypeEnum}
     */
    private Integer frontType;

    // ========== Menu related fields ==========

    /**
     * Parent menu ID
     *
     * The id attribute associated with MenuDO
     */
    private Long parentMenuId;

    // ========== Related fields of master and child tables ==========

    /**
     * Main table ID
     *
     * Association {@link CodegenTableDO#getId()}
     */
    private Long masterTableId;
    /**
     * [Self] The field ID of the main table associated with the subtable
     *
     * Association {@link CodegenColumnDO#getId()}
     */
    private Long subJoinColumnId;
    /**
     * Whether the main table and sub-table are one-to-many
     *
     * true: one-to-many
     * false: one to one
     */
    private Boolean subJoinMany;

    // ========== Tree table related fields ==========

    /**
     * The parent field ID of the tree table
     *
     * Association {@link CodegenColumnDO#getId()}
     */
    private Long treeParentColumnId;
    /**
     * The name field ID of the tree table
     *
     * The purpose of the name: when adding or modifying, the field displayed in the select box
     *
     * Association {@link CodegenColumnDO#getId()}
     */
    private Long treeNameColumnId;

}
