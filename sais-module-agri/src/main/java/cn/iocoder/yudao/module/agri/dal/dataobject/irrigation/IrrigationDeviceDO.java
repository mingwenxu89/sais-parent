package cn.iocoder.yudao.module.agri.dal.dataobject.irrigation;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sais_irrigation_device")
@KeySequence("sais_irrigation_device_id_seq")
public class IrrigationDeviceDO extends TenantBaseDO {

    @TableId
    private Long id;

    private String deviceCode;

    private Long farmId;

    private Long fieldId;

    private BigDecimal flowRate;

    private Boolean isWatering;

    private Integer status;

    private Long sensorId;
    /** Demo only: when true, MockDeviceAckJob skips sending ACK, triggering an irrigation-fault alert */
    private Boolean simulateFault;

}
