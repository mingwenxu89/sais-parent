package cn.iocoder.yudao.framework.encrypt.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricDecryptor;
import cn.hutool.crypto.asymmetric.AsymmetricEncryptor;
import cn.hutool.crypto.symmetric.SymmetricDecryptor;
import cn.hutool.crypto.symmetric.SymmetricEncryptor;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.encrypt.config.ApiEncryptProperties;
import cn.iocoder.yudao.framework.encrypt.core.annotation.ApiEncrypt;
import cn.iocoder.yudao.framework.web.config.WebProperties;
import cn.iocoder.yudao.framework.web.core.filter.ApiRequestFilter;
import cn.iocoder.yudao.framework.web.core.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.invalidParamException;

/**
 * API encryption filter, handling {@link APIEncrypt} annotation.
 *
 * 1. Decrypt request parameters
 * 2. Encrypted response results
 *
 * Question: Why not use SpringMVC’s RequestBodyAdvice or ResponseBodyAdvice mechanism?
 * Answer: Considering that the project will record access logs, exception logs, and HTTP API signatures, it is best to do global level analysis and analyze it in advance! ! !
 *
 * @author Yudao Source Code
 */
@Slf4j
public class ApiEncryptFilter extends ApiRequestFilter {

 private final ApiEncryptProperties apiEncryptProperties;

 private final RequestMappingHandlerMapping requestMappingHandlerMapping;

 private final GlobalExceptionHandler globalExceptionHandler;

 private final SymmetricDecryptor requestSymmetricDecryptor;
 private final AsymmetricDecryptor requestAsymmetricDecryptor;

 private final SymmetricEncryptor responseSymmetricEncryptor;
 private final AsymmetricEncryptor responseAsymmetricEncryptor;

 public ApiEncryptFilter(WebProperties webProperties,
 ApiEncryptProperties apiEncryptProperties,
 RequestMappingHandlerMapping requestMappingHandlerMapping,
 GlobalExceptionHandler globalExceptionHandler) {
 super(webProperties);
 this.apiEncryptProperties = apiEncryptProperties;
 this.requestMappingHandlerMapping = requestMappingHandlerMapping;
 this.globalExceptionHandler = globalExceptionHandler;
 if (StrUtil.equalsIgnoreCase(apiEncryptProperties.getAlgorithm(), "AES")) {
 this.requestSymmetricDecryptor = SecureUtil.aes(StrUtil.utf8Bytes(apiEncryptProperties.getRequestKey()));
 this.requestAsymmetricDecryptor = null;
 this.responseSymmetricEncryptor = SecureUtil.aes(StrUtil.utf8Bytes(apiEncryptProperties.getResponseKey()));
 this.responseAsymmetricEncryptor = null;
 } else if (StrUtil.equalsIgnoreCase(apiEncryptProperties.getAlgorithm(), "RSA")) {
 this.requestSymmetricDecryptor = null;
 this.requestAsymmetricDecryptor = SecureUtil.rsa(apiEncryptProperties.getRequestKey(), null);
 this.responseSymmetricEncryptor = null;
 this.responseAsymmetricEncryptor = SecureUtil.rsa(null, apiEncryptProperties.getResponseKey());
 } else {
            // Additional note: If you want to support algorithms such as SM2 and SM4, you can create the corresponding instance here and add the corresponding Maven dependency.
            throw new IllegalArgumentException("Unsupported encryption algorithms:" + apiEncryptProperties.getAlgorithm());
 }
 }

 @Override
 @SuppressWarnings("NullableProblems")
 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
 throws ServletException, IOException {
        // Get @APIEncrypt annotation
 ApiEncrypt apiEncrypt = getApiEncrypt(request);
 boolean requestEnable = apiEncrypt != null && apiEncrypt.request();
 boolean responseEnable = apiEncrypt != null && apiEncrypt.response();
 String encryptHeader = request.getHeader(apiEncryptProperties.getHeader());
 if (!requestEnable && !responseEnable && StrUtil.isBlank(encryptHeader)) {
 chain.doFilter(request, response);
 return;
 }

        // 1. Decryption request
 if (ObjectUtils.equalsAny(HttpMethod.valueOf(request.getMethod()),
 HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)) {
 try {
 if (StrUtil.isNotBlank(encryptHeader)) {
 request = new ApiDecryptRequestWrapper(request,
 requestSymmetricDecryptor, requestAsymmetricDecryptor);
 } else if (requestEnable) {
                    throw invalidParamException("The request dID not include encryption headers, please check if the encryption headers are configured correctly");
 }
 } catch (Exception ex) {
 CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
 ServletUtils.writeJSON(response, result);
 return;
 }
 }

        // 2. Execute the filter chain
 if (responseEnable) {
            // Special: Wrapper only, executed last. Purpose: Response content can be read repeatedly! ! !
 response = new ApiEncryptResponseWrapper(response);
 }
 chain.doFilter(request, response);

        // 3. Encrypted response (real execution)
 if (responseEnable) {
 ((ApiEncryptResponseWrapper) response).encrypt(apiEncryptProperties,
 responseSymmetricEncryptor, responseAsymmetricEncryptor);
 }
 }

 /**
     * Get @APIEncrypt annotation
 *
     * @param request ask
 */
 @SuppressWarnings("PatternVariableCanBeUsed")
 private ApiEncrypt getApiEncrypt(HttpServletRequest request) {
 try {
            // Special: Compatible with SpringBoot 2.X version that will report an error https://t.zsxq.com/kqyiB
 if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
 ServletRequestPathUtils.parseAndCache(request);
 }

            // Parse @APIEncrypt annotation
 HandlerExecutionChain mappingHandler = requestMappingHandlerMapping.getHandler(request);
 if (mappingHandler == null) {
 return null;
 }
 Object handler = mappingHandler.getHandler();
 if (handler instanceof HandlerMethod) {
 HandlerMethod handlerMethod = (HandlerMethod) handler;
 ApiEncrypt annotation = handlerMethod.getMethodAnnotation(ApiEncrypt.class);
 if (annotation == null) {
 annotation = handlerMethod.getBeanType().getAnnotation(ApiEncrypt.class);
 }
 return annotation;
 }
 } catch (Exception e) {
            log.error("[getAPIEncrypt][url({}/{}) failed to obtain @APIEncrypt annotation]",
 request.getRequestURI(), request.getMethod(), e);
 }
 return null;
 }

}
