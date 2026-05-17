package cn.iocoder.yudao.module.system.controller.admin.mail.vo.template;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management backend - Email template paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MailTemplatePageReqVO extends PageParam {

    @Schema(description = "Status, see CommonStatusEnum enumeration", example = "1")
    private Integer status;

    @Schema(description = "identification, fuzzy matching", example = "code_1024")
    private String code;

    @Schema(description = "name, fuzzy match", example = "taro")
    private String name;

    @Schema(description = "Account ID", example = "2048")
    private Long accountId;

    @Schema(description = "Create Time")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
