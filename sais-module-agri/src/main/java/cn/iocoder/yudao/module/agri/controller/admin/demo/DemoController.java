package cn.iocoder.yudao.module.agri.controller.admin.demo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.agri.job.IrrigationDecisionJob;
import cn.iocoder.yudao.module.agri.job.IrrigationPlanExecutionJob;
import cn.iocoder.yudao.module.agri.service.alert.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin - Demo Task Trigger")
@RestController
@RequestMapping("/agri/demo")
@Validated
@Slf4j
public class DemoController {

    @Resource
    private IrrigationDecisionJob irrigationDecisionJob;

    @Resource
    private IrrigationPlanExecutionJob irrigationPlanExecutionJob;

    @Resource
    private AlertService alertService;

    @PostMapping("/trigger-ai-irrigation")
    @Operation(summary = "Manually trigger AI irrigation decision")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<String> triggerAiIrrigation() {
        log.info("[Demo] Manually triggering IrrigationDecisionJob");
        irrigationDecisionJob.runDecision();
        return success("AI irrigation decision triggered successfully.");
    }

    @PostMapping("/trigger-irrigation-execution")
    @Operation(summary = "Manually trigger irrigation plan execution check")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<String> triggerIrrigationExecution() {
        log.info("[Demo] Manually triggering IrrigationPlanExecutionJob");
        irrigationPlanExecutionJob.executePlans();
        return success("Irrigation plan execution check triggered successfully.");
    }

    @PostMapping("/trigger-test-alert")
    @Operation(summary = "Trigger test alert")
    @PreAuthorize("@ss.hasPermission('agri:alert:create')")
    public CommonResult<String> triggerTestAlert(
            @RequestParam String type,
            @RequestParam(required = false) Long farmId) {
        log.info("[Demo] Triggering test alert type={} farmId={}", type, farmId);
        alertService.triggerTestAlert(type, farmId);
        return success("Test alert '" + type + "' triggered successfully.");
    }
}
