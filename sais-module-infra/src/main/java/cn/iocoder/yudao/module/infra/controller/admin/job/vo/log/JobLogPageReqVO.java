package cn.iocoder.yudao.module.infra.controller.admin.job.vo.log;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - scheduled task log paging Request VO")
@Data
public class JobLogPageReqVO extends PageParam {

    @Schema(description = "Task ID", example = "10")
    private Long jobId;

    @Schema(description = "Processor name, fuzzy matching")
    private String handlerName;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "Start execution time")
    private LocalDateTime beginTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "end execution time")
    private LocalDateTime endTime;

    @Schema(description = "Task status, see JobLogStatusEnum enumeration")
    private Integer status;

}
