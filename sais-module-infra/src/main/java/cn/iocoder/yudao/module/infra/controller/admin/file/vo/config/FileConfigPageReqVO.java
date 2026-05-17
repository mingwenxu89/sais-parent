package cn.iocoder.yudao.module.infra.controller.admin.file.vo.config;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - file configuration paging Request VO")
@Data
public class FileConfigPageReqVO extends PageParam {

    @Schema(description = "Configuration name", example = "S3 - Alibaba Cloud")
    private String name;

    @Schema(description = "memory", example = "1")
    private Integer storage;

    @Schema(description = "Create Time", example = "[2022-07-01 00:00:00, 2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}