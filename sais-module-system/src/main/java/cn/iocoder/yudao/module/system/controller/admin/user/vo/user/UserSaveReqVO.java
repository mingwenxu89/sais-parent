package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.system.framework.operatelog.core.DeptParseFunction;
import cn.iocoder.yudao.module.system.framework.operatelog.core.PostParseFunction;
import cn.iocoder.yudao.module.system.framework.operatelog.core.SexParseFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;
import java.util.Set;

@Schema(description = "Management backend - User creation/modification Request VO")
@Data
public class UserSaveReqVO {

    @Schema(description = "User ID", example = "1024")
    private Long id;

    @Schema(description = "User account", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @NotBlank(message = "User account cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "User account consists of IDs and letters")
    @Size(min = 4, max = 30, message = "User account length is 4-30 characters")
    @DiffLogField(name = "User account")
    private String username;

    @Schema(description = "User nickname", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yunai")
    @Size(max = 30, message = "User nickname cannot exceed 30 characters in length")
    @DiffLogField(name = "User nickname")
    private String nickname;

    @Schema(description = "Remark", example = "I am a user")
    @DiffLogField(name = "Remark")
    private String remark;

    @Schema(description = "Department ID", example = "I am a user")
    @DiffLogField(name = "Department", function = DeptParseFunction.NAME)
    private Long deptId;

    @Schema(description = "Position ID array", example = "1")
    @DiffLogField(name = "Post", function = PostParseFunction.NAME)
    private Set<Long> postIds;

    @Schema(description = "User email", example = "yudao@iocoder.cn")
    @Email(message = "Email format is incorrect")
    @Size(max = 50, message = "Email length cannot exceed 50 characters")
    @DiffLogField(name = "User email")
    private String email;

    @Schema(description = "Mobile phone ID", example = "15601691300")
    @Mobile
    @DiffLogField(name = "Mobile phone ID")
    private String mobile;

    @Schema(description = "User gender, see SexEnum enumeration class", example = "1")
    @DiffLogField(name = "User gender", function = SexParseFunction.NAME)
    private Integer sex;

    @Schema(description = "User avatar", example = "https://www.iocoder.cn/xxx.png")
    @DiffLogField(name = "User avatar")
    private String avatar;

    // ========== Fields that need to be passed only when [Creating] ==========

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @Length(min = 4, max = 16, message = "Password length is 4-16 characters")
    private String password;

    @AssertTrue(message = "Password cannot be empty")
    @JsonIgnore
    public boolean isPasswordValid() {
        return id != null // When modifying, no need to pass
                || (ObjectUtil.isAllNotEmpty(password)); // When adding, password must be passed
    }

}
