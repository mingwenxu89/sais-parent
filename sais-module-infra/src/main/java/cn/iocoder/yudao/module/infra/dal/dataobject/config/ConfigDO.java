package cn.iocoder.yudao.module.infra.dal.dataobject.config;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.enums.config.ConfigTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Parameter configuration table
 *
 * @author Yudao Source Code
 */
@TableName("infra_config")
@KeySequence("infra_config_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class ConfigDO extends BaseDO {

    /**
     * Parameter primary key
     */
    @TableId
    private Long id;
    /**
     * Parameter classification
     */
    private String category;
    /**
     * Parameter name
     */
    private String name;
    /**
     * Parameter key name
     *
     * When supporting multiple DB types, key + @TableField("config_key") cannot be used directly to achieve conversion. The reason is that "config_key" AS key causes an error.
     */
    private String configKey;
    /**
     * Parameter key value
     */
    private String value;
    /**
     * Parameter type
     *
     * Enum {@link ConfigTypeEnum}
     */
    private Integer type;
    /**
     * visible or not
     *
     * Invisible parameters are generally sensitive parameters and cannot be obtained by the front end.
     */
    private Boolean visible;
    /**
     * Remark
     */
    private String remark;

}
