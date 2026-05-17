package cn.iocoder.yudao.module.system.dal.dataobject.dept;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Job list
 *
 * @author ruoyi
 */
@TableName("system_post")
@KeySequence("system_post_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class PostDO extends BaseDO {

    /**
     * Position serial ID
     */
    @TableId
    private Long id;
    /**
     * Job title
     */
    private String name;
    /**
     * Position code
     */
    private String code;
    /**
     * Position sorting
     */
    private Integer sort;
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

}
