package cn.iocoder.yudao.module.agri.dal.dataobject.farm;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Farm DO
 */
@TableName("sais_farm")
@KeySequence("sais_farm_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FarmDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** Farm name */
    private String farmName;
    /** Longitude */
    private BigDecimal longitude;
    /** Latitude */
    private BigDecimal latitude;
    /** Address */
    private String address;

}
