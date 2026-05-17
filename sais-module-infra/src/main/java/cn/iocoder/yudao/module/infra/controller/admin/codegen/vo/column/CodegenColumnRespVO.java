package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.column;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management backend - code generation field definition Response VO")
@Data
public class CodegenColumnRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "table ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long tableId;

    @Schema(description = "Field name", requiredMode = Schema.RequiredMode.REQUIRED, example = "user_age")
    private String columnName;

    @Schema(description = "Field type", requiredMode = Schema.RequiredMode.REQUIRED, example = "int(11)")
    private String dataType;

    @Schema(description = "Field description", requiredMode = Schema.RequiredMode.REQUIRED, example = "age")
    private String columnComment;

    @Schema(description = "Whether to allow empty", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean nullable;

    @Schema(description = "Is it a primary key?", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean primaryKey;

    @Schema(description = "sort", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer ordinalPosition;

    @Schema(description = "Java property types", requiredMode = Schema.RequiredMode.REQUIRED, example = "userAge")
    private String javaType;

    @Schema(description = "Java attribute name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Integer")
    private String javaField;

    @Schema(description = "dict type", example = "sys_gender")
    private String dictType;

    @Schema(description = "Data example", example = "1024")
    private String example;

    @Schema(description = "Whether to create fields for the Create operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean createOperation;

    @Schema(description = "Whether to update the fields for Update operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean updateOperation;

    @Schema(description = "Whether it is a field for List query operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean listOperation;

    @Schema(description = "List condition type for query operation, see CodegenColumnListConditionEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "LIKE")
    private String listOperationCondition;

    @Schema(description = "Whether it is the return field of List query operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean listOperationResult;

    @Schema(description = "display type", requiredMode = Schema.RequiredMode.REQUIRED, example = "input")
    private String htmlType;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
