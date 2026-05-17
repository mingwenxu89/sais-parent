package cn.iocoder.yudao.module.infra.enums.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigTypeEnum {

    /**
     * System configuration
     */
    SYSTEM(1),
    /**
     * Custom configuration
     */
    CUSTOM(2);

    private final Integer type;

}
