package cn.iocoder.yudao.module.system.dal.dataobject.notify;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.List;

/**
 * Site letter template DO
 *
 * @author xrcoder
 */
@TableName(value = "system_notify_template", autoResultMap = true)
@KeySequence("system_notify_template_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class NotifyTemplateDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * Template name
     */
    private String name;
    /**
     * Template coding
     */
    private String code;
    /**
     * template type
     *
     * Corresponds to system_notify_template_type dict
     */
    private Integer type;
    /**
     * Sender name
     */
    private String nickname;
    /**
     * Template content
     */
    private String content;
    /**
     * parameter array
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
