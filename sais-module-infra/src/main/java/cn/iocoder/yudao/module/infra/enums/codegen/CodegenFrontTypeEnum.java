package cn.iocoder.yudao.module.infra.enums.codegen;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Code generated frontend type enum
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum CodegenFrontTypeEnum {

    VUE2_ELEMENT_UI(10), // Vue2 Element UI Standard template

    VUE3_ELEMENT_PLUS(20), // Vue3 Element Plus Standard template

    VUE3_VBEN2_ANTD_SCHEMA(30), // Vue3 VBEN2 + ANTD + Schema Template

    VUE3_VBEN5_ANTD_SCHEMA(40), // Vue3 VBEN5 + ANTD + schema Template
    VUE3_VBEN5_ANTD_GENERAL(41), // Vue3 VBEN5 + ANTD Standard template

    VUE3_VBEN5_EP_SCHEMA(50), // Vue3 VBEN5 + EP + schema Template
    VUE3_VBEN5_EP_GENERAL(51), // Vue3 VBEN5 + EP Standard template

    VUE3_ADMIN_UNIAPP_WOT(60), // Vue3 Admin + Uniapp + WOT Standard template
    ;

    /**
     * Type
     */
    private final Integer type;

}
