package cn.iocoder.yudao.framework.ip.core;

import cn.iocoder.yudao.framework.ip.core.enums.AreaTypeEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Regional nodes, including country, province, city, region and other information
 *
 * The data is visible in the resources/area.csv file
 *
 * @author Yudao Source Code
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"parent"}) // See https://gitee.com/yudaocode/yudao-cloud-mini/pulls/2 Reason
public class Area {

 /**
     * Number - Global, i.e. root directory
 */
 public static final Integer ID_GLOBAL = 0;
 /**
     * Number - China
 */
 public static final Integer ID_CHINA = 1;

 /**
     * serial number
 */
 private Integer id;
 /**
     * name
 */
 private String name;
 /**
     * type
 *
     * Enum {@link AreaTypeEnum}
 */
 private Integer type;

 /**
     * parent node
 */
 @JsonManagedReference
 private Area parent;
 /**
     * child node
 */
 @JsonBackReference
 private List<Area> children;

}
