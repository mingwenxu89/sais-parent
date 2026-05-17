package cn.iocoder.yudao.module.system.dal.dataobject.dict;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * dict data table
 *
 * @author ruoyi
 */
@TableName("system_dict_data")
@KeySequence("system_dict_data_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class DictDataDO extends BaseDO {

    /**
     * Dict data ID
     */
    @TableId
    private Long id;
    /**
     * Dict sort
     */
    private Integer sort;
    /**
     * dict tag
     */
    private String label;
    /**
     * Dict value
     */
    private String value;
    /**
     * Dict Type
     *
     * Redundant {@link DictDataDO#getDictType()}
     */
    private String dictType;
    /**
     * Status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * color type
     *
     * Corresponding to element-ui are default, primary, success, info, warning, danger
     */
    private String colorType;
    /**
     * css style
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cssClass;
    /**
     * Remark
     */
    private String remark;

}
