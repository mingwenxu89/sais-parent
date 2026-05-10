package cn.iocoder.yudao.module.agri.controller.admin.evaluation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.DecisionComparisonVO;
import cn.iocoder.yudao.module.agri.service.irrigation.DecisionComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * Decision evaluation endpoints — shadow-mode comparisons of rule-based vs AI irrigation
 * decisions on real ONGOING fields. Read-only; never creates irrigation plans.
 */
@Tag(name = "Admin - Decision Evaluation")
@RestController
@RequestMapping("/agri/evaluation")
@Validated
@Slf4j
public class DecisionEvaluationController {

    @Resource
    private DecisionComparisonService decisionComparisonService;

    @GetMapping("/decision-comparison")
    @Operation(summary = "AI vs rule-based decision comparison (dry-run, no irrigation plan created)")
    @PreAuthorize("@ss.hasPermission('agri:irrigation-plan:query')")
    public CommonResult<List<DecisionComparisonVO>> compareDecision() {
        log.info("[Evaluation] Running AI vs rule-based decision comparison (dry-run)");
        List<DecisionComparisonVO> result = decisionComparisonService.compareAll();
        return success(result);
    }
}
