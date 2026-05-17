package cn.iocoder.yudao.module.system.controller.app.dict;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.app.dict.vo.AppDictDataRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "User App - Dict Data")
@RestController
@RequestMapping("/system/dict-data")
@Validated
public class AppDictDataController {

    @Resource
    private DictDataService dictDataService;

    @GetMapping("/type")
    @Operation(summary = "Query dict data information based on dict type")
    @Parameter(name = "type", description = "Dict Type", required = true, example = "common_status")
    @PermitAll
    public CommonResult<List<AppDictDataRespVO>> getDictDataListByType(@RequestParam("type") String type) {
        List<DictDataDO> list = dictDataService.getDictDataList(
                CommonStatusEnum.ENABLE.getStatus(), type);
        return success(BeanUtils.toBean(list, AppDictDataRespVO.class));
    }

}
