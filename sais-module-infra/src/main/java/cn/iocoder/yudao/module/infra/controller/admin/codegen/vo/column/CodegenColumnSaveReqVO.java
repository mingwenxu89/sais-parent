package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.column;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Management background - code generation field definition creation/modification Request VO")
@Data
public class CodegenColumnSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "table ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Table ID cannot be empty")
    private Long tableId;

    @Schema(description = "Field name", requiredMode = Schema.RequiredMode.REQUIRED, example = "user_age")
    @NotNull(message = "Field name cannot be empty")
    private String columnName;

    @Schema(description = "Field type", requiredMode = Schema.RequiredMode.REQUIRED, example = "int(11)")
    @NotNull(message = "Field type cannot be empty")
    private String dataType;

    @Schema(description = "Field description", requiredMode = Schema.RequiredMode.REQUIRED, example = "age")
    @NotNull(message = "Field description cannot be empty")
    private String columnComment;

    @Schema(description = "Whether to allow empty", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether it is allowed to be empty or not. It cannot be empty.")
    private Boolean nullable;

    @Schema(description = "Is it a primary key?", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "Whether the primary key cannot be empty")
    private Boolean primaryKey;

    @Schema(description = "sort", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "Sorting cannot be empty")
    private Integer ordinalPosition;

    @Schema(description = "Java property types", requiredMode = Schema.RequiredMode.REQUIRED, example = "userAge")
    @NotNull(message = "Java property type cannot be null")
    private String javaType;

    @Schema(description = "Java attribute name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Integer")
    @NotNull(message = "Java property name cannot be empty")
    private String javaField;

    @Schema(description = "dict type", example = "sys_gender")
    private String dictType;

    @Schema(description = "Data example", example = "1024")
    private String example;

    @Schema(description = "Whether to create fields for the Create operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether the field for the Create creation operation cannot be empty")
    private Boolean createOperation;

    @Schema(description = "Whether to update the fields for Update operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "Whether the field for Update update operation cannot be empty")
    private Boolean updateOperation;

    @Schema(description = "Whether it is a field for List query operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether it is a field for List query operation cannot be empty.")
    private Boolean listOperation;

    @Schema(description = "List condition type for query operation, see CodegenColumnListConditionEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "LIKE")
    @NotNull(message = "The condition type of List query operation cannot be empty")
    private String listOperationCondition;

    @Schema(description = "Whether it is the return field of List query operation", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "Whether it is a List query operation, the return field cannot be empty.")
    private Boolean listOperationResult;

    @Schema(description = "display type", requiredMode = Schema.RequiredMode.REQUIRED, example = "input")
    @NotNull(message = "Display type cannot be empty")
    private String htmlType;

}
