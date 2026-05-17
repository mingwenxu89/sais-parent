package cn.iocoder.yudao.module.system.controller.admin.notify.vo.template;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management backend - site letter template pagination Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NotifyTemplatePageReqVO extends PageParam {

    @Schema(description = "Template coding", example = "test_01")
    private String code;

    @Schema(description = "Template name", example = "i am name")
    private String name;

    @Schema(description = "Status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

    @Schema(description = "Create Time")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
