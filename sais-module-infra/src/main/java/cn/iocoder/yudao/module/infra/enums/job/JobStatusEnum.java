package cn.iocoder.yudao.module.infra.enums.job;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.Collections;
import java.util.Set;

/**
 * Enumeration of task status
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnum {

    /**
     * Initializing
     */
    INIT(0, Collections.emptySet()),
    /**
     * turn on
     */
    NORMAL(1, Sets.newHashSet(Constants.STATE_WAITING, Constants.STATE_ACQUIRED, Constants.STATE_BLOCKED)),
    /**
     * pause
     */
    STOP(2, Sets.newHashSet(Constants.STATE_PAUSED, Constants.STATE_PAUSED_BLOCKED));

    /**
     * Status
     */
    private final Integer status;
    /**
     * The corresponding state collection of Quartz triggers
     */
    private final Set<String> quartzStates;

}
