package cn.iocoder.yudao.module.system.controller.admin.logger.vo.operatelog;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - operation log paging list Request VO")
@Data
public class OperateLogPageReqVO extends PageParam {

    @Schema(description = "User ID", example = "taro road")
    private Long userId;

    @Schema(description = "Operation module business ID", example = "1")
    private Long bizId;

    @Schema(description = "Operation module, simulation matching", example = "Order")
    private String type;

    @Schema(description = "Operation name, simulated matching", example = "Create order")
    private String subType;

    @Schema(description = "Operation details, simulation matching", example = "Modify user information IDed 1")
    private String action;

    @Schema(description = "start time", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
