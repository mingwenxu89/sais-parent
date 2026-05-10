package cn.iocoder.yudao.module.agri.dal.mysql.farm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FarmMapper extends BaseMapperX<FarmDO> {

    default PageResult<FarmDO> selectPage(FarmPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FarmDO>()
                .orderByDesc(FarmDO::getId));
    }

}
