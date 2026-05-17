package cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Management backend - Tenant Response VO")
@Data
@ExcelIgnoreUnannotated
public class TenantRespVO {

    @Schema(description = "Tenant ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Tenant ID")
    private Long id;

    @Schema(description = "Tenant name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @ExcelProperty("Tenant name")
    private String name;

    @Schema(description = "Contact person", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @ExcelProperty("Contact person")
    private String contactName;

    @Schema(description = "Contact mobile phone", example = "15601691300")
    @ExcelProperty("Contact mobile phone")
    private String contactMobile;

    @Schema(description = "Tenant status", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "Status", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "Bind domain name array", example = "https://www.iocoder.cn")
    private List<String> websites;

    @Schema(description = "Tenant Package ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long packageId;

    @Schema(description = "Expiration time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expireTime;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("Create Time")
    private LocalDateTime createTime;

}
