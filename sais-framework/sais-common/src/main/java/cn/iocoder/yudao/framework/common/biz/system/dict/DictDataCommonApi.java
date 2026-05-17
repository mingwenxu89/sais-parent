package cn.iocoder.yudao.framework.common.biz.system.dict;

import cn.iocoder.yudao.framework.common.biz.system.dict.dto.DictDataRespDTO;

import java.util.List;

/**
 * Dictionary data API interface
 *
 * @author Yudao Source Code
 */
public interface DictDataCommonApi {

 /**
     * Get a dictionary data list of the specified dictionary type
 *
     * @param dictType dictionary type
     * @return Dictionary data list
 */
 List<DictDataRespDTO> getDictDataList(String dictType);

}
