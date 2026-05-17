package cn.iocoder.yudao.framework.datapermission.core.util;

import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.datapermission.core.aop.DataPermissionContextHolder;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

/**
 * Data Permissions Util
 *
 * @author Yudao Source Code
 */
public class DataPermissionUtils {

 private static DataPermission DATA_PERMISSION_DISABLE;

 @DataPermission(enable = false)
 @SneakyThrows
 private static DataPermission getDisableDataPermissionDisable() {
 if (DATA_PERMISSION_DISABLE == null) {
 DATA_PERMISSION_DISABLE = DataPermissionUtils.class
.getDeclaredMethod("getDisableDataPermissionDisable")
.getAnnotation(DataPermission.class);
 }
 return DATA_PERMISSION_DISABLE;
 }

 /**
     * Ignore data permissions and execute corresponding logic
 *
     * @param runnable logic
 */
 public static void executeIgnore(Runnable runnable) {
 addDisableDataPermission();
 try {
            // Execute runnable
 runnable.run();
 } finally {
 removeDataPermission();
 }
 }

 /**
     * Ignore data permissions and execute corresponding logic
 *
     * @param callable logic
     * @return Execution result
 */
 @SneakyThrows
 public static <T> T executeIgnore(Callable<T> callable) {
 addDisableDataPermission();
 try {
            // Execute callable
 return callable.call();
 } finally {
 removeDataPermission();
 }
 }

 /**
     * Add ignore data permission
 */
 public static void addDisableDataPermission(){
 DataPermission dataPermission = getDisableDataPermissionDisable();
 DataPermissionContextHolder.add(dataPermission);
 }

 public static void removeDataPermission(){
 DataPermissionContextHolder.remove();
 }

}
