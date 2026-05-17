package cn.iocoder.yudao.framework.common.util.object;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * Bean utility class
 *
 * 1. By default, {@link cn.hutool.core.bean.BeanUtil} is used as the implementation class. Although the performance of different bean tools is different, for most students’ projects, you don’t need to worry about this performance.
 * 2. For complex object conversion, you can search for reference AuthConvert implementation, which is implemented through mapstruct + default
 *
 * @author Yudao Source Code
 */
public class BeanUtils {

 public static <T> T toBean(Object source, Class<T> targetClass) {
 return BeanUtil.toBean(source, targetClass);
 }

 public static <T> T toBean(Object source, Class<T> targetClass, Consumer<T> peek) {
 T target = toBean(source, targetClass);
 if (target != null) {
 peek.accept(target);
 }
 return target;
 }

 public static <S, T> List<T> toBean(List<S> source, Class<T> targetType) {
 if (source == null) {
 return null;
 }
 return CollectionUtils.convertList(source, s -> toBean(s, targetType));
 }

 public static <S, T> List<T> toBean(List<S> source, Class<T> targetType, Consumer<T> peek) {
 List<T> list = toBean(source, targetType);
 if (list != null) {
 list.forEach(peek);
 }
 return list;
 }

 public static <S, T> PageResult<T> toBean(PageResult<S> source, Class<T> targetType) {
 return toBean(source, targetType, null);
 }

 public static <S, T> PageResult<T> toBean(PageResult<S> source, Class<T> targetType, Consumer<T> peek) {
 if (source == null) {
 return null;
 }
 List<T> list = toBean(source.getList(), targetType);
 if (peek != null) {
 list.forEach(peek);
 }
 return new PageResult<>(list, source.getTotal());
 }

 public static void copyProperties(Object source, Object target) {
 if (source == null || target == null) {
 return;
 }
 BeanUtil.copyProperties(source, target, false);
 }

}