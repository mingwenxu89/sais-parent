package cn.iocoder.yudao.module.infra.enums.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Task log status enumeration
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum JobLogStatusEnum {

    RUNNING(0), // Running
    SUCCESS(1), // Success
    FAILURE(2); // Failure

    /**
     * Status
     */
    private final Integer status;

}
