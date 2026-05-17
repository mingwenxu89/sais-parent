package cn.iocoder.yudao.module.system.dal.dataobject.dept;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User and position association
 *
 * @author ruoyi
 */
@TableName("system_user_post")
@KeySequence("system_user_post_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPostDO extends BaseDO {

    /**
     * auto-increment primary key
     */
    @TableId
    private Long id;
    /**
     * User ID
     *
     * Association {@link AdminUserDO#getId()}
     */
    private Long userId;
    /**
     * Role ID
     *
     * Association {@link PostDO#getId()}
     */
    private Long postId;

}
