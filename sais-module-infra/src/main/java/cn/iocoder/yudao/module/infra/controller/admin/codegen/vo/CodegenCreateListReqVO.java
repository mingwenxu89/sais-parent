package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "Management background - Based on the table structure of the database, create table and field definitions for the code generator Request VO")
@Data
public class CodegenCreateListReqVO {

    @Schema(description = "Data source configuration ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "The ID of data source configuration cannot be empty")
    private Long dataSourceConfigId;

    @Schema(description = "table name array", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1, 2, 3]")
    @NotNull(message = "Table name array cannot be empty")
    private List<String> tableNames;

}
