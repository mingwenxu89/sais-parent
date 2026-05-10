package cn.iocoder.yudao.module.agri.service.field;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.field.vo.FieldSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.field.FieldDO;

import jakarta.validation.Valid;

/**
 * Field Service interface
 */
public interface FieldService {

    Long createField(@Valid FieldSaveReqVO createReqVO);

    void updateField(@Valid FieldSaveReqVO updateReqVO);

    void deleteField(Long id);

    FieldDO getField(Long id);

    PageResult<FieldDO> getFieldPage(FieldPageReqVO pageReqVO);

}
