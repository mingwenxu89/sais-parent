package cn.iocoder.yudao.module.system.dal.dataobject.sms;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsChannelEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * SMS channel DO
 *
 * @author zzf
 * @since 2021-01-25
 */
@TableName(value = "system_sms_channel", autoResultMap = true)
@KeySequence("system_sms_channel_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TenantIgnore
public class SmsChannelDO extends BaseDO {

    /**
     * Channel ID
     */
    private Long id;
    /**
     * SMS signature
     */
    private String signature;
    /**
     * Channel code
     *
     * Enum {@link SmsChannelEnum}
     */
    private String code;
    /**
     * Enabled status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * Remark
     */
    private String remark;
    /**
     * SMS API account
     */
    private String apiKey;
    /**
     * SMS API key
     */
    private String apiSecret;
    /**
     * SMS send callback URL
     */
    private String callbackUrl;

}
