package cn.iocoder.yudao.framework.common.util.json.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Timestamp-based LocalDateTime deserializer
 *
 * @author Lao Wu
 */
public class TimestampLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

 public static final TimestampLocalDateTimeDeserializer INSTANCE = new TimestampLocalDateTimeDeserializer();

 @Override
 public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Convert Long timestamp to LocalDateTime object
 return LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getValueAsLong()), ZoneId.systemDefault());
 }

}
