package cn.iocoder.yudao.module.system.controller.admin.user.vo.profile;

import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.post.PostSimpleRespVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RoleSimpleRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Management backend - User personal center information Response VO")
public class UserProfileRespVO {

    @Schema(description = "User ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    private String username;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    private String nickname;

    @Schema(description = "User email", example = "yudao@iocoder.cn")
    private String email;

    @Schema(description = "Mobile phone ID", example = "15601691300")
    private String mobile;

    @Schema(description = "User gender, see SexEnum enumeration class", example = "1")
    private Integer sex;

    @Schema(description = "User avatar", example = "https://www.iocoder.cn/xxx.png")
    private String avatar;

    @Schema(description = "Last login IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "192.168.1.1")
    private String loginIp;

    @Schema(description = "Last login time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime loginDate;

    @Schema(description = "Create Time", requiredMode = Schema.RequiredMode.REQUIRED, example = "timestamp format")
    private LocalDateTime createTime;

    /**
     * Role
     */
    private List<RoleSimpleRespVO> roles;
    /**
     * Department
     */
    private DeptSimpleRespVO dept;
    /**
     * Position array
     */
    private List<PostSimpleRespVO> posts;

}
