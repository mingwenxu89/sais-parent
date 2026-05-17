package cn.iocoder.yudao.module.system.dal.dataobject.social;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Social (three-party) users
 *
 * @author weir
 */
@TableName(value = "system_social_user", autoResultMap = true)
@KeySequence("system_social_user_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserDO extends BaseDO {

    /**
     * auto-increment primary key
     */
    @TableId
    private Long id;
    /**
     * Types of social platforms
     *
     * Enumeration {@link SocialTypeEnum}
     */
    private Integer type;

    /**
     * social openid
     */
    private String openid;
    /**
     * social token
     */
    private String token;
    /**
     * Original Token data, usually in JSON format
     */
    private String rawTokenInfo;

    /**
     * User nickname
     */
    private String nickname;
    /**
     * User avatar
     */
    private String avatar;
    /**
     * Raw user data, usually in JSON format
     */
    private String rawUserInfo;

    /**
     * Last authentication code
     */
    private String code;
    /**
     * The last authentication state
     */
    private String state;

}


