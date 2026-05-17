package cn.iocoder.yudao.module.system.controller.admin.socail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.social.SocialClientApi;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaSubscribeMessageSendReqDTO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientRespVO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.client.SocialClientSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialClientDO;
import cn.iocoder.yudao.module.system.service.social.SocialClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Management backend - social client")
@RestController
@RequestMapping("/system/social-client")
@Validated
public class SocialClientController {

    @Resource
    private SocialClientService socialClientService;
    @Resource
    private SocialClientApi socialClientApi;

    @PostMapping("/create")
    @Operation(summary = "Create a social client")
    @PreAuthorize("@ss.hasPermission('system:social-client:create')")
    public CommonResult<Long> createSocialClient(@Valid @RequestBody SocialClientSaveReqVO createReqVO) {
        return success(socialClientService.createSocialClient(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update social client")
    @PreAuthorize("@ss.hasPermission('system:social-client:update')")
    public CommonResult<Boolean> updateSocialClient(@Valid @RequestBody SocialClientSaveReqVO updateReqVO) {
        socialClientService.updateSocialClient(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete social client")
    @Parameter(name = "id", description = "ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:social-client:delete')")
    public CommonResult<Boolean> deleteSocialClient(@RequestParam("id") Long id) {
        socialClientService.deleteSocialClient(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "ID list", required = true)
    @Operation(summary = "Delete social clients in batches")
    @PreAuthorize("@ss.hasPermission('system:social-client:delete')")
    public CommonResult<Boolean> deleteSocialClientList(@RequestParam("ids") List<Long> ids) {
        socialClientService.deleteSocialClientList(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "Get social client")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:social-client:query')")
    public CommonResult<SocialClientRespVO> getSocialClient(@RequestParam("id") Long id) {
        SocialClientDO client = socialClientService.getSocialClient(id);
        return success(BeanUtils.toBean(client, SocialClientRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "Get social client pagination")
    @PreAuthorize("@ss.hasPermission('system:social-client:query')")
    public CommonResult<PageResult<SocialClientRespVO>> getSocialClientPage(@Valid SocialClientPageReqVO pageVO) {
        PageResult<SocialClientDO> pageResult = socialClientService.getSocialClientPage(pageVO);
        return success(BeanUtils.toBean(pageResult, SocialClientRespVO.class));
    }

    @PostMapping("/send-subscribe-message")
    @Operation(summary = "Send subscription message") // for testing
    @PreAuthorize("@ss.hasPermission('system:social-client:query')")
    public void sendSubscribeMessage(@RequestBody SocialWxaSubscribeMessageSendReqDTO reqDTO) {
        socialClientApi.sendWxaSubscribeMessage(reqDTO);
    }

}
