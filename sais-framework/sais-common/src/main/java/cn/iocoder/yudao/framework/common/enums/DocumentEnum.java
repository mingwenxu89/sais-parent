package cn.iocoder.yudao.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Document address
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum DocumentEnum {

    REDIS_INSTALL("https:// gitee.com/zhijiantianya/ruoyi-vue-pro/issues/I4VCSJ", "Redis Installation Document"),
    TENANT("https:// doc.iocoder.cn", "SaaS Multi-tenant Documentation");

 private final String url;
 private final String memo;

}
