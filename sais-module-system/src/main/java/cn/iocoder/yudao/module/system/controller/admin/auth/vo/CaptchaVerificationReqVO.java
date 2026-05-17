package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Management backend - Captcha Request VO")
@Data
public class CaptchaVerificationReqVO {

    // ========== Image captcha related ==========
    @Schema(description = "Captcha, when the captcha is turned on, it needs to be passed", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "PfcH6mgr8tpXuMWFjvW6YVaqrswIuwmWI5dsVZSg7sGpWtDCUbHuDEXl3cFB1+VvCC/rAkSwK8Fad52FSuncVg==")
    @NotEmpty(message = "Captcha cannot be empty", groups = CodeEnableGroup.class)
    private String captchaVerification;

    /**
     * Group that enables captcha
     */
    public interface CodeEnableGroup {
    }
}
