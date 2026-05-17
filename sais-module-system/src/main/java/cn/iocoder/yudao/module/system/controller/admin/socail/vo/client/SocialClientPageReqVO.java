package cn.iocoder.yudao.module.system.controller.admin.socail.vo.client;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "Management backend - social client paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SocialClientPageReqVO extends PageParam {

    @Schema(description = "Application name", example = "yudao mall")
    private String name;

    @Schema(description = "Types of social platforms", example = "31")
    private Integer socialType;

    @Schema(description = "User type", example = "2")
    private Integer userType;

    @Schema(description = "client ID", example = "145442115")
    private String clientId;

    @Schema(description = "Status", example = "1")
    private Integer status;

}
