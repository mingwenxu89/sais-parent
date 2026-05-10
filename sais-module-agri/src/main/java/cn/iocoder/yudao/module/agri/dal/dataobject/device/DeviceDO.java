package cn.iocoder.yudao.module.agri.dal.dataobject.device;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * IoT Device DO
 */
@TableName("sais_device")
@KeySequence("sais_device_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** Device name */
    private String name;
    /** Device code */
    private String deviceCode;
    /** Device type 1=Sensor 2=Irrigation Controller 3=Weather Station */
    private Integer deviceType;
    /** Field ID */
    private Long fieldId;
    /** IP address */
    private String ipAddress;
    /** Status 1=Online 2=Offline 3=Fault */
    private Integer status;
    /** Last online time */
    private LocalDateTime lastOnline;
    /** Description */
    private String description;

}
