package cn.iocoder.yudao.framework.mybatis.core.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.SortablePageParam;
import cn.iocoder.yudao.framework.common.pojo.SortingField;
import cn.iocoder.yudao.framework.mybatis.core.util.JdbcUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * Expand on the BaseMapper of MyBatis Plus to provide more capabilities
 *
 * 1. {@link BaseMapper} is the basic interface of MyBatis Plus and provides basic CRUD capabilities.
 * 2. {@link MPJBaseMapper} is the basic interface of MyBatis Plus Join, providing table join capabilities
 */
public interface BaseMapperX<T> extends MPJBaseMapper<T> {

 default PageResult<T> selectPage(SortablePageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
 return selectPage(pageParam, pageParam.getSortingFields(), queryWrapper);
 }

 default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
 return selectPage(pageParam, null, queryWrapper);
 }

 default PageResult<T> selectPage(PageParam pageParam, Collection<SortingField> sortingFields, @Param("ew") Wrapper<T> queryWrapper) {
        // Special: No paging, query all directly
 if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
 MyBatisUtils.addOrder(queryWrapper, sortingFields);
 List<T> list = selectList(queryWrapper);
 return new PageResult<>(list, (long) list.size());
 }

        // MyBatis Plus query
 IPage<T> mpPage = MyBatisUtils.buildPage(pageParam, sortingFields);
 selectPage(mpPage, queryWrapper);
        // Conversion returns
 return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
 }

 default <D> PageResult<D> selectJoinPage(PageParam pageParam, Class<D> clazz, MPJLambdaWrapper<T> lambdaWrapper) {
        // Special: No paging, query all directly
 if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
 List<D> list = selectJoinList(clazz, lambdaWrapper);
 return new PageResult<>(list, (long) list.size());
 }

        // MyBatis Plus Join Query
 IPage<D> mpPage = MyBatisUtils.buildPage(pageParam);
 mpPage = selectJoinPage(mpPage, clazz, lambdaWrapper);
        // Conversion returns
 return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
 }

 /**
     * Execute a paginated query and return the results.
 *
     * @param pageParam Paging parameters include page number, number of items per page, and sorting field information. If pageSize is {@link PageParam#PAGE_SIZE_NONE}, all data will be queried directly without paging.
     * @param clazz Result set class type
     * @param lambdaWrapper MyBatis Plus Join query condition wrapper
     * @param <D> Generic type of result set
     * @return Returns the results of the paging query, including the total number of records and the data list of the current page
 */
 default <D> PageResult<D> selectJoinPage(SortablePageParam pageParam, Class<D> clazz, MPJLambdaWrapper<T> lambdaWrapper) {
        // Special: No paging, query all directly
 if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
 List<D> list = selectJoinList(clazz, lambdaWrapper);
 return new PageResult<>(list, (long) list.size());
 }

        // MyBatis Plus Join Query
 IPage<D> mpPage = MyBatisUtils.buildPage(pageParam, pageParam.getSortingFields());
 mpPage = selectJoinPage(mpPage, clazz, lambdaWrapper);
        // Conversion returns
 return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
 }

 default <DTO> PageResult<DTO> selectJoinPage(PageParam pageParam, Class<DTO> resultTypeClass, MPJBaseJoin<T> joinQueryWrapper) {
 IPage<DTO> mpPage = MyBatisUtils.buildPage(pageParam);
 selectJoinPage(mpPage, resultTypeClass, joinQueryWrapper);
        // Conversion returns
 return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
 }

 default T selectOne(String field, Object value) {
 return selectOne(new QueryWrapper<T>().eq(field, value));
 }

 default T selectOne(SFunction<T, ?> field, Object value) {
 return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
 }

 default T selectOne(String field1, Object value1, String field2, Object value2) {
 return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
 }

 default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
 return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
 }

 default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
 SFunction<T, ?> field3, Object value3) {
 return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
 }

 /**
     * Get the first record that meets the conditions
 *
     * Purpose: To solve the problem of using selectOne to report an error after inserting multiple records in a concurrent scenario.
 *
     * @param field Field name
     * @param value field value
     * @return entity
 */
 default T selectFirstOne(SFunction<T, ?> field, Object value) {
        // If you explicitly use MySQL and other scenarios, you can consider using LIMIT 1 for optimization
 List<T> list = selectList(new LambdaQueryWrapper<T>().eq(field, value));
 return CollUtil.getFirst(list);
 }

 default T selectFirstOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
 List<T> list = selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
 return CollUtil.getFirst(list);
 }

 default T selectFirstOne(SFunction<T,?> field1, Object value1, SFunction<T,?> field2, Object value2,
 SFunction<T,?> field3, Object value3) {
 List<T> list = selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
 return CollUtil.getFirst(list);
 }


 default Long selectCount() {
 return selectCount(new QueryWrapper<>());
 }

 default Long selectCount(String field, Object value) {
 return selectCount(new QueryWrapper<T>().eq(field, value));
 }

 default Long selectCount(SFunction<T, ?> field, Object value) {
 return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
 }

 default List<T> selectList() {
 return selectList(new QueryWrapper<>());
 }

 default List<T> selectList(String field, Object value) {
 return selectList(new QueryWrapper<T>().eq(field, value));
 }

 default List<T> selectList(SFunction<T, ?> field, Object value) {
 return selectList(new LambdaQueryWrapper<T>().eq(field, value));
 }

 default List<T> selectList(String field, Collection<?> values) {
 if (CollUtil.isEmpty(values)) {
 return CollUtil.newArrayList();
 }
 return selectList(new QueryWrapper<T>().in(field, values));
 }

 default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
 if (CollUtil.isEmpty(values)) {
 return CollUtil.newArrayList();
 }
 return selectList(new LambdaQueryWrapper<T>().in(field, values));
 }

 default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
 return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
 }

 /**
     * Batch insertion, suitable for inserting large amounts of data
 *
     * @param entities Entities
 */
 default Boolean insertBatch(Collection<T> entities) {
        // Special: After SQL Server batch inserts, an error will be reported when obtaining the id, so it is processed through a loop.
 DbType dbType = JdbcUtils.getDbType();
 if (JdbcUtils.isSQLServer(dbType)) {
 entities.forEach(this::insert);
 return CollUtil.isNotEmpty(entities);
 }
 return Db.saveBatch(entities);
 }

 /**
     * Batch insertion, suitable for inserting large amounts of data
 *
     * @param entities Entities
     * @param size Insert quantity DB.saveBatch defaults to 1000
 */
 default Boolean insertBatch(Collection<T> entities, int size) {
        // Special: After SQL Server batch inserts, an error will be reported when obtaining the id, so it is processed through a loop.
 DbType dbType = JdbcUtils.getDbType();
 if (JdbcUtils.isSQLServer(dbType)) {
 entities.forEach(this::insert);
 return CollUtil.isNotEmpty(entities);
 }
 return Db.saveBatch(entities, size);
 }

 default int updateBatch(T update) {
 return update(update, new QueryWrapper<>());
 }

 default Boolean updateBatch(Collection<T> entities) {
 return Db.updateBatchById(entities);
 }

 default Boolean updateBatch(Collection<T> entities, int size) {
 return Db.updateBatchById(entities, size);
 }

 default int delete(String field, String value) {
 return delete(new QueryWrapper<T>().eq(field, value));
 }

 default int delete(SFunction<T, ?> field, Object value) {
 return delete(new LambdaQueryWrapper<T>().eq(field, value));
 }

 default int deleteBatch(SFunction<T, ?> field, Collection<?> values) {
 if (CollUtil.isEmpty(values)) {
 return 0;
 }
 return delete(new LambdaQueryWrapper<T>().in(field, values));
 }

}
