package cn.iocoder.yudao.module.system.dal.dataobject.notice;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.enums.notice.NoticeTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Notice announcement form
 *
 * @author ruoyi
 */
@TableName("system_notice")
@KeySequence("system_notice_seq") // Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you DO not need to write it.
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeDO extends BaseDO {

    /**
     * Announcement ID
     */
    private Long id;
    /**
     * Announcement title
     */
    private String title;
    /**
     * Announcement type
     *
     * Enum {@link NoticeTypeEnum}
     */
    private Integer type;
    /**
     * Announcement content
     */
    private String content;
    /**
     * Announcement status
     *
     * Enum {@link CommonStatusEnum}
     */
    private Integer status;

}
