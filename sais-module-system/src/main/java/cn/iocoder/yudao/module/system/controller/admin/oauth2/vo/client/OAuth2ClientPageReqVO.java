package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "Management backend - OAuth2 client paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OAuth2ClientPageReqVO extends PageParam {

    @Schema(description = "Application name, fuzzy matching", example = "potatoes")
    private String name;

    @Schema(description = "Status, see CommonStatusEnum enumeration", example = "1")
    private Integer status;

}
