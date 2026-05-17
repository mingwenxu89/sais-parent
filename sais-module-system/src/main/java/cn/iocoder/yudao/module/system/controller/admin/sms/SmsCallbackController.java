package cn.iocoder.yudao.module.system.controller.admin.sms;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.framework.sms.core.enums.SmsChannelEnum;
import cn.iocoder.yudao.module.system.service.sms.SmsSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Management background - SMS callback")
@RestController
@RequestMapping("/system/sms/callback")
public class SmsCallbackController {

    @Resource
    private SmsSendService smsSendService;

    @PostMapping("/aliyun")
    @PermitAll
    @TenantIgnore
    @Operation(summary = "Alibaba Cloud SMS callback", description = "See https://help.aliyun.com/document_detail/120998.html document")
    public CommonResult<Boolean> receiveAliyunSmsStatus(HttpServletRequest request) throws Throwable {
        String text = ServletUtils.getBody(request);
        smsSendService.receiveSmsStatus(SmsChannelEnum.ALIYUN.getCode(), text);
        return success(true);
    }

    @PostMapping("/tencent")
    @PermitAll
    @TenantIgnore
    @Operation(summary = "Tencent Cloud SMS callback", description = "See documentation at https://cloud.tencent.com/document/product/382/52077")
    public CommonResult<Boolean> receiveTencentSmsStatus(HttpServletRequest request) throws Throwable {
        String text = ServletUtils.getBody(request);
        smsSendService.receiveSmsStatus(SmsChannelEnum.TENCENT.getCode(), text);
        return success(true);
    }


    @PostMapping("/huawei")
    @PermitAll
    @TenantIgnore
    @Operation(summary = "Huawei Cloud SMS callback", description = "See https://support.huaweicloud.com/API-msgsms/sms_05_0003.html document")
    public CommonResult<Boolean> receiveHuaweiSmsStatus(@RequestBody String requestBody) throws Throwable {
        smsSendService.receiveSmsStatus(SmsChannelEnum.HUAWEI.getCode(), requestBody);
        return success(true);
    }

    @PostMapping("/qiniu")
    @PermitAll
    @TenantIgnore
    @Operation(summary = "Qiniu Cloud SMS callback", description = "See https://developer.qiniu.com/SMS/5910/message-push documentation")
    public CommonResult<Boolean> receiveQiniuSmsStatus(@RequestBody String requestBody) throws Throwable {
        smsSendService.receiveSmsStatus(SmsChannelEnum.QINIU.getCode(), requestBody);
        return success(true);
    }

}