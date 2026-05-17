package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management backend - User paging Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPageReqVO extends PageParam {

    @Schema(description = "User account, fuzzy matching", example = "yudao")
    private String username;

    @Schema(description = "Mobile phone ID, fuzzy matching", example = "yudao")
    private String mobile;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

    @Schema(description = "Create Time", example = "[2022-07-01 00:00:00, 2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "Department ID, while filtering sub-departments", example = "1024")
    private Long deptId;

    @Schema(description = "role ID", example = "1024")
    private Long roleId;

}
