package cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Management backend - menu list Request VO")
@Data
public class MenuListReqVO {

    @Schema(description = "Menu name, fuzzy matching", example = "taro road")
    private String name;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

}
