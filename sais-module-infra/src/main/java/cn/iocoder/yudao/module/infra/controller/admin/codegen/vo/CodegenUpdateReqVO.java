package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo;

import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.column.CodegenColumnSaveReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.CodegenTableSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "Management background - modification of code generation tables and fields Request VO")
@Data
public class CodegenUpdateReqVO {

    @Valid // Validate embedded fields
    @NotNull(message = "Table definition cannot be empty")
    private CodegenTableSaveReqVO table;

    @Valid // Validate embedded fields
    @NotNull(message = "Field definition cannot be empty")
    private List<CodegenColumnSaveReqVO> columns;

}
