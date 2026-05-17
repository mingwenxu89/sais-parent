package cn.iocoder.yudao.module.infra.framework.file.core.client;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * File client configuration
 * Clients with different implementations require different configurations, which are defined through subclasses
 *
 * @author Yudao Source Code
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
// The role of @JsonTypeInfo annotation, Jackson polymorphism
// 1. When serializing to the database, add the @class attribute.
// 2. When deserializing to a memory object, the correct type can be created through the @class attribute
public interface FileClientConfig {
}
