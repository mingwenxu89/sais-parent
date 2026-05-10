package cn.iocoder.yudao.module.agri.dal.dataobject.field;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Field DO
 */
@TableName("sais_field")
@KeySequence("sais_field_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldDO extends TenantBaseDO {

    @TableId
    private Long id;
    /** Farm ID */
    private Long farmId;
    /** Field name */
    private String fieldName;
    /** Area (mu) */
    private BigDecimal area;
    /** Longitude */
    private BigDecimal longitude;
    /** Latitude */
    private BigDecimal latitude;
    /** Growth status UNSTARTED=Not started ONGOING=In progress FINISHED=Completed FALLOW=Fallow */
    private String growStatus;
    /** Boundary */
    private String boundary;

}
