package cn.iocoder.yudao.framework.jackson.config;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.json.databind.NumberSerializer;
import cn.iocoder.yudao.framework.common.util.json.databind.TimestampLocalDateTimeDeserializer;
import cn.iocoder.yudao.framework.common.util.json.databind.TimestampLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AutoConfiguration(after = JacksonAutoConfiguration.class)
@Slf4j
public class YudaoJacksonAutoConfiguration {

 /**
     * Customize from Builder source (key: use *ByType, avoID handledType requirement)
 */
 @Bean
 public Jackson2ObjectMapperBuilderCustomizer ldtEpochMillisCustomizer() {
 return builder -> builder
 // Long -> Number
.serializerByType(Long.class, NumberSerializer.INSTANCE)
.serializerByType(Long.TYPE, NumberSerializer.INSTANCE)
 // LocalDate / LocalTime
.serializerByType(LocalDate.class, LocalDateSerializer.INSTANCE)
.deserializerByType(LocalDate.class, LocalDateDeserializer.INSTANCE)
.serializerByType(LocalTime.class, LocalTimeSerializer.INSTANCE)
.deserializerByType(LocalTime.class, LocalTimeDeserializer.INSTANCE)
 // LocalDateTime < - > EpochMillis
.serializerByType(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
.deserializerByType(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
 }

 /**
     * Expose Module as a Bean (Boot will automatically register with all ObjectMappers)
 */
 @Bean
 public Module timestampSupportModuleBean() {
 SimpleModule m = new SimpleModule("TimestampSupportModule");
        // Long -> Number to avoID loss of front-end precision
 m.addSerializer(Long.class, NumberSerializer.INSTANCE);
 m.addSerializer(Long.TYPE, NumberSerializer.INSTANCE);
 // LocalDate / LocalTime
 m.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
 m.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
 m.addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
 m.addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
 // LocalDateTime < - > EpochMillis
 m.addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE);
 m.addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
 return m;
 }

 /**
     * Initialize global JSONUtils and use the main ObjectMapper directly
 */
 @Bean
 @SuppressWarnings("InstantiationOfUtilityClass")
 public JsonUtils jsonUtils(ObjectMapper objectMapper) {
 JsonUtils.init(objectMapper);
        log.debug("[init][Initialization of JSONUtils successful]");
 return new JsonUtils();
 }

}
