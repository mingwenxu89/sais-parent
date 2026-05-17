package cn.iocoder.yudao.module.infra.service.codegen.inner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import cn.hutool.system.SystemUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.common.util.string.StrUtils;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenTableDO;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenFrontTypeEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenSceneEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenTemplateTypeEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenVOTypeEnum;
import cn.iocoder.yudao.module.infra.framework.codegen.config.CodegenProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.*;

import static cn.hutool.core.map.MapUtil.getStr;
import static cn.hutool.core.text.CharSequenceUtil.*;

/**
 * Code generation engine, used to specifically generate code
 * Currently implemented based on {@link org.apache.velocity.app.Velocity} template engine
 *
 * Considering that there are many frameworks for Java template engines, such as Freemarker, Velocity, Thymeleaf, etc., we use the {@link cn.hutool.extra.template.Template} abstraction encapsulated by hutool
 *
 * @author Yudao Source Code
 */
@Component
public class CodegenEngine {

    /**
     * Backend template configuration
     *
     * key: the address of the template in resources
     * value: generated path
     */
    private static final Map<String, String> SERVER_TEMPLATES = MapUtil.<String, String>builder(new LinkedHashMap<>()) // orderly
            // Java module-biz(server) Main
            .put(javaTemplatePath("controller/vo/pageReqVO"), javaModuleImplVOFilePath("PageReqVO"))
            .put(javaTemplatePath("controller/vo/listReqVO"), javaModuleImplVOFilePath("ListReqVO"))
            .put(javaTemplatePath("controller/vo/respVO"), javaModuleImplVOFilePath("RespVO"))
            .put(javaTemplatePath("controller/vo/saveReqVO"), javaModuleImplVOFilePath("SaveReqVO"))
            .put(javaTemplatePath("controller/controller"), javaModuleImplControllerFilePath())
            .put(javaTemplatePath("dal/do"),
                    javaModuleImplMainFilePath("dal/dataobject/${table.businessName}/${table.className}DO"))
            .put(javaTemplatePath("dal/do_sub"), // Special: Exclusive logic for master and sub-tables
                    javaModuleImplMainFilePath("dal/dataobject/${table.businessName}/${subTable.className}DO"))
            .put(javaTemplatePath("dal/mapper"),
                    javaModuleImplMainFilePath("dal/mysql/${table.businessName}/${table.className}Mapper"))
            .put(javaTemplatePath("dal/mapper_sub"), // Special: Exclusive logic for master and sub-tables
                    javaModuleImplMainFilePath("dal/mysql/${table.businessName}/${subTable.className}Mapper"))
            .put(javaTemplatePath("dal/mapper.xml"), mapperXmlFilePath())
            .put(javaTemplatePath("service/serviceImpl"),
                    javaModuleImplMainFilePath("service/${table.businessName}/${table.className}ServiceImpl"))
            .put(javaTemplatePath("service/service"),
                    javaModuleImplMainFilePath("service/${table.businessName}/${table.className}Service"))
            // Java module-biz(server) Test
            .put(javaTemplatePath("test/serviceTest"),
                    javaModuleImplTestFilePath("service/${table.businessName}/${table.className}ServiceImplTest"))
            // Java module-api Main
            .put(javaTemplatePath("enums/errorcode"), javaModuleApiMainFilePath("enums/ErrorCodeConstants_Manual operation"))
            // SQL
            .put("codegen/sql/sql.vm", "sql/sql.sql")
            .put("codegen/sql/h2.vm", "sql/h2.sql")
            .build();

