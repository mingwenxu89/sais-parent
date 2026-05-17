package cn.iocoder.yudao.module.system.dal.dataobject.social;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Social user binding
 * That is, the association table between {@link SocialUserDO} and UserDO
 *
 * @author Yudao Source Code
 */
@TableName(value = "system_social_user_bind", autoResultMap = true)
@KeySequence("system_social_user_bind_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserBindDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * Associated user ID
     *
     * ID associated with UserDO
     */
    private Long userId;
    /**
     * User type
     *
     * Enumeration {@link UserTypeEnum}
     */
    private Integer userType;

    /**
     * Social platform user ID
     *
     * Link {@link SocialUserDO#getId()}
     */
    private Long socialUserId;
    /**
     * Types of social platforms
     *
     * Redundant {@link SocialUserDO#getType()}
     */
    private Integer socialType;

}
