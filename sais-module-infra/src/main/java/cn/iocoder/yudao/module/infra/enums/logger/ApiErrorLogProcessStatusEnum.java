package cn.iocoder.yudao.module.infra.enums.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API exception data processing status
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum ApiErrorLogProcessStatusEnum {

    INIT(0, "Unprocessed"),
    DONE(1, "Processed"),
    IGNORE(2, "Ignored");

    /**
     * Status
     */
    private final Integer status;
    /**
     * Resource type name
     */
    private final String name;

}
