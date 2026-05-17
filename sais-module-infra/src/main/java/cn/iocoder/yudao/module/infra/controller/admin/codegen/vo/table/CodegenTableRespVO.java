package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - code generation table definition Response VO")
@Data
public class CodegenTableRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "Generate scenes, see CodegenSceneEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer scene;

    @Schema(description = "table name", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    private String tableName;

    @Schema(description = "Table description", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String tableComment;

    @Schema(description = "Remark", example = "I am a note")
    private String remark;

    @Schema(description = "module name", requiredMode = Schema.RequiredMode.REQUIRED, example = "system")
    private String moduleName;

    @Schema(description = "Business name", requiredMode = Schema.RequiredMode.REQUIRED, example = "codegen")
    private String businessName;

    @Schema(description = "class name", requiredMode = Schema.RequiredMode.REQUIRED, example = "CodegenTable")
    private String className;

    @Schema(description = "Class description", requiredMode = Schema.RequiredMode.REQUIRED, example = "Table definition for code generator")
    private String classComment;

    @Schema(description = "Author", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yudao Source Code")
    private String author;

    @Schema(description = "Template type, see CodegenTemplateTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer templateType;

    @Schema(description = "Front-end type, see CodegenFrontTypeEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
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

    @Schema(description = "primary key ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer dataSourceConfigId;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "Update Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}
