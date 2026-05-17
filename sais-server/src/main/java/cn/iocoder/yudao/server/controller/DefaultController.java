package cn.iocoder.yudao.server.controller;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_IMPLEMENTED;

/**
 * Default Controller that provides 404-style prompts when some modules are not enabled.
 * For example, the /bpm/** path belongs to workflow.
 *
 * @author Yudao Source Code
 */
@RestController
@Slf4j
public class DefaultController {

    @RequestMapping("/admin-api/bpm/**")
    public CommonResult<Boolean> bpm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[Workflow module sar-module-bpm - disabled][See https://doc.iocoder.cn/bpm/open]");
    }

    @RequestMapping("/admin-api/mp/**")
    public CommonResult<Boolean> mp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[WeChat official account sar-module-mp - disabled][See https://doc.iocoder.cn/mp/build/open]");
    }

    @RequestMapping(value = { "/admin-api/product/**", // Product center
            "/admin-api/trade/**", // Trade center
            "/admin-api/promotion/**" }) // Promotion center
    public CommonResult<Boolean> mall404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[Mall system sar-module-mall - disabled][See https://doc.iocoder.cn/mall/build/open]");
    }

    @RequestMapping("/admin-api/erp/**")
    public CommonResult<Boolean> erp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[ERP module sar-module-erp - disabled][See https://doc.iocoder.cn/erp/build/open]");
    }

    @RequestMapping("/admin-api/crm/**")
    public CommonResult<Boolean> crm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[CRM module sar-module-crm - disabled][See https://doc.iocoder.cn/crm/build/open]");
    }

    @RequestMapping(value = { "/admin-api/report/**"})
    public CommonResult<Boolean> report404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[Report module sar-module-report - disabled][See https://doc.iocoder.cn/report/open]");
    }

    @RequestMapping(value = { "/admin-api/pay/**"})
    public CommonResult<Boolean> pay404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[Payment module sar-module-pay - disabled][See https://doc.iocoder.cn/pay/build/open]");
    }

    @RequestMapping(value = { "/admin-api/ai/**"})
    public CommonResult<Boolean> ai404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[AI large model sar-module-ai - disabled][See https://doc.iocoder.cn/ai/build/open]");
    }

    @RequestMapping(value = { "/admin-api/iot/**"})
    public CommonResult<Boolean> iot404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[IoT module sar-module-iot - disabled][See https://doc.iocoder.cn/iot/build/open]");
    }

    /**
     * Test API: print query, header, and body.
     */
    @RequestMapping(value = { "/test" })
    @PermitAll
    public CommonResult<Boolean> test(HttpServletRequest request) {
        // Print query parameters
        log.info("Query: {}", ServletUtils.getParamMap(request));
        // Print request headers
        log.info("Header: {}", ServletUtils.getHeaderMap(request));
        // Print request body
        log.info("Body: {}", ServletUtils.getBody(request));
        return CommonResult.success(true);
    }

}
