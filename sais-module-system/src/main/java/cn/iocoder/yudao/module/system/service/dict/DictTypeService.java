package cn.iocoder.yudao.module.system.service.dict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.type.DictTypeSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictTypeDO;

import java.util.List;

/**
 * Dict type Service API
 *
 * @author Yudao Source Code
 */
public interface DictTypeService {

    /**
     * Create dict type
     *
     * @param createReqVO Dict type information
     * @return Dict type ID
     */
    Long createDictType(DictTypeSaveReqVO createReqVO);

    /**
     * Update dict type
     *
     * @param updateReqVO Dict type information
     */
    void updateDictType(DictTypeSaveReqVO updateReqVO);

    /**
     * Delete dict type
     *
     * @param id Dict type ID
     */
    void deleteDictType(Long id);

    /**
     * Delete dict types in batches
     *
     * @param ids Dict type IDed list
     */
    void deleteDictTypeList(List<Long> ids);

    /**
     * Get a paginated list of dict types
     *
     * @param pageReqVO Pagination request
     * @return Dict type paginated list
     */
    PageResult<DictTypeDO> getDictTypePage(DictTypePageReqVO pageReqVO);

    /**
     * Get dict type details
     *
     * @param id Dict type ID
     * @return Dict Type
     */
    DictTypeDO getDictType(Long id);

    /**
     * Get dict type details
     *
     * @param type Dict Type
     * @return Dict type details
     */
    DictTypeDO getDictType(String type);

    /**
     * Get a list of all dict types
     *
     * @return List of dict types
     */
    List<DictTypeDO> getDictTypeList();

}
