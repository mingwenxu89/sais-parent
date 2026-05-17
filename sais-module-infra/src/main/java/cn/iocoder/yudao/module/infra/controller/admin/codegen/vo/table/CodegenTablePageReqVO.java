package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - table definition paging Request VO")
@Data
public class CodegenTablePageReqVO extends PageParam {

    @Schema(description = "Table name, fuzzy match", example = "yudao")
    private String tableName;

    @Schema(description = "Table description, fuzzy matching", example = "taro road")
    private String tableComment;

    @Schema(description = "Entity, fuzzy matching", example = "Yudao")
    private String className;

    @Schema(description = "Create Time", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
