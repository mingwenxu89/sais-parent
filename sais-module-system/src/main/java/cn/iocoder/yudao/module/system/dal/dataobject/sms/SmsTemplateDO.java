package cn.iocoder.yudao.module.system.dal.dataobject.sms;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.enums.sms.SmsTemplateTypeEnum;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * SMS Template DO
 *
 * @author zzf
 * @since 2021-01-25
 */
@TableName(value = "system_sms_template", autoResultMap = true)
@KeySequence("system_sms_template_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class SmsTemplateDO extends BaseDO {

    /**
     * self-increasing ID
     */
    private Long id;

    // ========= Template related fields =========

    /**
     * SMS type
     *
     * Enum {@link SmsTemplateTypeEnum}
     */
    private Integer type;
    /**
     * Enabled status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Template encoding, guaranteed to be unique
     */
    private String code;
    /**
     * Template name
     */
    private String name;
    /**
     * Template content
     *
     * Content parameters, use {} to include, for example {name}
     */
    private String content;
    /**
     * Parameter array (automatically generated based on content)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> params;
    /**
     * Remark
     */
    private String remark;
    /**
     * SMS API template ID
     */
    private String apiTemplateId;

    // ========= Channel related fields =========

    /**
     * SMS channel ID
     *
     * Association {@link SmsChannelDO#getId()}
     */
    private Long channelId;
    /**
     * SMS channel code
     *
     * Redundant {@link SmsChannelDO#getCode()}
     */
    private String channelCode;

}
