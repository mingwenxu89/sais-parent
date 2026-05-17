package cn.iocoder.yudao.module.infra.controller.admin.job.vo.log;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.infra.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Management background - scheduled task log Response VO")
@Data
@ExcelIgnoreUnannotated
public class JobLogRespVO {

    @Schema(description = "Log ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Log ID")
    private Long id;

    @Schema(description = "Task ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Task ID")
    private Long jobId;

    @Schema(description = "processor name", requiredMode = Schema.RequiredMode.REQUIRED, example = "sysUserSessionTimeoutJob")
    @ExcelProperty("processor name")
    private String handlerName;

    @Schema(description = "Processor parameters", example = "yudao")
    @ExcelProperty("Processor parameters")
    private String handlerParam;

    @Schema(description = "How many times to execute", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("How many times to execute")
    private Integer executeIndex;

    @Schema(description = "Start execution time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Start execution time")
    private LocalDateTime beginTime;

    @Schema(description = "end execution time")
    @ExcelProperty("end execution time")
    private LocalDateTime endTime;

    @Schema(description = "Execution time", example = "123")
    @ExcelProperty("Execution time")
    private Integer duration;

    @Schema(description = "Task status, see JobLogStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Task status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.JOB_LOG_STATUS)
    private Integer status;

    @Schema(description = "Result data", example = "Executed successfully")
    @ExcelProperty("Result data")
    private String result;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
