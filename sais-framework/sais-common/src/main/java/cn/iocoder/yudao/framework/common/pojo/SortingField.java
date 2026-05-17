package cn.iocoder.yudao.framework.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Sort field DTO
 *
 * The reason why ing is added to the class name is to avoID having the same name as ES SortField.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortingField implements Serializable {

 /**
     * Order - Ascending
 */
 public static final String ORDER_ASC = "asc";
 /**
     * Order - Descending
 */
 public static final String ORDER_DESC = "desc";

 /**
     * Field
 */
 private String field;
 /**
     * order
 */
 private String order;

}
