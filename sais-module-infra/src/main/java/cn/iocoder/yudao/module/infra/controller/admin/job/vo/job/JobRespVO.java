package cn.iocoder.yudao.module.infra.controller.admin.job.vo.job;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Management background - scheduled tasks Response VO")
@Data
@ExcelIgnoreUnannotated
public class JobRespVO {

    @Schema(description = "Task ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Task ID")
    private Long id;

    @Schema(description = "Task name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Test tasks")
    @ExcelProperty("Task name")
    private String name;

    @Schema(description = "Task status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Task status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.JOB_STATUS)
    private Integer status;

    @Schema(description = "processor name", requiredMode = Schema.RequiredMode.REQUIRED, example = "sysUserSessionTimeoutJob")
    @ExcelProperty("processor name")
    private String handlerName;

    @Schema(description = "Processor parameters", example = "yudao")
    @ExcelProperty("Processor parameters")
    private String handlerParam;

    @Schema(description = "CRON expression", requiredMode = Schema.RequiredMode.REQUIRED, example = "0/10 * * * * ? *")
    @ExcelProperty("CRON expression")
    private String cronExpression;

    @Schema(description = "ID of retries", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "The ID of retries cannot be empty")
    private Integer retryCount;

    @Schema(description = "Retry interval", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private Integer retryInterval;

    @Schema(description = "Monitoring timeout", example = "1000")
    @ExcelProperty("Monitoring timeout")
    private Integer monitorTimeout;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
