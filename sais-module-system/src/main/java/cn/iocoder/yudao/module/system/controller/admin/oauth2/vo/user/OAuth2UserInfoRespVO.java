package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Management backend - OAuth2 Obtain basic user information Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserInfoRespVO {

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    private String username;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "taro road")
    private String nickname;

    @Schema(description = "User email", example = "yudao@iocoder.cn")
    private String email;
    @Schema(description = "Mobile phone ID", example = "15601691300")
    private String mobile;

    @Schema(description = "User gender, see SexEnum enumeration class", example = "1")
    private Integer sex;

    @Schema(description = "User avatar", example = "https://www.iocoder.cn/xxx.png")
    private String avatar;

    /**
     * Department
     */
    private Dept dept;

    /**
     * Position array
     */
    private List<Post> posts;

    @Schema(description = "Department")
    @Data
    public static class Dept {

        @Schema(description = "Department ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long id;

        @Schema(description = "Department name", requiredMode = Schema.RequiredMode.REQUIRED, example = "R&D Department")
        private String name;

    }

    @Schema(description = "Post")
    @Data
    public static class Post {

        @Schema(description = "Position ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        private Long id;

        @Schema(description = "Job title", requiredMode = Schema.RequiredMode.REQUIRED, example = "develop")
        private String name;

    }

}
