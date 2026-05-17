package cn.iocoder.yudao.module.agri.service.field;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldSaveReqVO;
import cn.iocoder.yudao.module.agri.convert.field.FieldConvert;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import cn.iocoder.yudao.module.agri.dal.mysql.farm.FarmMapper;
import cn.iocoder.yudao.module.agri.dal.mysql.field.FieldMapper;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.agri.enums.ErrorCodeConstants.FIELD_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class FieldServiceImpl implements FieldService {

    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private FarmMapper farmMapper;

    @Override
    public Long createField(FieldSaveReqVO createReqVO) {
        Long farmId = getCurrentTenantFarmId();
        if (farmId == null) {
            return null;
        }
        FieldDO fieldInfo = FieldConvert.INSTANCE.convert(createReqVO);
        fieldInfo.setFarmId(farmId);
        fieldMapper.insert(fieldInfo);
        return fieldInfo.getId();
    }

    @Override
    public void updateField(FieldSaveReqVO updateReqVO) {
        Long farmId = getCurrentTenantFarmId();
        if (farmId == null) {
            return;
        }
        FieldDO dbField = validateFieldExists(updateReqVO.getId());
        if (!farmId.equals(dbField.getFarmId())) {
            throw exception(FIELD_NOT_EXISTS);
        }
        FieldDO updateObj = FieldConvert.INSTANCE.convert(updateReqVO);
        updateObj.setFarmId(farmId);
        fieldMapper.updateById(updateObj);
    }

    @Override
    public void deleteField(Long id) {
        Long farmId = getCurrentTenantFarmId();
        if (farmId == null) {
            return;
        }
        FieldDO dbField = validateFieldExists(id);
        if (!farmId.equals(dbField.getFarmId())) {
            throw exception(FIELD_NOT_EXISTS);
        }
        fieldMapper.deleteById(id);
    }

    @Override
    public FieldDO getField(Long id) {
        Long farmId = getCurrentTenantFarmId();
        if (farmId == null) {
            return null;
        }
        FieldDO fieldInfo = fieldMapper.selectById(id);
        if (fieldInfo == null || !farmId.equals(fieldInfo.getFarmId())) {
            throw exception(FIELD_NOT_EXISTS);
        }
        return fieldInfo;
    }

    @Override
    public PageResult<FieldDO> getFieldPage(FieldPageReqVO pageReqVO) {
        Long farmId = getCurrentTenantFarmId();
        if (farmId == null) {
            return PageResult.empty();
        }
        pageReqVO.setFarmId(farmId);
        return fieldMapper.selectPage(pageReqVO);
    }

    private Long getCurrentTenantFarmId() {
        Long tenantId = TenantContextHolder.getRequiredTenantId();
        FarmDO farmInfo = farmMapper.selectOne(FarmDO::getTenantId, tenantId);
        if (farmInfo == null) {
            return null;
        }
        return farmInfo.getId();
    }

    @VisibleForTesting
    public FieldDO validateFieldExists(Long id) {
        if (id == null) {
            return null;
        }
        FieldDO fieldInfo = fieldMapper.selectById(id);
        if (fieldInfo == null) {
            throw exception(FIELD_NOT_EXISTS);
        }
        return fieldInfo;
    }

}
