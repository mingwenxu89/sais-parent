package cn.iocoder.yudao.framework.common.util.json;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.json.databind.TimestampLocalDateTimeDeserializer;
import cn.iocoder.yudao.framework.common.util.json.databind.TimestampLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON tool class
 *
 * @author Yudao Source Code
 */
@Slf4j
public class JsonUtils {

 @Getter
 private static ObjectMapper objectMapper = new ObjectMapper();

 static {
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Ignore null values
        // Solve the serialization of LocalDateTime
 SimpleModule simpleModule = new JavaTimeModule()
.addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
.addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
 objectMapper.registerModules(simpleModule);
 }

 /**
     * Initialize objectMapper properties
 * <p>
     * In this way, use the ObjectMapper Bean created by Spring
 *
     * @param objectMapper ObjectMapper object
 */
 public static void init(ObjectMapper objectMapper) {
 JsonUtils.objectMapper = objectMapper;
 }

 @SneakyThrows
 public static String toJsonString(Object object) {
 return objectMapper.writeValueAsString(object);
 }

 @SneakyThrows
 public static byte[] toJsonByte(Object object) {
 return objectMapper.writeValueAsBytes(object);
 }

 @SneakyThrows
 public static String toJsonPrettyString(Object object) {
 return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
 }

 public static <T> T parseObject(String text, Class<T> clazz) {
 if (StrUtil.isEmpty(text)) {
 return null;
 }
 try {
 return objectMapper.readValue(text, clazz);
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static <T> T parseObject(String text, String path, Class<T> clazz) {
 if (StrUtil.isEmpty(text)) {
 return null;
 }
 try {
 JsonNode treeNode = objectMapper.readTree(text);
 JsonNode pathNode = treeNode.path(path);
 return objectMapper.readValue(pathNode.toString(), clazz);
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static <T> T parseObject(String text, Type type) {
 if (StrUtil.isEmpty(text)) {
 return null;
 }
 try {
 return objectMapper.readValue(text, objectMapper.getTypeFactory().constructType(type));
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static <T> T parseObject(byte[] text, Type type) {
 if (ArrayUtil.isEmpty(text)) {
 return null;
 }
 try {
 return objectMapper.readValue(text, objectMapper.getTypeFactory().constructType(type));
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 /**
     * Parses a string into an object of the specified type
     * When using {@link #parseObject(String, Class)}, in the scenario of @JSONTypeInfo(use = JSONTypeInfo.Id.CLASS),
     * If text does not have a class attribute, an error will be reported. At this time, using this method can solve the problem.
 *
     * @param text string
     * @param clazz type
     * @return object
 */
 public static <T> T parseObject2(String text, Class<T> clazz) {
 if (StrUtil.isEmpty(text)) {
 return null;
 }
 return JSONUtil.toBean(text, clazz);
 }

 public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
 if (ArrayUtil.isEmpty(bytes)) {
 return null;
 }
 try {
 return objectMapper.readValue(bytes, clazz);
 } catch (IOException e) {
 log.error("json parse err,json:{}", bytes, e);
 throw new RuntimeException(e);
 }
 }

 public static <T> T parseObject(String text, TypeReference<T> typeReference) {
 try {
 return objectMapper.readValue(text, typeReference);
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 /**
     * Parses a JSON string into an object of the specified type. If parsing fails, null is returned.
 *
     * @param text string
     * @param typeReference type reference
     * @return object of specified type
 */
 public static <T> T parseObjectQuietly(String text, TypeReference<T> typeReference) {
 try {
 return objectMapper.readValue(text, typeReference);
 } catch (IOException e) {
 return null;
 }
 }

 public static <T> List<T> parseArray(String text, Class<T> clazz) {
 if (StrUtil.isEmpty(text)) {
 return new ArrayList<>();
 }
 try {
 return objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static <T> List<T> parseArray(String text, String path, Class<T> clazz) {
 if (StrUtil.isEmpty(text)) {
 return null;
 }
 try {
 JsonNode treeNode = objectMapper.readTree(text);
 JsonNode pathNode = treeNode.path(path);
 return objectMapper.readValue(pathNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static JsonNode parseTree(String text) {
 try {
 return objectMapper.readTree(text);
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static JsonNode parseTree(byte[] text) {
 try {
 return objectMapper.readTree(text);
 } catch (IOException e) {
 log.error("json parse err,json:{}", text, e);
 throw new RuntimeException(e);
 }
 }

 public static boolean isJson(String text) {
 return JSONUtil.isTypeJSON(text);
 }

 /**
     * Determine whether a string is a JSON type string
     * @param str string
 */
 public static boolean isJsonObject(String str) {
 return JSONUtil.isTypeJSONObject(str);
 }

 /**
     * Convert Object to target type
 * <p>
     * AvoID the performance loss of converting JSONString first and then parseObject
 *
     * @param obj Source object (can be Map, POJO, etc.)
     * @param clazz target type
     * @return converted object
 */
 public static <T> T convertObject(Object obj, Class<T> clazz) {
 if (obj == null) {
 return null;
 }
 if (clazz.isInstance(obj)) {
 return clazz.cast(obj);
 }
 return objectMapper.convertValue(obj, clazz);
 }

 /**
     * Convert Object to target type (supports generics)
 *
     * @param obj source object
     * @param typeReference target type reference
     * @return converted object
 */
 public static <T> T convertObject(Object obj, TypeReference<T> typeReference) {
 if (obj == null) {
 return null;
 }
 return objectMapper.convertValue(obj, typeReference);
 }

 /**
     * Convert Object to List type
 * <p>
     * AvoID the performance loss of converting JSONString first and then parseArray
 *
     * @param obj Source object (can be List, array, etc.)
     * @param clazz target element type
     * @return Converted List
 */
 public static <T> List<T> convertList(Object obj, Class<T> clazz) {
 if (obj == null) {
 return new ArrayList<>();
 }
 return objectMapper.convertValue(obj, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
 }

}
