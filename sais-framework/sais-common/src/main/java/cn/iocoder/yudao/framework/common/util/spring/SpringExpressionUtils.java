package cn.iocoder.yudao.framework.common.util.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Utility classes for Spring EL expressions
 *
 * @author mashu
 */
public class SpringExpressionUtils {

 /**
     * Spring EL expression parser
 */
 private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
 /**
     * Parameter name finder
 */
 private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

 private SpringExpressionUtils() {
 }

 /**
     * Result of a single parsed EL expression from an aspect
 *
     * @param joinPoint Cutting point
     * @param expressionString EL expression array
     * @return Execution interface
 */
 public static Object parseExpression(JoinPoint joinPoint, String expressionString) {
 Map<String, Object> result = parseExpressions(joinPoint, Collections.singletonList(expressionString));
 return result.get(expressionString);
 }

 /**
     * Batch parse the results of EL expressions from aspects
 *
     * @param joinPoint Cutting point
     * @param expressionStrings EL expression array
     * @return As a result, key is the expression and value is the corresponding value.
 */
 public static Map<String, Object> parseExpressions(JoinPoint joinPoint, List<String> expressionStrings) {
        // If empty, no parsing is performed
 if (CollUtil.isEmpty(expressionStrings)) {
 return MapUtil.newHashMap();
 }

        // The first step is to build the parsed context EvaluationContext
        // Get the annotated method through joinPoint
 MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
 Method method = methodSignature.getMethod();
        // Use spring's ParameterNameDiscoverer to obtain the method parameter name array
 String[] paramNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        // Spring’s expression context object
 EvaluationContext context = new StandardEvaluationContext();
        // Assign a value to the context
 if (ArrayUtil.isNotEmpty(paramNames)) {
 Object[] args = joinPoint.getArgs();
 for (int i = 0; i < paramNames.length; i++) {
 context.setVariable(paramNames[i], args[i]);
 }
 }

        // The second step is to analyze parameters one by one.
 Map<String, Object> result = MapUtil.newHashMap(expressionStrings.size(), true);
 expressionStrings.forEach(key -> {
 Object value = EXPRESSION_PARSER.parseExpression(key).getValue(context);
 result.put(key, value);
 });
 return result;
 }

 /**
     * From the bean factory, parse the result of the EL expression
 *
     * @param expressionString EL expression
     * @return Execution interface
 */
 public static Object parseExpression(String expressionString) {
 return parseExpression(expressionString, null);
 }

 /**
     * From the bean factory, parse the result of the EL expression
 *
     * @param expressionString EL expression
     * @param variables variable
     * @return Execution interface
 */
 public static Object parseExpression(String expressionString, Map<String, Object> variables) {
 if (StrUtil.isBlank(expressionString)) {
 return null;
 }
 Expression expression = EXPRESSION_PARSER.parseExpression(expressionString);
 StandardEvaluationContext context = new StandardEvaluationContext();
 context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getApplicationContext()));
 if (MapUtil.isNotEmpty(variables)) {
 context.setVariables(variables);
 }
 return expression.getValue(context);
 }

}
