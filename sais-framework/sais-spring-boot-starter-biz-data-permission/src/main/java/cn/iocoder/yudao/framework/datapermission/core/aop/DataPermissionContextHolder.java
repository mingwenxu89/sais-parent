package cn.iocoder.yudao.framework.datapermission.core.aop;

import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.LinkedList;
import java.util.List;

/**
 * {@link DataPermission} annotation Context context
 *
 * @author Yudao Source Code
 */
public class DataPermissionContextHolder {

 /**
     * The reason for using List is that there may be nested calls to methods
 */
 private static final ThreadLocal<LinkedList<DataPermission>> DATA_PERMISSIONS =
 TransmittableThreadLocal.withInitial(LinkedList::new);

 /**
     * Get the current DataPermission annotation
 *
     * @return DataPermission annotation
 */
 public static DataPermission get() {
 return DATA_PERMISSIONS.get().peekLast();
 }

 /**
     * Push DataPermission annotation
 *
     * @param dataPermission DataPermission annotation
 */
 public static void add(DataPermission dataPermission) {
 DATA_PERMISSIONS.get().addLast(dataPermission);
 }

 /**
     * Pop DataPermission annotation
 *
     * @return DataPermission annotation
 */
 public static DataPermission remove() {
 DataPermission dataPermission = DATA_PERMISSIONS.get().removeLast();
        // When there is no element, clear ThreadLocal
 if (DATA_PERMISSIONS.get().isEmpty()) {
 DATA_PERMISSIONS.remove();
 }
 return dataPermission;
 }

 /**
     * Get all DataPermission
 *
     * @return DataPermission Queue
 */
 public static List<DataPermission> getAll() {
 return DATA_PERMISSIONS.get();
 }

 /**
     * clear context
 *
     * Currently only used for single testing
 */
 public static void clear() {
 DATA_PERMISSIONS.remove();
 }

}
