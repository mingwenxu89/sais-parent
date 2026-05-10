package cn.iocoder.yudao.module.agri.dal.dataobject.sensordata;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("sais_sensor_data")
@KeySequence("sais_sensor_data_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SensorDataDO extends TenantBaseDO {

    @TableId
    private Long id;

    private Long sensorId;

    private Long farmId;

    private Long fieldId;

    /** SOIL_MOISTURE / HUMIDITY / TEMPERATURE */
    private String dataType;

    private BigDecimal value;

    private LocalDateTime collectedAt;

}