    /**
     * Front-end configuration template
     *
     * key1: Type of UI template {@link CodegenFrontTypeEnum#getType()}
     * key2: The address of the template in resources
     * value: generated path
     */
    private static final Table<Integer, String, String> FRONT_TEMPLATES = ImmutableTable.<Integer, String, String>builder()
            // VUE2_ELEMENT_UI
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/index.vue"),
                    vueFilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("api/api.js"),
                    vueFilePath("api/${table.moduleName}/${table.businessName}/index.js"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/form.vue"),
                    vueFilePath("views/${table.moduleName}/${table.businessName}/${simpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/components/form_sub_normal.vue"),  // Special: Exclusive logic for master and sub-tables
                    vueFilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/components/form_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vueFilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/components/form_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vueFilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/components/list_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vueFilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}List.vue"))
            .put(CodegenFrontTypeEnum.VUE2_ELEMENT_UI.getType(), vueTemplatePath("views/components/list_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vueFilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}List.vue"))
            // VUE3_ELEMENT_PLUS
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/index.vue"),
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/form.vue"),
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/${simpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/components/form_sub_normal.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/components/form_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/components/form_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}Form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/components/list_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}List.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("views/components/list_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3FilePath("views/${table.moduleName}/${table.businessName}/components/${subSimpleClassName}List.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ELEMENT_PLUS.getType(), vue3TemplatePath("api/api.ts"),
                    vue3FilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            .put(CodegenFrontTypeEnum.VUE3_ADMIN_UNIAPP_WOT.getType(), vue3AdminUniappTemplatePath("api/api.ts"),
                    vue3UniappFilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            .put(CodegenFrontTypeEnum.VUE3_ADMIN_UNIAPP_WOT.getType(), vue3AdminUniappTemplatePath("views/index.vue"),
                    vue3UniappFilePath("pages-${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ADMIN_UNIAPP_WOT.getType(), vue3AdminUniappTemplatePath("components/search-form.vue"),
                    vue3UniappFilePath("pages-${table.moduleName}/${table.businessName}/components/search-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ADMIN_UNIAPP_WOT.getType(), vue3AdminUniappTemplatePath("views/form/index.vue"),
                    vue3UniappFilePath("pages-${table.moduleName}/${table.businessName}/form/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_ADMIN_UNIAPP_WOT.getType(), vue3AdminUniappTemplatePath("views/detail/index.vue"),
                    vue3UniappFilePath("pages-${table.moduleName}/${table.businessName}/detail/index.vue"))
            // VUE3_VBEN2_ANTD_SCHEMA
            .put(CodegenFrontTypeEnum.VUE3_VBEN2_ANTD_SCHEMA.getType(), vue3VbenTemplatePath("views/data.ts"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/${classNameVar}.data.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN2_ANTD_SCHEMA.getType(), vue3VbenTemplatePath("views/index.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN2_ANTD_SCHEMA.getType(), vue3VbenTemplatePath("views/form.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/${simpleClassName}Modal.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN2_ANTD_SCHEMA.getType(), vue3VbenTemplatePath("api/api.ts"),
                    vue3VbenFilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            // VUE3_VBEN5_ANTD_SCHEMA
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/data.ts"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/data.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/index.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/form.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("api/api.ts"),
                    vue3VbenFilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/modules/form_sub_normal.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/modules/form_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/modules/form_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/modules/list_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_SCHEMA.getType(), vue3Vben5AntdSchemaTemplatePath("views/modules/list_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            // VUE3_VBEN5_ANTD
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/index.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/form.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("api/api.ts"),
                    vue3VbenFilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/modules/form_sub_normal.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/modules/form_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/modules/form_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/modules/list_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_ANTD_GENERAL.getType(), vue3Vben5AntdGeneralTemplatePath("views/modules/list_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            // VUE3_VBEN5_EP_SCHEMA
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/data.ts"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/data.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/index.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/form.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("api/api.ts"),
                    vue3VbenFilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/modules/form_sub_normal.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/modules/form_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/modules/form_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/modules/list_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_SCHEMA.getType(), vue3Vben5EpSchemaTemplatePath("views/modules/list_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            // VUE3_VBEN5_EP
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/index.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/index.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/form.vue"),
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("api/api.ts"),
                    vue3VbenFilePath("api/${table.moduleName}/${table.businessName}/index.ts"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/modules/form_sub_normal.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/modules/form_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/modules/form_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-form.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/modules/list_sub_inner.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            .put(CodegenFrontTypeEnum.VUE3_VBEN5_EP_GENERAL.getType(), vue3Vben5EpGeneralTemplatePath("views/modules/list_sub_erp.vue"),  // Special: Exclusive logic for master and sub-tables
                    vue3VbenFilePath("views/${table.moduleName}/${table.businessName}/modules/${subSimpleClassName_strikeCase}-list.vue"))
            .build();

    @Resource
    private CodegenProperties codegenProperties;

    /**
     * Whether to use the jakarta package to solve the compatibility issues of Spring Boot 2.X and 3.X
     *
     * true - uses jakarta.validation.constraints.*
     * false - use javax.validation.constraints.*
     */
    @Setter // The reason why settings are allowed is because single tests need to be changed manually.
    private Boolean jakartaEnable;

    /**
     * Whether it is a sar-cloud project, used to solve Boot and Cloud API module compatibility issues
     *
     * true - requires sar-module-xxx-API module
     * false - No need to have it, just use API and enum packages
     */
    @Setter
    private Boolean cloudEnable;

    /**
     * Template engine, implemented by hutool
     */
    private final TemplateEngine templateEngine;
    /**
     * Global universal variable mapping
     */
    private final Map<String, Object> globalBindingMap = new HashMap<>();

    public CodegenEngine() {
        // Initialize TemplateEngine properties
        TemplateConfig config = new TemplateConfig();
        config.setResourceMode(TemplateConfig.ResourceMode.CLASSPATH);
        this.templateEngine = new VelocityEngine(config);
        // Set javaxEnable to determine whether to use JDK17
        this.jakartaEnable = SystemUtil.getJavaInfo().isJavaVersionAtLeast(1700) // 17.00 * 100
                && ClassUtils.isPresent("jakarta.annotation.Resource", ClassUtils.getDefaultClassLoader());
        // Set cloudEnable to determine whether to use Spring Cloud
        this.cloudEnable = ClassUtils.isPresent("cn.iocoder.yudao.module.infra.framework.rpc.config.RpcConfiguration",
                ClassUtils.getDefaultClassLoader());
    }

    @PostConstruct
    @VisibleForTesting
    void initGlobalBindingMap() {
        // Global configuration
        globalBindingMap.put("basePackage", codegenProperties.getBasePackage());
        globalBindingMap.put("baseFrameworkPackage", codegenProperties.getBasePackage()
                + '.' + "framework"); // Used to subsequently obtain the package address of the test class
        globalBindingMap.put("jakartaPackage", jakartaEnable ? "jakarta" : "javax");
        globalBindingMap.put("voType", codegenProperties.getVoType());
        globalBindingMap.put("deleteBatchEnable", codegenProperties.getDeleteBatchEnable());
        // Global Java Beans
        globalBindingMap.put("CommonResultClassName", CommonResult.class.getName());
        globalBindingMap.put("PageResultClassName", PageResult.class.getName());
        // VO class, unique fields
        globalBindingMap.put("PageParamClassName", PageParam.class.getName());
        globalBindingMap.put("DictFormatClassName", DictFormat.class.getName());
        // DO class, unique fields
        globalBindingMap.put("BaseDOClassName", BaseDO.class.getName());
        globalBindingMap.put("baseDOFields", CodegenBuilder.BASE_DO_FIELDS);
        globalBindingMap.put("QueryWrapperClassName", LambdaQueryWrapperX.class.getName());
        globalBindingMap.put("BaseMapperClassName", BaseMapperX.class.getName());
        // Util tool class
        globalBindingMap.put("ServiceExceptionUtilClassName", ServiceExceptionUtil.class.getName());
        globalBindingMap.put("DateUtilsClassName", DateUtils.class.getName());
        globalBindingMap.put("ExcelUtilsClassName", ExcelUtils.class.getName());
        globalBindingMap.put("LocalDateTimeUtilsClassName", LocalDateTimeUtils.class.getName());
        globalBindingMap.put("ObjectUtilsClassName", ObjectUtils.class.getName());
        globalBindingMap.put("DictConvertClassName", DictConvert.class.getName());
        globalBindingMap.put("ApiAccessLogClassName", ApiAccessLog.class.getName());
        globalBindingMap.put("OperateTypeEnumClassName", OperateTypeEnum.class.getName());
        globalBindingMap.put("BeanUtils", BeanUtils.class.getName());
        globalBindingMap.put("CollectionUtilsClassName", CollectionUtils.class.getName());
    }

    /**
     * Generate code
     *
     * @param dbType         Database type
     * @param table          table definition
     * @param columns        field definition array of table
     * @param subTables      Subtable array, used when and only when the main subtable
     * @param subColumnsList Array of field definitions for subTables
     * @return Generated code, key is the path, value is the corresponding code
     */
    public Map<String, String> execute(DbType dbType, CodegenTableDO table, List<CodegenColumnDO> columns,
                                       List<CodegenTableDO> subTables, List<List<CodegenColumnDO>> subColumnsList) {
        // 1.1 Initialize bindMap context
        Map<String, Object> bindingMap = initBindingMap(dbType, table, columns, subTables, subColumnsList);
        // 1.2 Obtain templates
        Map<String, String> templates = getTemplates(table.getFrontType());

        // 2. Execute the build
        Map<String, String> result = Maps.newLinkedHashMapWithExpectedSize(templates.size()); // orderly
        templates.forEach((vmPath, filePath) -> {
            // 2.1 Special: Exclusive logic for master and sub-tables
            if (isSubTemplate(vmPath)) {
                generateSubCode(table, subTables, result, vmPath, filePath, bindingMap);
                return;
                // 2.2 Special: tree table exclusive logic
            } else if (isPageReqVOTemplate(vmPath)) {
                // Reduce redundant class generation, such as the PageVO.Java class
                if (CodegenTemplateTypeEnum.isTree(table.getTemplateType())) {
                    return;
                }
            } else if (isListReqVOTemplate(vmPath)) {
                // Reduce redundant class generation, such as the ListVO.Java class
                if (!CodegenTemplateTypeEnum.isTree(table.getTemplateType())) {
                    return;
                }
            }
            // 2.3 Default generation
            generateCode(result, vmPath, filePath, bindingMap);
        });
        return result;
    }

    private void generateCode(Map<String, String> result, String vmPath,
                              String filePath, Map<String, Object> bindingMap) {
        filePath = formatFilePath(filePath, bindingMap);
        String content = templateEngine.getTemplate(vmPath).render(bindingMap);
        // Format code
        content = prettyCode(content, vmPath);
        result.put(filePath, content);
    }

    private void generateSubCode(CodegenTableDO table, List<CodegenTableDO> subTables,
                                 Map<String, String> result, String vmPath,
                                 String filePath, Map<String, Object> bindingMap) {
        // There is no subtable, so it is not generated
        if (CollUtil.isEmpty(subTables)) {
            return;
        }
        // Pattern matching for master and child tables. Purpose: Filter out personalized templates
        if (vmPath.contains("_normal")
                && ObjectUtil.notEqual(table.getTemplateType(), CodegenTemplateTypeEnum.MASTER_NORMAL.getType())) {
            return;
        }
        if (vmPath.contains("_erp")
                && ObjectUtil.notEqual(table.getTemplateType(), CodegenTemplateTypeEnum.MASTER_ERP.getType())) {
            return;
        }
        if (vmPath.contains("_inner")
                && ObjectUtil.notEqual(table.getTemplateType(), CodegenTemplateTypeEnum.MASTER_INNER.getType())) {
            return;
        }

        // Generate one by one
        for (int i = 0; i < subTables.size(); i++) {
            bindingMap.put("subIndex", i);
            generateCode(result, vmPath, filePath, bindingMap);
        }
        bindingMap.remove("subIndex");
    }

    /**
     * Format generated code
     *
     * Because we try to keep the vm template as simple as possible, all unified processing is done in this method.
     * If not processed, Vue's Pretty format verification may report an error.
     *
     * @param content Code before formatting
     * @param vmPath template path
     * @return Formatted code
     */
    private String prettyCode(String content, String vmPath) {
        // Vue API: remove the extra , comma after the field to solve the error in the frontend Pretty code format check (need to exclude vben5, vue3_admin_uniapp)
        if (!StrUtil.containsAny(vmPath, "vben5", "vue3_admin_uniapp")) {
            content = content.replaceAll(",\n}", "\n}").replaceAll(",\n  }", "\n  }");
        }
        // Vue API: Remove multiple dateFormatters. If there is only one, it means it is not used.
        if (StrUtil.count(content, "dateFormatter") == 1) {
            content = StrUtils.removeLineContains(content, "dateFormatter");
        }
        // Vue2 API: fix $refs
        if (StrUtil.count(content, "this.refs") >= 1) {
            content = content.replace("this.refs", "this.$refs");
        }
        // Vue API: Remove multiple dict related ones. If there is only one, it means it is not used.
        if (StrUtil.count(content, "getIntDictOptions") == 1) {
            content = content.replace("getIntDictOptions, ", "");
        }
        if (StrUtil.count(content, "getStrDictOptions") == 1) {
            content = content.replace("getStrDictOptions, ", "");
        }
        if (StrUtil.count(content, "getBoolDictOptions") == 1) {
            content = content.replace("getBoolDictOptions, ", "");
        }
        if (StrUtil.count(content, "DICT_TYPE.") == 0) {
            content = StrUtils.removeLineContains(content, "DICT_TYPE");
        }
        return content;
    }

    private Map<String, Object> initBindingMap(DbType dbType, CodegenTableDO table, List<CodegenColumnDO> columns,
                                               List<CodegenTableDO> subTables, List<List<CodegenColumnDO>> subColumnsList) {
        // Create bindingMap
        Map<String, Object> bindingMap = new HashMap<>(globalBindingMap);
        bindingMap.put("dbType", dbType);
        bindingMap.put("table", table);
        bindingMap.put("columns", columns);
        bindingMap.put("primaryColumn", CollectionUtils.findFirst(columns, CodegenColumnDO::getPrimaryKey)); // primary key field
        bindingMap.put("sceneEnum", CodegenSceneEnum.valueOf(table.getScene()));
        // className related
        // Remove the specified prefix and convert TestDictType to DictType. Because there is no need to bring the Test prefix after create and other methods.
        String className = table.getClassName();
        String simpleClassName = equalsAnyIgnoreCase(table.getClassName(), table.getModuleName()) ? table.getClassName()
                : removePrefix(table.getClassName(), upperFirst(table.getModuleName()));
        String classNameVar = lowerFirst(simpleClassName);
        bindingMap.put("simpleClassName", simpleClassName);
        bindingMap.put("simpleClassName_underlineCase", toUnderlineCase(simpleClassName)); // Convert DictType to dict_type
        bindingMap.put("classNameVar", classNameVar); // Convert DictType to dictType for use with variables
        // Convert DictType to dict-type
        String simpleClassNameStrikeCase = toSymbolCase(simpleClassName, '-');
        bindingMap.put("simpleClassName_strikeCase", simpleClassNameStrikeCase);
        // permission prefix
        bindingMap.put("permissionPrefix", table.getModuleName() + ":" + simpleClassNameStrikeCase);

        // Special: tree table exclusive logic
        if (CodegenTemplateTypeEnum.isTree(table.getTemplateType())) {
            CodegenColumnDO treeParentColumn = CollUtil.findOne(columns,
                    column -> Objects.equals(column.getId(), table.getTreeParentColumnId()));
            bindingMap.put("treeParentColumn", treeParentColumn);
            bindingMap.put("treeParentColumn_javaField_underlineCase", toUnderlineCase(treeParentColumn.getJavaField()));
            CodegenColumnDO treeNameColumn = CollUtil.findOne(columns,
                    column -> Objects.equals(column.getId(), table.getTreeNameColumnId()));
            bindingMap.put("treeNameColumn", treeNameColumn);
            bindingMap.put("treeNameColumn_javaField_underlineCase", toUnderlineCase(treeNameColumn.getJavaField()));
        }

        // Special: Exclusive logic for master and sub-tables
        if (CollUtil.isNotEmpty(subTables)) {
            // Create bindingMap
            bindingMap.put("subTables", subTables);
            bindingMap.put("subColumnsList", subColumnsList);
            List<CodegenColumnDO> subPrimaryColumns = new ArrayList<>();
            List<CodegenColumnDO> subJoinColumns = new ArrayList<>();
            List<String> subJoinColumnStrikeCases = new ArrayList<>();
            List<String> subSimpleClassNames = new ArrayList<>();
            List<String> subClassNameVars = new ArrayList<>();
            List<String> simpleClassNameUnderlineCases = new ArrayList<>();
            List<String> subSimpleClassNameStrikeCases = new ArrayList<>();
            for (int i = 0; i < subTables.size(); i++) {
                CodegenTableDO subTable = subTables.get(i);
                List<CodegenColumnDO> subColumns = subColumnsList.get(i);
                subPrimaryColumns.add(CollectionUtils.findFirst(subColumns, CodegenColumnDO::getPrimaryKey)); //
                CodegenColumnDO subColumn = CollectionUtils.findFirst(subColumns, // associated fields
                        column -> Objects.equals(column.getId(), subTable.getSubJoinColumnId()));
                subJoinColumns.add(subColumn);
                subJoinColumnStrikeCases.add(toSymbolCase(subColumn.getJavaField(), '-')); // Convert DictType to dict-type
                // className related
                String subSimpleClassName = removePrefix(subTable.getClassName(), upperFirst(subTable.getModuleName()));
                subSimpleClassNames.add(subSimpleClassName);
                simpleClassNameUnderlineCases.add(toUnderlineCase(subSimpleClassName)); // Convert DictType to dict_type
                subClassNameVars.add(lowerFirst(subSimpleClassName)); // Convert DictType to dictType for use with variables
                subSimpleClassNameStrikeCases.add(toSymbolCase(subSimpleClassName, '-')); // Convert DictType to dict-type
            }
            bindingMap.put("subPrimaryColumns", subPrimaryColumns);
            bindingMap.put("subJoinColumns", subJoinColumns);
            bindingMap.put("subJoinColumn_strikeCases", subJoinColumnStrikeCases);
            bindingMap.put("subSimpleClassNames", subSimpleClassNames);
            bindingMap.put("simpleClassNameUnderlineCases", simpleClassNameUnderlineCases);
            bindingMap.put("subClassNameVars", subClassNameVars);
            bindingMap.put("subSimpleClassName_strikeCases", subSimpleClassNameStrikeCases);
        }

        // VO variables common to multiple VMs
        if (ObjectUtil.equal(codegenProperties.getVoType(), CodegenVOTypeEnum.VO.getType())) {
            String prefixClass = CodegenSceneEnum.valueOf(table.getScene()).getPrefixClass();
            bindingMap.put("saveReqVOClass", prefixClass + className + "SaveReqVO");
            bindingMap.put("updateReqVOClass", prefixClass + className + "SaveReqVO");
            bindingMap.put("respVOClass", prefixClass + className + "RespVO");
            bindingMap.put("saveReqVOVar", "createReqVO");
            bindingMap.put("updateReqVOVar", "updateReqVO");
        } else if (ObjectUtil.equal(codegenProperties.getVoType(), CodegenVOTypeEnum.DO.getType())) {
            bindingMap.put("saveReqVOClass", className + "DO");
            bindingMap.put("updateReqVOClass", className + "DO");
            bindingMap.put("respVOClass", className + "DO");
            bindingMap.put("saveReqVOVar", classNameVar);
            bindingMap.put("updateReqVOVar", classNameVar);
        }
        return bindingMap;
    }

    private Map<String, String> getTemplates(Integer frontType) {
        Map<String, String> templates = new LinkedHashMap<>();
        templates.putAll(SERVER_TEMPLATES);
        templates.putAll(FRONT_TEMPLATES.row(frontType));
        // If it is a Boot project, the API/server module is not used
        if (Boolean.FALSE.equals(cloudEnable)) {
            SERVER_TEMPLATES.forEach((templatePath, filePath) -> {
                filePath = StrUtil.replace(filePath, "/sar-module-${table.moduleName}-api", "");
                filePath = StrUtil.replace(filePath, "/sar-module-${table.moduleName}-server", "");
                templates.put(templatePath, filePath);
            });
        }
        // If unit testing is disabled, the corresponding template is removed
        if (Boolean.FALSE.equals(codegenProperties.getUnitTestEnable())) {
            templates.remove(javaTemplatePath("test/serviceTest"));
            templates.remove("codegen/sql/h2.vm");
        }
        // If the VO type is disabled, the corresponding template is removed
        if (ObjectUtil.notEqual(codegenProperties.getVoType(), CodegenVOTypeEnum.VO.getType())) {
            templates.remove(javaTemplatePath("controller/vo/respVO"));
            templates.remove(javaTemplatePath("controller/vo/saveReqVO"));
        }
        return templates;
    }

    @SuppressWarnings("unchecked")
    private String formatFilePath(String filePath, Map<String, Object> bindingMap) {
        filePath = StrUtil.replace(filePath, "${basePackage}",
                getStr(bindingMap, "basePackage").replaceAll("\\.", "/"));
        filePath = StrUtil.replace(filePath, "${classNameVar}",
                getStr(bindingMap, "classNameVar"));
        filePath = StrUtil.replace(filePath, "${simpleClassName}",
                getStr(bindingMap, "simpleClassName"));
        // Fields contained in sceneEnum
        CodegenSceneEnum sceneEnum = (CodegenSceneEnum) bindingMap.get("sceneEnum");
        filePath = StrUtil.replace(filePath, "${sceneEnum.prefixClass}", sceneEnum.getPrefixClass());
        filePath = StrUtil.replace(filePath, "${sceneEnum.basePackage}", sceneEnum.getBasePackage());
        // The fields contained in the table
        CodegenTableDO table = (CodegenTableDO) bindingMap.get("table");
        filePath = StrUtil.replace(filePath, "${table.moduleName}", table.getModuleName());
        filePath = StrUtil.replace(filePath, "${table.businessName}", table.getBusinessName());
        filePath = StrUtil.replace(filePath, "${table.className}", table.getClassName());
        // Special: Exclusive logic for master and sub-tables
        Integer subIndex = (Integer) bindingMap.get("subIndex");
        if (subIndex != null) {
            CodegenTableDO subTable = ((List<CodegenTableDO>) bindingMap.get("subTables")).get(subIndex);
            filePath = StrUtil.replace(filePath, "${subTable.moduleName}", subTable.getModuleName());
            filePath = StrUtil.replace(filePath, "${subTable.businessName}", subTable.getBusinessName());
            filePath = StrUtil.replace(filePath, "${subTable.className}", subTable.getClassName());
            filePath = StrUtil.replace(filePath, "${subSimpleClassName}",
                    ((List<String>) bindingMap.get("subSimpleClassNames")).get(subIndex));
            filePath = StrUtil.replace(filePath, "${subSimpleClassName_strikeCase}",
                    ((List<String>) bindingMap.get("subSimpleClassName_strikeCases")).get(subIndex));
        }
        return filePath;
    }

    private static String javaTemplatePath(String path) {
        return "codegen/java/" + path + ".vm";
    }

    private static String javaModuleImplVOFilePath(String path) {
        return javaModuleFilePath("controller/${sceneEnum.basePackage}/${table.businessName}/" +
                "vo/${sceneEnum.prefixClass}${table.className}" + path, "server", "main");
    }

    private static String javaModuleImplControllerFilePath() {
        return javaModuleFilePath("controller/${sceneEnum.basePackage}/${table.businessName}/" +
                "${sceneEnum.prefixClass}${table.className}Controller", "server", "main");
    }

    private static String javaModuleImplMainFilePath(String path) {
        return javaModuleFilePath(path, "server", "main");
    }

    private static String javaModuleApiMainFilePath(String path) {
        return javaModuleFilePath(path, "api", "main");
    }

    private static String javaModuleImplTestFilePath(String path) {
        return javaModuleFilePath(path, "server", "test");
    }

    private static String javaModuleFilePath(String path, String module, String src) {
        return "sar-module-${table.moduleName}/" + // top module
                "sar-module-${table.moduleName}-" + module + "/" + // submodule
                "src/" + src + "/java/${basePackage}/module/${table.moduleName}/" + path + ".java";
    }

    private static String mapperXmlFilePath() {
        return "sar-module-${table.moduleName}/" + // top module
                "sar-module-${table.moduleName}-server/" + // submodule
                "src/main/resources/mapper/${table.businessName}/${table.className}Mapper.xml";
    }

    private static String vueTemplatePath(String path) {
        return "codegen/vue/" + path + ".vm";
    }

    private static String vueFilePath(String path) {
        return "sar-ui-${sceneEnum.basePackage}-vue2/" + // top level directory
                "src/" + path;
    }

    private static String vue3TemplatePath(String path) {
        return "codegen/vue3/" + path + ".vm";
    }

    private static String vue3FilePath(String path) {
        return "sar-ui-${sceneEnum.basePackage}-vue3/" + // top level directory
                "src/" + path;
    }

    private static String vue3AdminUniappTemplatePath(String path) {
        return "codegen/vue3_admin_uniapp/" + path + ".vm";
    }

    private static String vue3UniappFilePath(String path) {
        return "sar-ui-${sceneEnum.basePackage}-uniapp/" + // top level directory
                "src/" + path;
    }

    private static String vue3VbenFilePath(String path) {
        return "sar-ui-${sceneEnum.basePackage}-vben/" + // top level directory
                "src/" + path;
    }

    private static String vue3VbenTemplatePath(String path) {
        return "codegen/vue3_vben/" + path + ".vm";
    }

    private static String vue3Vben5AntdSchemaTemplatePath(String path) {
        return "codegen/vue3_vben5_antd/schema/" + path + ".vm";
    }

    private static String vue3Vben5AntdGeneralTemplatePath(String path) {
        return "codegen/vue3_vben5_antd/general/" + path + ".vm";
    }

    private static String vue3Vben5EpSchemaTemplatePath(String path) {
        return "codegen/vue3_vben5_ele/schema/" + path + ".vm";
    }

    private static String vue3Vben5EpGeneralTemplatePath(String path) {
        return "codegen/vue3_vben5_ele/general/" + path + ".vm";
    }

    private static boolean isSubTemplate(String path) {
        return path.contains("_sub");
    }

    private static boolean isPageReqVOTemplate(String path) {
        return path.contains("pageReqVO");
    }

    private static boolean isListReqVOTemplate(String path) {
        return path.contains("listReqVO");
    }

}
