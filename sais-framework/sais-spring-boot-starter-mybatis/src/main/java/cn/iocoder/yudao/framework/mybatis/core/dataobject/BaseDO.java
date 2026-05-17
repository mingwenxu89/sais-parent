package cn.iocoder.yudao.framework.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * base entity object
 *
 * Why implement the {@link TransPojo} interface?
 * Because Easy-Trans TransType.SIMPLE mode is used, MyBatis Plus query is integrated
 *
 * @author Yudao Source Code
 */
@Data
@JsonIgnoreProperties(value = "transMap") // Easy-Trans will add the transMap attribute to avoID Jackson deserialization errors in Spring Cache
public abstract class BaseDO implements Serializable, TransPojo {

 /**
     * creation time
 */
 @TableField(fill = FieldFill.INSERT)
 private LocalDateTime createTime;
 /**
     * Last updated
 */
 @TableField(fill = FieldFill.INSERT_UPDATE)
 private LocalDateTime updateTime;
 /**
     * Creator, currently using SysUser's ID number
 *
     * The reason for using the String type is that there may be non-numeric values ​​in the future, so as to preserve scalability.
 */
 @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
 private String creator;
 /**
     * Updated by, currently using SysUser's ID number
 *
     * The reason for using the String type is that there may be non-numeric values ​​in the future, so as to preserve scalability.
 */
 @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
 private String updater;
 /**
     * Whether to delete
 */
 @TableLogic
 private Boolean deleted;

 /**
     * Clear creator, createTime, updateTime, and updater to prevent the front end from directly passing fields such as creator and being updated directly.
 */
 public void clean(){
 this.creator = null;
 this.createTime = null;
 this.updater = null;
 this.updateTime = null;
 }

}
