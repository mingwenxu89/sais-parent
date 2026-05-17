package cn.iocoder.yudao.module.infra.framework.codegen.config;

import cn.iocoder.yudao.module.infra.enums.codegen.CodegenFrontTypeEnum;
import cn.iocoder.yudao.module.infra.enums.codegen.CodegenVOTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;

@ConfigurationProperties(prefix = "yudao.codegen")
@Validated
@Data
public class CodegenProperties {

    /**
     * Base package for generated Java code
     */
    @NotNull(message = "Java code base package cannot be empty")
    private String basePackage;

    /**
     * Database name array
     */
    @NotEmpty(message = "database cannot be empty")
    private Collection<String> dbSchemas;

    /**
     * Frontend type for code generation (default)
     *
     * Enum {@link CodegenFrontTypeEnum#getType()}
     */
    @NotNull(message = "code generation frontend type cannot be empty")
    private Integer frontType;

    /**
     * Code generated VO type
     *
     * Enum {@link CodegenVOTypeEnum#getType()}
     */
    @NotNull(message = "code generation VO type cannot be empty")
    private Integer voType;

    /**
     * Whether to generate a batch deletion API
     */
    @NotNull(message = "whether to generate batch delete API cannot be empty")
    private Boolean deleteBatchEnable;

    /**
     * Whether to generate unit tests
     */
    @NotNull(message = "whether to generate unit tests cannot be empty")
    private Boolean unitTestEnable;

}
