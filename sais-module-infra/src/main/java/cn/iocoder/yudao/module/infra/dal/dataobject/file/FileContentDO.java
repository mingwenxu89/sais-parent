package cn.iocoder.yudao.module.infra.dal.dataobject.file;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.framework.file.core.client.db.DBFileClient;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * File content table
 *
 * Specifically used to store the file contents of {@link DBFileClient}
 *
 * @author Yudao Source Code
 */
@TableName("infra_file_content")
@KeySequence("infra_file_content_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class FileContentDO extends BaseDO {

    /**
     * ID, automatically incremented by the database
     */
    @TableId
    private Long id;
    /**
     * Configuration ID
     *
     * Association {@link FileConfigDO#getId()}
     */
    private Long configId;
    /**
     * path, i.e. file name
     */
    private String path;
    /**
     * File content
     */
    private byte[] content;

}
