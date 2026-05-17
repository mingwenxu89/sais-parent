package cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - SMS channel streamlining Response VO")
@Data
public class SmsChannelSimpleRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "SMS signature", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yudao Source Code")
    private String signature;

    @Schema(description = "Channel encoding, see SmsChannelEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "YUN_PIAN")
    private String code;

}
