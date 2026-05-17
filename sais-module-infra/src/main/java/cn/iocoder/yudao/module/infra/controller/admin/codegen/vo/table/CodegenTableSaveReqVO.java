package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenSceneEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenTemplateTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management background - code generation table definition creation/modification Response VO")
@Data
public class CodegenTableSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "Generate scenes, see CodegenSceneEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Import type cannot be empty")
    private Integer scene;

    @Schema(description = "table name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotNull(message = "Table name cannot be empty")
    private String tableName;

    @Schema(description = "Table description", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @NotNull(message = "Table description cannot be empty")
    private String tableComment;

    @Schema(description = "Remark", example = "I am a note")
    private String remark;

    @Schema(description = "module name", requiredMode = Schema.RequiredMode.REQUIRED, example = "system")
    @NotNull(message = "Module name cannot be empty")
    private String moduleName;

    @Schema(description = "Business name", requiredMode = Schema.RequiredMode.REQUIRED, example = "codegen")
    @NotNull(message = "Business name cannot be empty")
    private String businessName;

    @Schema(description = "class name", requiredMode = Schema.RequiredMode.REQUIRED, example = "CodegenTable")
    @NotNull(message = "Class name cannot be empty")
    private String className;

    @Schema(description = "Class description", requiredMode = Schema.RequiredMode.REQUIRED, example = "Table definition for code generator")
    @NotNull(message = "Class description cannot be empty")
    private String classComment;

    @Schema(description = "Author", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yudao Source Code")
    @NotNull(message = "Author cannot be empty")
    private String author;

    @Schema(description = "Template type, see CodegenTemplateTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Template type cannot be empty")
    private Integer templateType;

    @Schema(description = "Front-end type, see CodegenFrontTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "Frontend type cannot be empty")
    private Integer frontType;

    @Schema(description = "Parent menu ID", example = "1024")
    private Long parentMenuId;

    @Schema(description = "Main table ID", example = "2048")
    private Long masterTableId;
    @Schema(description = "The field ID of the main table associated with the subtable", example = "4096")
    private Long subJoinColumnId;
    @Schema(description = "Whether the main table and sub-table are one-to-many", example = "4096")
    private Boolean subJoinMany;

    @Schema(description = "The parent field ID of the tree table", example = "8192")
    private Long treeParentColumnId;
    @Schema(description = "The name field ID of the tree table", example = "16384")
    private Long treeNameColumnId;

    @AssertTrue(message = "The upper-level menu cannot be empty, please go to the [Modify generation configuration -> Generation information] API and set the upper-level menu")
    @JsonIgnore
    public boolean isParentMenuIdValid() {
        // When the generated scene is the management background, the upper-level menu must be set, otherwise the generated menu SQL will have no parent menu.
        return ObjectUtil.notEqual(getScene(), CodegenSceneEnum.ADMIN.getScene())
                || getParentMenuId() != null;
    }

    @AssertTrue(message = "The associated parent table information is incomplete")
    @JsonIgnore
    public boolean isSubValid() {
        return ObjectUtil.notEqual(getTemplateType(), CodegenTemplateTypeEnum.SUB)
                || (ObjectUtil.isAllNotEmpty(masterTableId, subJoinColumnId, subJoinMany));
    }

    @AssertTrue(message = "The associated tree table information is incomplete")
    @JsonIgnore
    public boolean isTreeValid() {
        return ObjectUtil.notEqual(templateType, CodegenTemplateTypeEnum.TREE)
                || (ObjectUtil.isAllNotEmpty(treeParentColumnId, treeNameColumnId));
    }

}
