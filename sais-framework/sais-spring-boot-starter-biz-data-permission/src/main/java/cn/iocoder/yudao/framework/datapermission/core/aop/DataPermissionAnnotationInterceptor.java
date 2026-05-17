package cn.iocoder.yudao.framework.datapermission.core.aop;

import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import lombok.Getter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link DataPermission} annotated interceptor
 * 1. Before executing the method, push the @DataPermission annotation onto the stack
 * 2. After executing the method, pop the @DataPermission annotation from the stack
 *
 * @author Yudao Source Code
 */
@DataPermission // This annotation is used for the empty object of {@link DATA_PERMISSION_NULL}
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

 /**
     * DataPermission empty object, used when the method has no {@link DataPermission} annotation, use DATA_PERMISSION_NULL for placeholder
 */
 static final DataPermission DATA_PERMISSION_NULL = DataPermissionAnnotationInterceptor.class.getAnnotation(DataPermission.class);

 @Getter
 private final Map<MethodClassKey, DataPermission> dataPermissionCache = new ConcurrentHashMap<>();

 @Override
 public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // push to stack
 DataPermission dataPermission = this.findAnnotation(methodInvocation);
 if (dataPermission != null) {
 DataPermissionContextHolder.add(dataPermission);
 }
 try {
            // execution logic
 return methodInvocation.proceed();
 } finally {
            // pop
 if (dataPermission != null) {
 DataPermissionContextHolder.remove();
 }
 }
 }

 private DataPermission findAnnotation(MethodInvocation methodInvocation) {
        // 1. Get from cache
 Method method = methodInvocation.getMethod();
 Object targetObject = methodInvocation.getThis();
 Class<?> clazz = targetObject != null ? targetObject.getClass(): method.getDeclaringClass();
 MethodClassKey methodClassKey = new MethodClassKey(method, clazz);
 DataPermission dataPermission = dataPermissionCache.get(methodClassKey);
 if (dataPermission != null) {
 return dataPermission != DATA_PERMISSION_NULL ? dataPermission: null;
 }

        // 2.1 Get from method
 dataPermission = AnnotationUtils.findAnnotation(method, DataPermission.class);
        // 2.2 Obtain from class
 if (dataPermission == null) {
 dataPermission = AnnotationUtils.findAnnotation(clazz, DataPermission.class);
 }
        // 2.3 Add to cache
 dataPermissionCache.put(methodClassKey, dataPermission != null ? dataPermission: DATA_PERMISSION_NULL);
 return dataPermission;
 }

}
