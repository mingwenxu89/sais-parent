package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Management backend - Send mobile phone captcha Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSmsSendReqVO extends CaptchaVerificationReqVO {

    @Schema(description = "Mobile phone ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudaoyuanma")
    @NotEmpty(message = "Mobile phone ID cannot be empty")
    @Mobile
    private String mobile;

    @Schema(description = "SMS scenario", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "The sending scene cannot be empty")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;

}
