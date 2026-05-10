package cn.iocoder.yudao.module.agri.service.farm;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.farm.vo.FarmSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.farm.FarmDO;

import jakarta.validation.Valid;

/**
 * Farm Service interface
 */
public interface FarmService {

    FarmDO getCurrentFarm();

    Long saveCurrentFarm(@Valid FarmSaveReqVO reqVO);

    PageResult<FarmDO> getFarmPage(FarmPageReqVO pageReqVO);

}
