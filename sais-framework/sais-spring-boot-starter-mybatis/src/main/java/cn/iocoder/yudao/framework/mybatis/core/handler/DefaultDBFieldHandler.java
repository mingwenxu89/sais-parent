package cn.iocoder.yudao.framework.mybatis.core.handler;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Common parameter filling implementation class
 *
 * If there is no explicit assignment of values ​​to the general parameters, the general parameters will be filled in and assigned here.
 *
 * @author hexiaowu
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

 @Override
 @SuppressWarnings("PatternVariableCanBeUsed")
 public void insertFill(MetaObject metaObject) {
 if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
 BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

 LocalDateTime current = LocalDateTime.now();
            // If the creation time is empty, the current time will be used as the insertion time.
 if (Objects.isNull(baseDO.getCreateTime())) {
 baseDO.setCreateTime(current);
 }
            // If the update time is empty, the current time will be used as the update time.
 if (Objects.isNull(baseDO.getUpdateTime())) {
 baseDO.setUpdateTime(current);
 }

 Long userId = SecurityFrameworkUtils.getLoginUserId();
            // If the currently logged in user is not empty and the creator is empty, then the currently logged in user is the creator.
 if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreator())) {
 baseDO.setCreator(userId.toString());
 }
            // If the currently logged in user is not empty and the updater is empty, then the currently logged in user is the updater.
 if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
 baseDO.setUpdater(userId.toString());
 }
 }
 }

 @Override
 public void updateFill(MetaObject metaObject) {
        // If the update time is empty, the current time will be used as the update time.
 Object modifyTime = getFieldValByName("updateTime", metaObject);
 if (Objects.isNull(modifyTime)) {
 setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
 }

        // If the currently logged in user is not empty and the updater is empty, then the currently logged in user is the updater.
 Object modifier = getFieldValByName("updater", metaObject);
 Long userId = SecurityFrameworkUtils.getLoginUserId();
 if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
 setFieldValByName("updater", userId.toString(), metaObject);
 }
 }
}
