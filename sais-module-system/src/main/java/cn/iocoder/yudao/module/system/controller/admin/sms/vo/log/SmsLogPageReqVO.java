package cn.iocoder.yudao.module.system.controller.admin.sms.vo.log;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "Management background - SMS log paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SmsLogPageReqVO extends PageParam {

    @Schema(description = "SMS channel ID", example = "10")
    private Long channelId;

    @Schema(description = "Template ID", example = "20")
    private Long templateId;

    @Schema(description = "Mobile phone ID", example = "15601691300")
    private String mobile;

    @Schema(description = "Send status, see SmsSendStatusEnum enumeration class", example = "1")
    private Integer sendStatus;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "Send time")
    private LocalDateTime[] sendTime;

    @Schema(description = "Receive status, see SmsReceiveStatusEnum enumeration class", example = "0")
    private Integer receiveStatus;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(description = "Receiving time")
    private LocalDateTime[] receiveTime;

}
