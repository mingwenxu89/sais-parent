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
 * Crop DO
 */
@TableName("sais_crop")
@KeySequence("sais_crop_id_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class CropDO extends BaseDO {

    @TableId
    private Long id;
    /** Crop name */
    private String cropName;
    /** Crop type 1=Vegetable 2=Grain 3=Fruit 4=Other */
    private Integer cropType;
    /** Variety */
    private String variety;
    /** Growth period (days) */
    private Integer growthPeriod;
    /** Soil pH minimum */
    private java.math.BigDecimal soilPhMin;
    /** Soil pH maximum */
    private java.math.BigDecimal soilPhMax;
    /** Irrigation method 1=Drip 2=Sprinkler 3=Flood 4=Micro-spray */
    private Integer irrigationMethod;
    /** Drought resistance 1=Weak 2=Medium 3=Strong */
    private Integer droughtResistance;
    /** Waterlogging tolerance 1=Weak 2=Medium 3=Strong */
    private Integer waterloggingTolerance;
    /** Crop image URL */
    private String imageUrl;

}
