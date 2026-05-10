package cn.iocoder.yudao.module.agri.dal.mysql.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropGrowthStagePageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropGrowthStageDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CropGrowthStageMapper extends BaseMapperX<CropGrowthStageDO> {

    default PageResult<CropGrowthStageDO> selectPage(CropGrowthStagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CropGrowthStageDO>()
                .eqIfPresent(CropGrowthStageDO::getCropId, reqVO.getCropId())
                .likeIfPresent(CropGrowthStageDO::getStageName, reqVO.getStageName())
                .eqIfPresent(CropGrowthStageDO::getStageOrder, reqVO.getStageOrder())
                .orderByAsc(CropGrowthStageDO::getCropId)
                .orderByAsc(CropGrowthStageDO::getStageOrder));
    }

    default List<CropGrowthStageDO> selectListByCropId(Long cropId) {
        return selectList(new LambdaQueryWrapperX<CropGrowthStageDO>()
                .eq(CropGrowthStageDO::getCropId, cropId)
                .orderByAsc(CropGrowthStageDO::getStageOrder));
    }

    @Delete("DELETE FROM sais_growth_stage WHERE crop_id = #{cropId}")
    void deleteByCropIdPhysically(@Param("cropId") Long cropId);

}
