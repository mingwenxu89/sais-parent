package cn.iocoder.yudao.module.infra.controller.admin.job;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.log.JobLogPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.job.vo.log.JobLogRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.job.JobLogDO;
import cn.iocoder.yudao.module.infra.service.job.JobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Management background - scheduled task log")
@RestController
@RequestMapping("/infra/job-log")
@Validated
public class JobLogController {

    @Resource
    private JobLogService jobLogService;

    @GetMapping("/get")
    @Operation(summary = "Get scheduled task log")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:job:query')")
    public CommonResult<JobLogRespVO> getJobLog(@RequestParam("id") Long id) {
        JobLogDO jobLog = jobLogService.getJobLog(id);
        return success(BeanUtils.toBean(jobLog, JobLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "Get scheduled task log pagination")
    @PreAuthorize("@ss.hasPermission('infra:job:query')")
    public CommonResult<PageResult<JobLogRespVO>> getJobLogPage(@Valid JobLogPageReqVO pageVO) {
        PageResult<JobLogDO> pageResult = jobLogService.getJobLogPage(pageVO);
        return success(BeanUtils.toBean(pageResult, JobLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export scheduled task log to Excel")
    @PreAuthorize("@ss.hasPermission('infra:job:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportJobLogExcel(@Valid JobLogPageReqVO exportReqVO,
                                  HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<JobLogDO> list = jobLogService.getJobLogPage(exportReqVO).getList();
        // Export to Excel
        ExcelUtils.write(response, "Task log.xls", "Data", JobLogRespVO.class,
                BeanUtils.toBean(list, JobLogRespVO.class));
    }

}