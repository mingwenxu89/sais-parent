package cn.iocoder.yudao.module.system.service.dict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dict.vo.data.DictDataSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Dict data Service API
 *
 * @author ruoyi
 */
public interface DictDataService {

    /**
     * Create dict data
     *
     * @param createReqVO Dict data information
     * @return Dict data ID
     */
    Long createDictData(DictDataSaveReqVO createReqVO);

    /**
     * Update dict data
     *
     * @param updateReqVO Dict data information
     */
    void updateDictData(DictDataSaveReqVO updateReqVO);

    /**
     * Delete dict data
     *
     * @param id Dict data ID
     */
    void deleteDictData(Long id);

    /**
     * Delete dict data in batches
     *
     * @param ids Dict data ID list
     */
    void deleteDictDataList(List<Long> ids);

    /**
     * Get dict data list
     *
     * @param status   Status
     * @param dictType Dict Type
     * @return Full list of dict data
     */
    List<DictDataDO> getDictDataList(@Nullable Integer status, @Nullable String dictType);

    /**
     * Get a paginated list of dict data
     *
     * @param pageReqVO Pagination request
     * @return Dict data paginated list
     */
    PageResult<DictDataDO> getDictDataPage(DictDataPageReqVO pageReqVO);

    /**
     * Get dict data details
     *
     * @param id Dict data ID
     * @return Dict Data
     */
    DictDataDO getDictData(Long id);

    /**
     * Get the ID of data of the specified dict type
     *
     * @param dictType Dict Type
     * @return Data quantity
     */
    long getDictDataCountByDictType(String dictType);

    /**
     * Verify that the dict data are valid. The following situations will be deemed invalid:
     * 1. Dict data does not exist
     * 2. Dict data is disabled
     *
     * @param dictType Dict Type
     * @param values   array of dict data values
     */
    void validateDictDataList(String dictType, Collection<String> values);

    /**
     * Get the specified dict data
     *
     * @param dictType Dict Type
     * @param value    dict data value
     * @return Dict Data
     */
    DictDataDO getDictData(String dictType, String value);

    /**
     * Parse to obtain the specified dict data from the cache
     *
     * @param dictType Dict Type
     * @param label    dict data tags
     * @return Dict Data
     */
    DictDataDO parseDictData(String dictType, String label);

    /**
     * Get a dict data list of the specified data type
     *
     * @param dictType Dict Type
     * @return Dict data list
     */
    List<DictDataDO> getDictDataListByDictType(String dictType);

}
