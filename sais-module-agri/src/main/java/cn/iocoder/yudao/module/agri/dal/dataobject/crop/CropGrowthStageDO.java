package cn.iocoder.yudao.module.agri.dal.dataobject.crop;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Crop Growth Stage DO
 */
@TableName("sais_growth_stage")
@KeySequence("sais_growth_stage_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class CropGrowthStageDO extends BaseDO {

    @TableId
    private Long id;
    /** Crop ID - foreign key to crop_info */
    private Long cropId;
    /** Stage name */
    private String stageName;
    /** Stage order (1, 2, 3...) */
    private Integer stageOrder;
    /** Stage duration in days */
    private Integer durationDays;
    /** Soil moisture minimum threshold */
    private java.math.BigDecimal soilMoistureMin;
    /** Soil moisture maximum threshold */
    private java.math.BigDecimal soilMoistureMax;
    /** Soil moisture optimal threshold */
    private java.math.BigDecimal soilMoistureOptimal;

}
