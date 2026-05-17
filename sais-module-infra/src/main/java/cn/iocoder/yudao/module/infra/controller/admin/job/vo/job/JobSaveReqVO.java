package cn.iocoder.yudao.module.infra.controller.admin.job.vo.job;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Management background - scheduled task creation/modification Request VO")
@Data
public class JobSaveReqVO {

    @Schema(description = "Task ID", example = "1024")
    private Long id;

    @Schema(description = "Task name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Test tasks")
    @NotEmpty(message = "Task name cannot be empty")
    private String name;

    @Schema(description = "processor name", requiredMode = Schema.RequiredMode.REQUIRED, example = "sysUserSessionTimeoutJob")
    @NotEmpty(message = "Processor name cannot be empty")
    private String handlerName;

    @Schema(description = "Processor parameters", example = "yudao")
    private String handlerParam;

    @Schema(description = "CRON expression", requiredMode = Schema.RequiredMode.REQUIRED, example = "0/10 * * * * ? *")
    @NotEmpty(message = "CRON expression cannot be empty")
    private String cronExpression;

    @Schema(description = "ID of retries", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "The ID of retries cannot be empty")
    private Integer retryCount;

    @Schema(description = "Retry interval", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @NotNull(message = "Retry interval cannot be empty")
    private Integer retryInterval;

    @Schema(description = "Monitoring timeout", example = "1000")
    private Integer monitorTimeout;

}
