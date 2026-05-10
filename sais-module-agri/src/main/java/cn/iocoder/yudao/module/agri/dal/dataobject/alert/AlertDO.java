package cn.iocoder.yudao.module.agri.dal.dataobject.alert;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Alert DO
 *
 * alert_type: 1=SENSOR_ABNORMAL  2=EXTREME_WEATHER  3=IRRIGATION_ABNORMAL
 * level:      1=INFO  2=WARN  3=CRITICAL
 * status:     0=UNHANDLED  1=HANDLING  2=RESOLVED  3=IGNORED
 */
@TableName("sais_alert")
@KeySequence("sais_alert_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AlertDO extends TenantBaseDO {

    @TableId
    private Long id;
    private Long irrigationPlanId;
    private Long farmId;
    private Long fieldId;
    /** Alert type (dict: agri_alert_type) */
    private Integer alertType;
    /** Alert level (dict: agri_alert_level) */
    private Integer level;
    /** Alert description */
    private String context;
    /** Status (dict: agri_alert_status) */
    private Integer status;
    private LocalDateTime triggeredAt;
    private LocalDateTime handledAt;

}
