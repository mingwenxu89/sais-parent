package cn.iocoder.yudao.framework.mybatis.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.handler.DefaultDBFieldHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.IJsonTypeHandler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.incrementer.*;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.parser.cache.JdkSerialCaffeineJsqlParseCache;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MyBaits configuration class
 *
 * @author Yudao Source Code
 */
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class) // Purpose: Automatically configure MyBatis Plus before @MapperScan may fail to scan Mapper and print warn logs
@MapperScan(value = "${yudao.info.base-package}", annotationClass = Mapper.class,
        lazyInitialization = "${mybatis.lazy-initialization:false}") // Mapper lazy loading, currently only used for unit testing
public class YudaoMybatisAutoConfiguration {

 static {
        // Dynamic SQL intelligent optimization supports local cache to accelerate parsing, more complete tenant complex XML dynamic SQL support, and static injection cache
 JsqlParserGlobal.setJsqlParseCache(new JdkSerialCaffeineJsqlParseCache(
 (cache) -> cache.maximumSize(1024)
.expireAfterWrite(5, TimeUnit.SECONDS))
 );
 }

 @Bean
 public MybatisPlusInterceptor mybatisPlusInterceptor() {
 MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // Pagination plugin
        // ↓↓↓ Enable on demand, which may affect updateBatch: for example, file configuration management ↓↓↓
        // mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor()); // Intercept update and delete statements without specified conditions
 return mybatisPlusInterceptor;
 }

 @Bean
 public MetaObjectHandler defaultMetaObjectHandler() {
        return new DefaultDBFieldHandler(); // Autofill parameter class
 }

 @Bean
 @ConditionalOnProperty(prefix = "mybatis-plus.global-config.db-config", name = "id-type", havingValue = "INPUT")
 public IKeyGenerator keyGenerator(ConfigurableEnvironment environment) {
 DbType dbType = IdTypeEnvironmentPostProcessor.getDbType(environment);
 if (dbType != null) {
 switch (dbType) {
 case POSTGRE_SQL:
 return new PostgreKeyGenerator();
 case ORACLE:
 case ORACLE_12C:
 return new OracleKeyGenerator();
 case H2:
 return new H2KeyGenerator();
 case KINGBASE_ES:
 return new KingbaseKeyGenerator();
 case DM:
 return new DmKeyGenerator();
 }
 }
        // No suitable IKeyGenerator implementation class found
        throw new IllegalArgumentException(StrUtil.format("DBType{} cannot find a suitable IKeyGenerator implementation class", dbType));
 }

    @Bean // Special: The reason why the returned result is Object instead of JacksonTypeHandler is to avoID JacksonTypeHandler being used globally by mybatis!
 public Object jacksonTypeHandler(List<ObjectMapper> objectMappers) {
        // Special: Set JacksonTypeHandler's ObjectMapper!
 ObjectMapper objectMapper = CollUtil.getFirst(objectMappers);
 if (objectMapper == null) {
 objectMapper = JsonUtils.getObjectMapper();
 }
 JacksonTypeHandler.setObjectMapper(objectMapper);
 return new JacksonTypeHandler(Object.class);
 }

}
