package cn.iocoder.yudao.module.agri.dal.dataobject.crop;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("sais_crop_plan")
@KeySequence("sais_crop_plan_id_seq")
public class CropPlanDO extends TenantBaseDO {

    @TableId
    private Long id;

    private Long cropId;

    private Long fieldId;

    private Integer growStatus;

    private LocalDate startDate;

    private LocalDate endDate;

}
