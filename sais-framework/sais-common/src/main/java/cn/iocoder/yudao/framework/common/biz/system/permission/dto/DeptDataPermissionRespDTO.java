package cn.iocoder.yudao.framework.common.biz.system.permission.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Department data permissions Response DTO
 *
 * @author Yudao Source Code
 */
@Data
public class DeptDataPermissionRespDTO {

 /**
     * Can all data be viewed?
 */
 private Boolean all;
 /**
     * Can I view my own data?
 */
 private Boolean self;
 /**
     * Viewable array of department IDs
 */
 private Set<Long> deptIds;

 public DeptDataPermissionRespDTO() {
 this.all = false;
 this.self = false;
 this.deptIds = new HashSet<>();
 }

}
