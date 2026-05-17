package cn.iocoder.yudao.module.infra.controller.admin.logger;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apierrorlog.ApiErrorLogRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiErrorLogDO;
import cn.iocoder.yudao.module.infra.service.logger.ApiErrorLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "Admin Backend - API Error Log")
@RestController
@RequestMapping("/infra/api-error-log")
@Validated
public class ApiErrorLogController {

    @Resource
    private ApiErrorLogService apiErrorLogService;

    @PutMapping("/update-status")
    @Operation(summary = "Update the status of the API error log")
    @Parameters({
            @Parameter(name = "id", description = "ID", required = true, example = "1024"),
            @Parameter(name = "processStatus", description = "Processing status", required = true, example = "1")
    })
    @PreAuthorize("@ss.hasPermission('infra:api-error-log:update-status')")
    public CommonResult<Boolean> updateApiErrorLogProcess(@RequestParam("id") Long id,
                                                          @RequestParam("processStatus") Integer processStatus) {
        apiErrorLogService.updateApiErrorLogProcess(id, processStatus, getLoginUserId());
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get API error log")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:api-error-log:query')")
    public CommonResult<ApiErrorLogRespVO> getApiErrorLog(@RequestParam("id") Long id) {
        ApiErrorLogDO apiErrorLog = apiErrorLogService.getApiErrorLog(id);
        return success(BeanUtils.toBean(apiErrorLog, ApiErrorLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "Get API error log pagination")
    @PreAuthorize("@ss.hasPermission('infra:api-error-log:query')")
    public CommonResult<PageResult<ApiErrorLogRespVO>> getApiErrorLogPage(@Valid ApiErrorLogPageReqVO pageReqVO) {
        PageResult<ApiErrorLogDO> pageResult = apiErrorLogService.getApiErrorLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ApiErrorLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "Export API error log to Excel")
    @PreAuthorize("@ss.hasPermission('infra:api-error-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportApiErrorLogExcel(@Valid ApiErrorLogPageReqVO exportReqVO,
                                       HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ApiErrorLogDO> list = apiErrorLogService.getApiErrorLogPage(exportReqVO).getList();
        // Export to Excel
        ExcelUtils.write(response, "API error log.xls", "Data", ApiErrorLogRespVO.class,
                BeanUtils.toBean(list, ApiErrorLogRespVO.class));
    }

}
