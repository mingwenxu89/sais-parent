package cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apiaccesslog;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - API access log paging Request VO")
@Data
public class ApiAccessLogPageReqVO extends PageParam {

    @Schema(description = "User ID", example = "666")
    private Long userId;

    @Schema(description = "User type", example = "2")
    private Integer userType;

    @Schema(description = "Application name", example = "dashboard")
    private String applicationName;

    @Schema(description = "Request address, fuzzy matching", example = "/xxx/yyy")
    private String requestUrl;

    @Schema(description = "start time", example = "[2022-07-01 00:00:00, 2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] beginTime;

    @Schema(description = "Execution time, greater than or equal to, unit: milliseconds", example = "100")
    private Integer duration;

    @Schema(description = "result code", example = "0")
    private Integer resultCode;

}
