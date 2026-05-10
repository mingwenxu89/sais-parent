package cn.iocoder.yudao.module.agri.dal.mysql.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface CropPlanMapper extends BaseMapperX<CropPlanDO> {

    /** Get the currently ongoing crop plan for a field (start_date <= today <= end_date) */
    default CropPlanDO selectCurrentByFieldId(Long fieldId) {
        LocalDate today = LocalDate.now();
        return selectOne(new LambdaQueryWrapperX<CropPlanDO>()
                .eq(CropPlanDO::getFieldId, fieldId)
                .le(CropPlanDO::getStartDate, today)
                .ge(CropPlanDO::getEndDate, today)
                .orderByDesc(CropPlanDO::getId)
                .last("LIMIT 1"));
    }

    default PageResult<CropPlanDO> selectPage(CropPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CropPlanDO>()
                .eqIfPresent(CropPlanDO::getCropId, reqVO.getCropId())
                .eqIfPresent(CropPlanDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(CropPlanDO::getGrowStatus, reqVO.getGrowStatus())
                .orderByDesc(CropPlanDO::getId));
    }

}
