package cn.iocoder.yudao.module.system.controller.admin.notice;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.api.websocket.WebSocketSenderApi;
import cn.iocoder.yudao.module.system.controller.admin.notice.vo.NoticePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.notice.vo.NoticeRespVO;
import cn.iocoder.yudao.module.system.controller.admin.notice.vo.NoticeSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.notice.NoticeDO;
import cn.iocoder.yudao.module.system.service.notice.NoticeService;
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

@Tag(name = "Management background - notifications and announcements")
@RestController
@RequestMapping("/system/notice")
@Validated
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private WebSocketSenderApi webSocketSenderApi;

    @PostMapping("/create")
    @Operation(summary = "Create notification announcement")
    @PreAuthorize("@ss.hasPermission('system:notice:create')")
    public CommonResult<Long> createNotice(@Valid @RequestBody NoticeSaveReqVO createReqVO) {
        Long noticeId = noticeService.createNotice(createReqVO);
        return success(noticeId);
    }

    @PutMapping("/update")
    @Operation(summary = "Modification Notice Announcement")
    @PreAuthorize("@ss.hasPermission('system:notice:update')")
    public CommonResult<Boolean> updateNotice(@Valid @RequestBody NoticeSaveReqVO updateReqVO) {
        noticeService.updateNotice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete notification announcement")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:notice:delete')")
    public CommonResult<Boolean> deleteNotice(@RequestParam("id") Long id) {
        noticeService.deleteNotice(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "Bulk deletion notification announcement")
    @Parameter(name = "ids", description = "ID list", required = true)
    @PreAuthorize("@ss.hasPermission('system:notice:delete')")
    public CommonResult<Boolean> deleteNoticeList(@RequestParam("ids") List<Long> ids) {
        noticeService.deleteNoticeList(ids);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "Get notification announcement list")
    @PreAuthorize("@ss.hasPermission('system:notice:query')")
    public CommonResult<PageResult<NoticeRespVO>> getNoticePage(@Validated NoticePageReqVO pageReqVO) {
        PageResult<NoticeDO> pageResult = noticeService.getNoticePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, NoticeRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "Get notification announcements")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:notice:query')")
    public CommonResult<NoticeRespVO> getNotice(@RequestParam("id") Long id) {
        NoticeDO notice = noticeService.getNotice(id);
        return success(BeanUtils.toBean(notice, NoticeRespVO.class));
    }

    @PostMapping("/push")
    @Operation(summary = "Push notification announcement", description = "Only sent to users with online WebSocket connections")
    @Parameter(name = "id", description = "ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:notice:update')")
    public CommonResult<Boolean> push(@RequestParam("id") Long id) {
        NoticeDO notice = noticeService.getNotice(id);
        Assert.notNull(notice, "Announcement cannot be empty");
        // Push to online users via WebSocket
        webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), "notice-push", notice);
        return success(true);
    }

}
