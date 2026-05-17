package cn.iocoder.yudao.module.infra.dal.dataobject.file;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.client.db.DBFileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.client.ftp.FtpFileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.client.local.LocalFileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.client.s3.S3FileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.client.sftp.SftpFileClientConfig;
import cn.iocoder.yudao.module.infra.framework.file.core.enums.FileStorageEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;

import java.lang.reflect.Field;

/**
 * File configuration table
 *
 * @author Yudao Source Code
 */
@TableName(value = "infra_file_config", autoResultMap = true)
@KeySequence("infra_file_config_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class FileConfigDO extends BaseDO {

    /**
     * Configuration ID, database auto-increment
     */
    private Long id;
    /**
     * Configuration name
     */
    private String name;
    /**
     * memory
     *
     * Enum {@link FileStorageEnum}
     */
    private Integer storage;
    /**
     * Remark
     */
    private String remark;
    /**
     * Is it the main configuration?
     *
     * Since we can configure multiple file configurations, by default, the main configuration is used for file uploading.
     */
    private Boolean master;

    /**
     * Payment channel configuration
     */
    @TableField(typeHandler = FileClientConfigTypeHandler.class)
    private FileClientConfig config;

    public static class FileClientConfigTypeHandler extends AbstractJsonTypeHandler<Object> {

        public FileClientConfigTypeHandler(Class<?> type) {
            super(type);
        }

        public FileClientConfigTypeHandler(Class<?> type, Field field) {
            super(type, field);
        }

        @Override
        public Object parse(String json) {
            FileClientConfig config = JsonUtils.parseObjectQuietly(json, new TypeReference<FileClientConfig>() {
            });
            if (config != null) {
                return config;
            }

            // Package paths compatible with older versions
            String className = JsonUtils.parseObject(json, "@class", String.class);
            className = StrUtil.subAfter(className, ".", true);
            switch (className) {
                case "DBFileClientConfig":
                    return JsonUtils.parseObject2(json, DBFileClientConfig.class);
                case "FtpFileClientConfig":
                    return JsonUtils.parseObject2(json, FtpFileClientConfig.class);
                case "LocalFileClientConfig":
                    return JsonUtils.parseObject2(json, LocalFileClientConfig.class);
                case "SftpFileClientConfig":
                    return JsonUtils.parseObject2(json, SftpFileClientConfig.class);
                case "S3FileClientConfig":
                    return JsonUtils.parseObject2(json, S3FileClientConfig.class);
                default:
                    throw new IllegalArgumentException("Unknown FileClientConfig type:" + json);
            }
        }

        @Override
        public String toJson(Object obj) {
            return JsonUtils.toJsonString(obj);
        }

    }

}
