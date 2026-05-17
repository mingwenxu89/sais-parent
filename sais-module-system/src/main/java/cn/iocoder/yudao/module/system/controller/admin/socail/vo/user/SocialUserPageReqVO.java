package cn.iocoder.yudao.module.system.controller.admin.socail.vo.user;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management backend - social user paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SocialUserPageReqVO extends PageParam {

    @Schema(description = "Types of social platforms", example = "30")
    private Integer type;

    @Schema(description = "User nickname", example = "John Doe")
    private String nickname;

    @Schema(description = "social openid", example = "oz-Jdt0kd_jdhUxJHQdBJMlOFN7w")
    private String openid;

    @Schema(description = "Create Time")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
