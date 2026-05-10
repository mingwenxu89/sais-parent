package cn.iocoder.yudao.module.agri.dal.dataobject.irrigation;

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
 * Irrigation Plan DO
 *
 * Status flow: PENDING → EXECUTING → COMPLETED / CANCELLED
 */
@TableName("sais_irrigation_plan")
@KeySequence("sais_irrigation_plan_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IrrigationPlanDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** Farm ID */
    private Long farmId;
    /** Field ID */
    private Long fieldId;
    /** Device ID */
    private Long deviceId;
    /** Crop plan ID */
    private Long cropPlanId;
    /** Decision source: MANUAL / AI */
    private String decisionSource;
    /** Decision reason / AI rationale */
    private String decisionReason;
    /** Planned execution time */
    private LocalDateTime plannedStartTime;
    /** Planned irrigation duration (minutes) */
    private Integer plannedDuration;
    /** Status: PENDING / EXECUTING / COMPLETED / CANCELLED */
    private String status;
    /** Actual start time */
    private LocalDateTime actualStartTime;
    /** Actual end time */
    private LocalDateTime actualEndTime;
    /** Water quantity (litres), computed after execution completes */
    private BigDecimal waterQuantity;
    /** Device ACK arrival time; NULL means device confirmation not yet received */
    private LocalDateTime ackReceivedAt;

}
