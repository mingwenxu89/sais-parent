package cn.iocoder.yudao.module.infra.dal.dataobject.job;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.enums.job.JobStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Scheduled tasks DO
 *
 * @author Yudao Source Code
 */
@TableName("infra_job")
@KeySequence("infra_job_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class JobDO extends BaseDO {

    /**
     * Task ID
     */
    @TableId
    private Long id;
    /**
     * Task name
     */
    private String name;
    /**
     * Task status
     *
     * Enumeration {@link JobStatusEnum}
     */
    private Integer status;
    /**
     * processor name
     */
    private String handlerName;
    /**
     * Processor parameters
     */
    private String handlerParam;
    /**
     * CRON expression
     */
    private String cronExpression;

    // ========== Retry related fields ==========
    /**
     * ID of retries
     * Set to 0 if not retrying
     */
    private Integer retryCount;
    /**
     * Retry interval, unit: milliseconds
     * If there is no interval, set to 0
     */
    private Integer retryInterval;

    // ========== Monitoring related fields ==========
    /**
     * Monitoring timeout, unit: milliseconds
     * When empty, it means no monitoring
     *
     * Note that the purpose of the timeout here is not to cancel the task, but to warn that the execution time of the task is too long.
     */
    private Integer monitorTimeout;

}
