package cn.iocoder.yudao.module.system.controller.admin.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "Management backend - menu information Response VO")
@Data
public class MenuRespVO {

    @Schema(description = "menu ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "Menu name", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    @NotBlank(message = "Menu name cannot be empty")
    @Size(max = 50, message = "Menu name cannot exceed 50 characters in length")
    private String name;

    @Schema(description = "Permission identifier, only needs to be passed when the menu type is button", example = "sys:menu:add")
    @Size(max = 100)
    private String permission;

    @Schema(description = "Type, see MenuTypeEnum enumeration class", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Menu type cannot be empty")
    private Integer type;

    @Schema(description = "Display order", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Display order cannot be empty")
    private Integer sort;

    @Schema(description = "Parent menu ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "Parent menu ID cannot be empty")
    private Long parentId;

    @Schema(description = "Routing address, only needs to be passed when the menu type is menu or directory.", example = "post")
    @Size(max = 200, message = "Routing address cannot exceed 200 characters")
    private String path;

    @Schema(description = "Menu icon, only needs to be passed when the menu type is menu or directory.", example = "/menu/list")
    private String icon;

    @Schema(description = "Component path, only needs to be passed when the menu type is menu", example = "system/post/index")
    @Size(max = 200, message = "Component path cannot exceed 255 characters")
    private String component;

    @Schema(description = "Component name", example = "SystemUser")
    private String componentName;

    @Schema(description = "Status, see CommonStatusEnum enumeration", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Status cannot be empty")
    private Integer status;

    @Schema(description = "visible or not", example = "false")
    private Boolean visible;

    @Schema(description = "Whether to cache", example = "false")
    private Boolean keepAlive;

    @Schema(description = "Whether to always display", example = "false")
    private Boolean alwaysShow;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

}
