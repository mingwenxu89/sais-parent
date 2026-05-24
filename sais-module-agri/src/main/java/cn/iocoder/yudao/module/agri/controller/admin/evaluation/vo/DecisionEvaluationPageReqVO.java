package cn.iocoder.yudao.module.agri.controller.admin.evaluation.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Admin - Decision Evaluation Page Request VO")
@Data
public class DecisionEvaluationPageReqVO extends PageParam {

    @Schema(description = "Field ID", example = "1")
    private Long fieldId;

    @Schema(description = "Whether AI decision matched the rule-based decision", example = "true")
    private Boolean aligned;

}
