package cn.iocoder.yudao.framework.common.util.json.databind;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Timestamp-based LocalDateTime serializer
 *
 * @author Lao Wu
 */
@Slf4j
public class TimestampLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

 public static final TimestampLocalDateTimeSerializer INSTANCE = new TimestampLocalDateTimeSerializer();

 private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();

 @Override
 public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Case 1: If there is JSONFormat custom annotation, use it. https://github.com/YunaiV/ruoyi-vue-pro/pull/1019
 String fieldName = gen.getOutputContext().getCurrentName();
 if (fieldName != null) {
 Object currentValue = gen.getOutputContext().getCurrentValue();
 if (currentValue != null) {
 Class<?> clazz = currentValue.getClass();
 Map<String, Field> fieldMap = FIELD_CACHE.computeIfAbsent(clazz, this::buildFieldMap);
 Field field = fieldMap.get(fieldName);
                // Further fixes: https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/1480
 if (field != null && field.isAnnotationPresent(JsonFormat.class)) {
 JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
 try {
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern(jsonFormat.pattern());
 gen.writeString(formatter.format(value));
 return;
 } catch (Exception ex) {
                        log.warn("[serialize][({}#{}) Failed to use JSONFormat pattern, try to use default Long timestamp]",
 clazz.getName(), fieldName, ex);
 }
 }
 }
 }

        // Case 2: Convert LocalDateTime object to Long timestamp by default
 gen.writeNumber(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
 }

 /**
     * Build field mapping (caching)
 *
     * @param clazz kind
     * @return Field mapping
 */
 private Map<String, Field> buildFieldMap(Class<?> clazz) {
 Map<String, Field> fieldMap = new HashMap<>();
 for (Field field: ReflectUtil.getFields(clazz)) {
 String fieldName = field.getName();
 JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
 if (jsonProperty != null) {
 String value = jsonProperty.value();
 if (StrUtil.isNotEmpty(value) && ObjUtil.notEqual("\u0000", value)) {
 fieldName = value;
 }
 }
 fieldMap.put(fieldName, field);
 }
 return fieldMap;
 }

}
