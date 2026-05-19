package cn.iocoder.yudao.module.agri.dal.mysql.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPlanPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropPlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    /** Get field IDs that have an active crop plan today (start_date <= today <= end_date). */
    default List<Long> selectCurrentFieldIds() {
        LocalDate today = LocalDate.now();
        return selectList(new LambdaQueryWrapperX<CropPlanDO>()
                .select(CropPlanDO::getFieldId)
                .le(CropPlanDO::getStartDate, today)
                .ge(CropPlanDO::getEndDate, today))
                .stream()
                .map(CropPlanDO::getFieldId)
                .filter(fieldId -> fieldId != null)
                .distinct()
                .collect(Collectors.toList());
    }

    default PageResult<CropPlanDO> selectPage(CropPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CropPlanDO>()
                .eqIfPresent(CropPlanDO::getCropId, reqVO.getCropId())
                .eqIfPresent(CropPlanDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(CropPlanDO::getGrowStatus, reqVO.getGrowStatus())
                .orderByDesc(CropPlanDO::getId));
    }

}
