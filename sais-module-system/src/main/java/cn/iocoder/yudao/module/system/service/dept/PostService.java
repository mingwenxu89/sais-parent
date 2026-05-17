package cn.iocoder.yudao.module.system.service.dept;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.post.PostSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.PostDO;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Position Service API
 *
 * @author Yudao Source Code
 */
public interface PostService {

    /**
     * Create a position
     *
     * @param createReqVO Position information
     * @return Position ID
     */
    Long createPost(PostSaveReqVO createReqVO);

    /**
     * Update position
     *
     * @param updateReqVO Position information
     */
    void updatePost(PostSaveReqVO updateReqVO);

    /**
     * Delete job information
     *
     * @param id Position ID
     */
    void deletePost(Long id);

    /**
     * Delete job information in batches
     *
     * @param ids Position ID array
     */
    void deletePostList(List<Long> ids);

    /**
     * Get job list
     *
     * @param ids Position ID array
     * @return Department list
     */
    List<PostDO> getPostList(@Nullable Collection<Long> ids);

    /**
     * Get a list of eligible positions
     *
     * @param ids Array of position IDs. If empty, no filtering will be performed
     * @param statuses Status array. If empty, no filtering will be performed
     * @return Department list
     */
    List<PostDO> getPostList(@Nullable Collection<Long> ids,
                             @Nullable Collection<Integer> statuses);

    /**
     * Get a paged list of positions
     *
     * @param reqVO Paging conditions
     * @return Department paging list
     */
    PageResult<PostDO> getPostPage(PostPageReqVO reqVO);

    /**
     * Get job information
     *
     * @param id Position ID
     * @return Position information
     */
    PostDO getPost(Long id);

    /**
     * Verify whether the positions are valid. The following situations will be deemed invalid:
     * 1. The position ID does not exist
     * 2. The position is disabled
     *
     * @param ids Position ID array
     */
    void validatePostList(Collection<Long> ids);

}
