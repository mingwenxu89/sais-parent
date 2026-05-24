package cn.iocoder.yudao.module.agri.dal.dataobject.evaluation;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Persisted dry-run comparison between rule-based and AI irrigation decisions.
 */
@TableName("sais_decision_evaluation")
@KeySequence("sais_decision_evaluation_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DecisionEvaluationRecordDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long fieldId;
    private String fieldName;
    private String cropName;
    private String stageName;
    private BigDecimal currentMoisture;
    private BigDecimal moistureMin;
    private BigDecimal moistureOptimal;
    private BigDecimal tomorrowRainfall;
    private String ruleDecision;
    private String ruleReason;
    private Integer ruleDurationMinutes;
    private String aiDecision;
    private String aiReason;
    private Integer aiDurationMinutes;
    private Boolean aligned;
    private Boolean aiAvailable;
    private LocalDateTime evaluatedAt;

}
