package cn.iocoder.yudao.module.system.controller.admin.permission.vo.role;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management backend - Role paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePageReqVO extends PageParam {

    @Schema(description = "Role name, fuzzy matching", example = "taro road")
    private String name;

    @Schema(description = "Role identification, fuzzy matching", example = "yudao")
    private String code;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

    @Schema(description = "Create Time", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
