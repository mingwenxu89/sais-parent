package cn.iocoder.yudao.module.system.controller.app.ip;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.system.controller.app.ip.vo.AppAreaNodeRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.PermitAll;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "User App - area")
@RestController
@RequestMapping("/system/area")
@Validated
public class AppAreaController {

    @GetMapping("/tree")
    @Operation(summary = "get region tree")
    @PermitAll
    public CommonResult<List<AppAreaNodeRespVO>> getAreaTree() {
        Area area = AreaUtils.getArea(Area.ID_CHINA);
        Assert.notNull(area, "Can't get China");
        return success(BeanUtils.toBean(area.getChildren(), AppAreaNodeRespVO.class));
    }

}
