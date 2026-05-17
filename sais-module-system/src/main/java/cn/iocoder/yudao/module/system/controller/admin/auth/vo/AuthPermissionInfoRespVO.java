package cn.iocoder.yudao.module.system.controller.admin.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Schema(description = "Management background - permission information of logged in user Response VO, additionally including user information and role list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthPermissionInfoRespVO {

    @Schema(description = "User information", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserVO user;

    @Schema(description = "Role ID array", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> roles;

    @Schema(description = "Operation permission array", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> permissions;

    @Schema(description = "menu tree", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MenuVO> menus;

    @Schema(description = "User information VO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserVO {

        @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long id;

        @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yudao Source Code")
        private String nickname;

        @Schema(description = "User avatar", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.jpg")
        private String avatar;

        @Schema(description = "Department ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
        private Long deptId;

        @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
        private String username;

        @Schema(description = "User email", example = "yudao@iocoder.cn")
        private String email;

    }

    @Schema(description = "Management backend - Menu information for logged in users Response VO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MenuVO {

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
        private List<MenuVO> children;

    }

}
