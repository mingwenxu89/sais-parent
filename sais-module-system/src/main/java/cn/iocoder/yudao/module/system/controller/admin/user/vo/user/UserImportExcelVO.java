package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import cn.idev.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Excel import VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserImportExcelVO {

    @ExcelProperty("Login name")
    private String username;

    @ExcelProperty("Username")
    private String nickname;

    @ExcelProperty("Department ID")
    private Long deptId;

    @ExcelProperty("User email")
    private String email;

    @ExcelProperty("Mobile phone ID")
    private String mobile;

    @ExcelProperty(value = "User gender", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private Integer sex;

    @ExcelProperty(value = "Account status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

}
