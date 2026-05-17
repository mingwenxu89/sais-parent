package cn.iocoder.yudao.framework.common.util.object;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.SortablePageParam;
import cn.iocoder.yudao.framework.common.pojo.SortingField;
import org.springframework.util.Assert;

import static java.util.Collections.singletonList;

/**
 * {@link cn.iocoder.yudao.framework.common.pojo.PageParam} tool class
 *
 * @author Yudao Source Code
 */
public class PageUtils {

 private static final Object[] ORDER_TYPES = new String[]{SortingField.ORDER_ASC, SortingField.ORDER_DESC};

 public static int getStart(PageParam pageParam) {
 return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
 }

 /**
     * Build sorting field (default reverse order)
 *
     * @param func Lambda expression for sorting field
     * @param <T> The type of sorting field
     * @return sort field
 */
 public static <T> SortingField buildSortingField(Func1<T, ?> func) {
 return buildSortingField(func, SortingField.ORDER_DESC);
 }

 /**
     * Build sort field
 *
     * @param func Lambda expression for sorting field
     * @param order Sorting type {@link SortingField#ORDER_ASC} {@link SortingField#ORDER_DESC}
     * @param <T> The type of sorting field
     * @return sort field
 */
 public static <T> SortingField buildSortingField(Func1<T, ?> func, String order) {
        Assert.isTrue(ArrayUtil.contains(ORDER_TYPES, order), String.format("The sorting type of the field can only be %s/%s", ORDER_TYPES));

 String fieldName = LambdaUtil.getFieldName(func);
 return new SortingField(fieldName, order);
 }

 /**
     * Build default sort field
     * Sets the sort field if it is empty; otherwise ignores
 *
     * @param sortablePageParam Sort paging query parameters
     * @param func Lambda expression for sorting field
     * @param <T> The type of sorting field
 */
 public static <T> void buildDefaultSortingField(SortablePageParam sortablePageParam, Func1<T, ?> func) {
 if (sortablePageParam != null && CollUtil.isEmpty(sortablePageParam.getSortingFields())) {
 sortablePageParam.setSortingFields(singletonList(buildSortingField(func)));
 }
 }

}
