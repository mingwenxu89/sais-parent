package cn.iocoder.yudao.module.infra.controller.admin.job.vo.job;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management background - scheduled task paging Request VO")
@Data
public class JobPageReqVO extends PageParam {

    @Schema(description = "Task name, fuzzy matching", example = "Test tasks")
    private String name;

    @Schema(description = "Task status, see JobStatusEnum enumeration", example = "1")
    private Integer status;

    @Schema(description = "Processor name, fuzzy matching", example = "sysUserSessionTimeoutJob")
    private String handlerName;

}
