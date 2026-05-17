package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Management backend - SMS captcha login Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSmsLoginReqVO {

    @Schema(description = "Mobile phone ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudaoyuanma")
    @NotEmpty(message = "Mobile phone ID cannot be empty")
    @Mobile
    private String mobile;

    @Schema(description = "SMS captcha", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "Captcha cannot be empty")
    private String code;

}
