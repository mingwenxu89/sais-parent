package cn.iocoder.yudao.module.system.controller.admin.sms;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelRespVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel.SmsChannelSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.sms.SmsChannelDO;
import cn.iocoder.yudao.module.system.service.sms.SmsChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Management backend - SMS channel")
@RestController
@RequestMapping("system/sms-channel")
public class SmsChannelController {

    @Resource
    private SmsChannelService smsChannelService;

    @PostMapping("/create")
    @Operation(summary = "Create SMS channel")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:create')")
    public CommonResult<Long> createSmsChannel(@Valid @RequestBody SmsChannelSaveReqVO createReqVO) {
        return success(smsChannelService.createSmsChannel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update SMS channel")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:update')")
    public CommonResult<Boolean> updateSmsChannel(@Valid @RequestBody SmsChannelSaveReqVO updateReqVO) {
        smsChannelService.updateSmsChannel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete SMS channel")
    @Parameter(name = "id", description = "ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:sms-channel:delete')")
    public CommonResult<Boolean> deleteSmsChannel(@RequestParam("id") Long id) {
        smsChannelService.deleteSmsChannel(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "ID list", required = true)
    @Operation(summary = "Delete SMS channels in batches")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:delete')")
    public CommonResult<Boolean> deleteSmsChannelList(@RequestParam("ids") List<Long> ids) {
        smsChannelService.deleteSmsChannelList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get SMS channel")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:query')")
    public CommonResult<SmsChannelRespVO> getSmsChannel(@RequestParam("id") Long id) {
        SmsChannelDO channel = smsChannelService.getSmsChannel(id);
        return success(BeanUtils.toBean(channel, SmsChannelRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "Get SMS channel pagination")
    @PreAuthorize("@ss.hasPermission('system:sms-channel:query')")
    public CommonResult<PageResult<SmsChannelRespVO>> getSmsChannelPage(@Valid SmsChannelPageReqVO pageVO) {
        PageResult<SmsChannelDO> pageResult = smsChannelService.getSmsChannelPage(pageVO);
        return success(BeanUtils.toBean(pageResult, SmsChannelRespVO.class));
    }

    @GetMapping({"/list-all-simple", "/simple-list"})
    @Operation(summary = "Get a streamlined list of SMS channels", description = "Contains disabled SMS channels")
    public CommonResult<List<SmsChannelSimpleRespVO>> getSimpleSmsChannelList() {
        List<SmsChannelDO> list = smsChannelService.getSmsChannelList();
        list.sort(Comparator.comparing(SmsChannelDO::getId));
        return success(BeanUtils.toBean(list, SmsChannelSimpleRespVO.class));
    }

}
