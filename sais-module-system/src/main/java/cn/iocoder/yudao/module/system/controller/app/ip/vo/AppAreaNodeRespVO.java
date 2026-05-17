package cn.iocoder.yudao.module.system.controller.app.ip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "User App - Regional Node Response VO")
@Data
public class AppAreaNodeRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "110000")
    private Integer id;

    @Schema(description = "name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Beijing")
    private String name;

    /**
     * child node
     */
    private List<AppAreaNodeRespVO> children;

}
