package cn.iocoder.yudao.module.system.controller.admin.ip;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.framework.ip.core.utils.IPUtils;
import cn.iocoder.yudao.module.system.controller.admin.ip.vo.AreaNodeRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Admin Backend - Region")
@RestController
@RequestMapping("/system/area")
@Validated
public class AreaController {

    @GetMapping("/tree")
    @Operation(summary = "get region tree")
    public CommonResult<List<AreaNodeRespVO>> getAreaTree() {
        Area area = AreaUtils.getArea(Area.ID_CHINA);
        Assert.notNull(area, "Can't get China");
        return success(BeanUtils.toBean(area.getChildren(), AreaNodeRespVO.class));
    }

    @GetMapping("/get-by-ip")
    @Operation(summary = "Get the region name corresponding to the IP")
    @Parameter(name = "ip", description = "IP", required = true)
    public CommonResult<String> getAreaByIp(@RequestParam("ip") String ip) {
        // get city
        Area area = IPUtils.getArea(ip);
        if (area == null) {
            return success("Unknown");
        }
        // Formatted return
        return success(AreaUtils.format(area.getId()));
    }

}
