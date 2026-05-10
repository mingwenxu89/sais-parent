package cn.iocoder.yudao.module.agri.dal.dataobject.sensor;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sais_sensor")
@KeySequence("sais_sensor_id_seq")
public class SensorDO extends TenantBaseDO {

    @TableId
    private Long id;

    private String sensorCode;

    private Integer sensorType;

    private String model;

    private Long farmId;

    private Long fieldId;

    private Boolean isMock;

    private Integer status;

}
