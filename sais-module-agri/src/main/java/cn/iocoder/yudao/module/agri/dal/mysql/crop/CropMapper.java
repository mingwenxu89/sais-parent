package cn.iocoder.yudao.module.agri.dal.mysql.crop;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.crop.vo.CropPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.crop.CropDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CropMapper extends BaseMapperX<CropDO> {

    default PageResult<CropDO> selectPage(CropPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CropDO>()
                .likeIfPresent(CropDO::getCropName, reqVO.getCropName())
                .eqIfPresent(CropDO::getCropType, reqVO.getCropType())
                .orderByDesc(CropDO::getId));
    }

}
