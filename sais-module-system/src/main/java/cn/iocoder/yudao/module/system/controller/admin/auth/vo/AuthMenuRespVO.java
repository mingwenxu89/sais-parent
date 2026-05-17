package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Management backend - Menu information for logged in users Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthMenuRespVO {

    @Schema(description = "Menu name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private Long id;

    @Schema(description = "Parent menu ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long parentId;

    @Schema(description = "Menu name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String name;

    @Schema(description = "Routing address, only needs to be passed when the menu type is menu or directory.", example = "post")
    private String path;

    @Schema(description = "Component path, only needs to be passed when the menu type is menu", example = "system/post/index")
    private String component;

    @Schema(description = "Component name", example = "SystemUser")
    private String componentName;

    @Schema(description = "Menu icon, only needs to be passed when the menu type is menu or directory.", example = "/menu/list")
    private String icon;

    @Schema(description = "visible or not", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean visible;

    @Schema(description = "Whether to cache", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean keepAlive;

    @Schema(description = "Whether to always display", example = "false")
    private Boolean alwaysShow;

    /**
     * Subroutes
     */
    private List<AuthMenuRespVO> children;

}
