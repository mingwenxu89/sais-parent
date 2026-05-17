package cn.iocoder.yudao.module.system.api.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.system.api.dept.dto.PostRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Position API API
 *
 * @author Yudao Source Code
 */
public interface PostApi {

    /**
     * Verify whether the positions are valid. The following situations will be deemed invalid:
     * 1. The position ID does not exist
     * 2. The position is disabled
     *
     * @param ids Position ID array
     */
    void validPostList(Collection<Long> ids);

    List<PostRespDTO> getPostList(Collection<Long> ids);

    default Map<Long, PostRespDTO> getPostMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return MapUtil.empty();
        }

        List<PostRespDTO> list = getPostList(ids);
        return CollectionUtils.convertMap(list, PostRespDTO::getId);
    }

}
