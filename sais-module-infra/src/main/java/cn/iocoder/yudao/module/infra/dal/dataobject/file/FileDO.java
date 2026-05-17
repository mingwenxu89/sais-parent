package cn.iocoder.yudao.module.infra.dal.dataobject.file;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * file table
 * Every time a file is uploaded, a record will be recorded in this table.
 *
 * @author Yudao Source Code
 */
@TableName("infra_file")
@KeySequence("infra_file_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class FileDO extends BaseDO {

    /**
     * ID, automatically incremented by the database
     */
    private Long id;
    /**
     * Configuration ID
     *
     * Association {@link FileConfigDO#getId()}
     */
    private Long configId;
    /**
     * Original file name
     */
    private String name;
    /**
     * path, i.e. file name
     */
    private String path;
    /**
     * Access address
     */
    private String url;
    /**
     * The MIME type of the file, such as "application/octet-stream"
     */
    private String type;
    /**
     * file size
     */
    private Long size;

}
