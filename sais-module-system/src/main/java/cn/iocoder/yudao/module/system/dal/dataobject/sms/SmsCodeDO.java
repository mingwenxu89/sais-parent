package cn.iocoder.yudao.module.system.dal.dataobject.sms;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Mobile phone captcha DO
 *
 * idx_mobile index: based on {@link #mobile} field
 *
 * @author Yudao Source Code
 */
@TableName("system_sms_code")
@KeySequence("system_sms_code_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class SmsCodeDO extends BaseDO {

    /**
     * ID
     */
    private Long id;
    /**
     * Mobile phone ID
     */
    private String mobile;
    /**
     * Captcha
     */
    private String code;
    /**
     * Send scene
     *
     * Enumeration {@link SmsCodeDO}
     */
    private Integer scene;
    /**
     * Create IP
     */
    private String createIp;
    /**
     * Which message was sent today?
     */
    private Integer todayIndex;
    /**
     * Whether to use
     */
    private Boolean used;
    /**
     * usage time
     */
    private LocalDateTime usedTime;
    /**
     * Use IP
     */
    private String usedIp;

}
