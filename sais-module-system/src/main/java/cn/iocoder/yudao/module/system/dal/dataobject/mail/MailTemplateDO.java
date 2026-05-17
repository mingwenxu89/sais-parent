package cn.iocoder.yudao.module.system.dal.dataobject.mail;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Email template DO
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
@TableName(value = "system_mail_template", autoResultMap = true)
@KeySequence("system_mail_template_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@TenantIgnore
public class MailTemplateDO extends BaseDO {

    /**
     * primary key
     */
    private Long id;
    /**
     * Template name
     */
    private String name;
    /**
     * Template ID
     */
    private String code;
    /**
     * Email account ID sent
     *
     * Association {@link MailAccountDO#getId()}
     */
    private Long accountId;

    /**
     * Sender name
     */
    private String nickname;
    /**
     * Title
     */
    private String title;
    /**
     * Content
     */
    private String content;
    /**
     * Parameter array (automatically generated based on content)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> params;
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
