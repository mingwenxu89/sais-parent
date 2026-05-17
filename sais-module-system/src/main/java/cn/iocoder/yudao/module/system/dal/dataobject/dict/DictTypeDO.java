package cn.iocoder.yudao.module.system.dal.dataobject.dict;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Dict type table
 *
 * @author ruoyi
 */
@TableName("system_dict_type")
@KeySequence("system_dict_type_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class DictTypeDO extends BaseDO {

    /**
     * dict primary key
     */
    @TableId
    private Long id;
    /**
     * Dict name
     */
    private String name;
    /**
     * Dict Type
     */
    private String type;
    /**
     * Status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Remark
     */
    private String remark;

    /**
     * Delete Time
     */
    private LocalDateTime deletedTime;

}
