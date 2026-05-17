package cn.iocoder.yudao.module.system.controller.admin.logger.vo.loginlog;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - Login log paginated list Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogPageReqVO extends PageParam {

    @Schema(description = "User IP, simulated matching", example = "127.0.0.1")
    private String userIp;

    @Schema(description = "User account, simulated matching", example = "taro road")
    private String username;

    @Schema(description = "operating status", example = "true")
    private Boolean status;

    @Schema(description = "Login time", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
