package cn.iocoder.yudao.module.infra.dal.dataobject.db;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.EncryptTypeHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Data source configuration
 *
 * @author Yudao Source Code
 */
@TableName(value = "infra_data_source_config", autoResultMap = true)
@KeySequence("infra_data_source_config_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@TenantIgnore
public class DataSourceConfigDO extends BaseDO {

    /**
     * Primary key ID - Master data source
     */
    public static final Long ID_MASTER = 0L;

    /**
     * primary key ID
     */
    private Long id;
    /**
     * connection name
     */
    private String name;

    /**
     * Data source connection
     */
    private String url;
    /**
     * Username
     */
    private String username;
    /**
     * Password
     */
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String password;

}
