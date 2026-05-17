package cn.iocoder.yudao.module.system.api.dict;

import cn.iocoder.yudao.framework.common.biz.system.dict.DictDataCommonApi;

import java.util.Collection;

/**
 * Dict data API API
 *
 * @author Yudao Source Code
 */
public interface DictDataApi extends DictDataCommonApi {

    /**
     * Verify that the dict data are valid. The following situations will be deemed invalid:
     * 1. Dict data does not exist
     * 2. Dict data is disabled
     *
     * @param dictType Dict Type
     * @param values   array of dict data values
     */
    void validateDictDataList(String dictType, Collection<String> values);

}
