package cn.iocoder.yudao.module.agri.dal.mysql.field;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FieldMapper extends BaseMapperX<FieldDO> {

    default PageResult<FieldDO> selectPage(FieldPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FieldDO>()
                .eq(reqVO.getFarmId() != null, FieldDO::getFarmId, reqVO.getFarmId())
                .likeIfPresent(FieldDO::getFieldName, reqVO.getFieldName())
                .eqIfPresent(FieldDO::getGrowStatus, reqVO.getGrowStatus())
                .orderByDesc(FieldDO::getId));
    }

}
