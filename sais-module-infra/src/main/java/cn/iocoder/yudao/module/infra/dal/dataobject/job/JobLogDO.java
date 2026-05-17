package cn.iocoder.yudao.module.infra.dal.dataobject.job;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.enums.job.JobLogStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Execution log of scheduled tasks
 *
 * @author Yudao Source Code
 */
@TableName("infra_job_log")
@KeySequence("infra_job_log_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class JobLogDO extends BaseDO {

    /**
     * Log ID
     */
    private Long id;
    /**
     * Task ID
     *
     * Association {@link JobDO#getId()}
     */
    private Long jobId;
    /**
     * processor name
     *
     * Redundant fields {@link JobDO#getHandlerName()}
     */
    private String handlerName;
    /**
     * Processor parameters
     *
     * Redundant fields {@link JobDO#getHandlerParam()}
     */
    private String handlerParam;
    /**
     * How many times to execute
     *
     * Used to distinguish whether to retry execution. If the execution is retried, the index is greater than 1
     */
    private Integer executeIndex;

    /**
     * Start execution time
     */
    private LocalDateTime beginTime;
    /**
     * end execution time
     */
    private LocalDateTime endTime;
    /**
     * Execution time, unit: milliseconds
     */
    private Integer duration;
    /**
     * Status
     *
     * Enumeration {@link JobLogStatusEnum}
     */
    private Integer status;
    /**
     * Result data
     *
     * On success, use the result of {@link JobHandler#execute(String)}
     * On failure, use the exception stack of {@link JobHandler#execute(String)}
     */
    private String result;

}
