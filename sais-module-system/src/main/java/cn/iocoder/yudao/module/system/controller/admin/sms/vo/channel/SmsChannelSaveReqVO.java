package cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - SMS channel creation/modification Request VO")
@Data
public class SmsChannelSaveReqVO {

    @Schema(description = "ID", example = "1024")
    private Long id;

    @Schema(description = "SMS signature", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yudao Source Code")
    @NotNull(message = "SMS signature cannot be empty")
    private String signature;

    @Schema(description = "Channel encoding, see SmsChannelEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "YUN_PIAN")
    @NotNull(message = "Channel code cannot be empty")
    private String code;

    @Schema(description = "Enabled status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Enabled status cannot be empty")
    private Integer status;

    @Schema(description = "Remark", example = "Delicious!")
    private String remark;

    @Schema(description = "SMS API account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotNull(message = "The SMS API account cannot be empty")
    private String apiKey;

    @Schema(description = "SMS API key", example = "yuanma")
    private String apiSecret;

    @Schema(description = "SMS send callback URL", example = "http://www.iocoder.cn")
    @URL(message = "Callback URL is malformed")
    private String callbackUrl;

}
