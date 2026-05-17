package cn.iocoder.yudao.module.system.controller.admin.logger.vo.operatelog;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Schema(description = "Management background - operation log Response VO")
@Data
@ExcelIgnoreUnannotated
public class OperateLogRespVO implements VO {

    @Schema(description = "Log ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("Log ID")
    private Long id;

    @Schema(description = "link tracking ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "89aca178-a370-411c-ae02-3f0d672be4ab")
    private String traceId;

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @Trans(type = TransType.SIMPLE, target = AdminUserDO.class, fields = "nickname", ref = "userName")
    private Long userId;
    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @ExcelProperty("operator")
    private String userName;

    @Schema(description = "User type", requiredMode = Schema.RequiredMode.REQUIRED, example = "1", implementation = Integer.class)
    @ExcelProperty("User type")
    @DictFormat(DictTypeConstants.USER_TYPE)
    private Integer userType;

    @Schema(description = "Operation module type", requiredMode = Schema.RequiredMode.REQUIRED, example = "Order")
    @ExcelProperty("Operation module type")
    private String type;

    @Schema(description = "Operation name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Create order")
    @ExcelProperty("Operation name")
    private String subType;

    @Schema(description = "Operation module business ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("Operation module business ID")
    private Long bizId;

    @Schema(description = "Operation details", example = "Modify the user information IDed 1, change the gender from male to female, and change the name from Yudao to Yuandao.")
    private String action;

    @Schema(description = "Extend fields", example = "{'orderId': 1}")
    private String extra;

    @Schema(description = "Request method name", requiredMode = Schema.RequiredMode.REQUIRED, example = "GET")
    @NotEmpty(message = "The request method name cannot be empty")
    private String requestMethod;

    @Schema(description = "Request address", requiredMode = Schema.RequiredMode.REQUIRED, example = "/xxx/yyy")
    private String requestUrl;

    @Schema(description = "User IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    private String userIp;

    @Schema(description = "Browser UserAgent", requiredMode = Schema.RequiredMode.REQUIRED, example = "Mozilla/5.0")
    private String userAgent;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
