package cn.iocoder.yudao.module.agri.service.farm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.farm.FarmConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.dal.mysql.farm.FarmMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

@Service
@Validated
public class FarmServiceImpl implements FarmService {

    @Resource
    private FarmMapper farmInfoMapper;

    @Override
    public FarmDO getCurrentFarm() {
        Long tenantId = TenantContextHolder.getRequiredTenantId();
        return farmInfoMapper.selectOne(FarmDO::getTenantId, tenantId);
    }

    @Override
    public Long saveCurrentFarm(FarmSaveReqVO reqVO) {
        Long tenantId = TenantContextHolder.getRequiredTenantId();
        FarmDO existed = farmInfoMapper.selectOne(FarmDO::getTenantId, tenantId);
        if (existed == null) {
            FarmDO farmInfo = FarmConvert.INSTANCE.convert(reqVO);
            farmInfoMapper.insert(farmInfo);
            return farmInfo.getId();
        }
        FarmDO updateObj = FarmConvert.INSTANCE.convert(reqVO);
        updateObj.setId(existed.getId());
        farmInfoMapper.updateById(updateObj);
        return existed.getId();
    }

    @Override
    public PageResult<FarmDO> getFarmPage(FarmPageReqVO pageReqVO) {
        return farmInfoMapper.selectPage(pageReqVO);
    }

}
