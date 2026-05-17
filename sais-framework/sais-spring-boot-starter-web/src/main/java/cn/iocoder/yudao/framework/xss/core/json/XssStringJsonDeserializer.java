package cn.iocoder.yudao.framework.xss.core.json;

import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.xss.config.XssProperties;
import cn.iocoder.yudao.framework.xss.core.clean.XssCleaner;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS filtering jackson deserializer.
 * During the deserialization process, the string will be filtered for XSS.
 *
 * @author Hccake
 */
@Slf4j
@AllArgsConstructor
public class XssStringJsonDeserializer extends StringDeserializer {

 /**
     * property
 */
 private final XssProperties properties;
 /**
     * path matcher
 */
 private final PathMatcher pathMatcher;

 private final XssCleaner xssCleaner;

 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 1. Processing of whitelist URLs
 HttpServletRequest request = ServletUtils.getRequest();
 if (request != null) {
 String uri = ServletUtils.getRequest().getRequestURI();
 if (properties.getExcludeUrls().stream().anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, uri))) {
 return p.getText();
 }
 }

        // 2. Really use xssCleaner for filtering
 if (p.hasToken(JsonToken.VALUE_STRING)) {
 return xssCleaner.clean(p.getText());
 }
 JsonToken t = p.currentToken();
 // [databind#381]
 if (t == JsonToken.START_ARRAY) {
 return _deserializeFromArray(p, ctxt);
 }
 // need to gracefully handle byte[] data, as base64
 if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
 Object ob = p.getEmbeddedObject();
 if (ob == null) {
 return null;
 }
 if (ob instanceof byte[]) {
 return ctxt.getBase64Variant().encode((byte[]) ob, false);
 }
 // otherwise, try conversion using toString()...
 return ob.toString();
 }
 // 29-Jun-2020, tatu: New! "Scalar from Object" (mostly for XML)
 if (t == JsonToken.START_OBJECT) {
 return ctxt.extractScalarFromObject(p, this, _valueClass);
 }

 if (t.isScalarValue()) {
 String text = p.getValueAsString();
 return xssCleaner.clean(text);
 }
 return (String) ctxt.handleUnexpectedToken(_valueClass, p);
 }
}

