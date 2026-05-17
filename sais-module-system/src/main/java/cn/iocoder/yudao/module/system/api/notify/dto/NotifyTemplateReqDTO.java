package cn.iocoder.yudao.module.system.api.notify.dto;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class NotifyTemplateReqDTO {

    @NotEmpty(message = "Template name cannot be empty")
    private String name;

    @NotNull(message = "Template code cannot be empty")
    private String code;

    @NotNull(message = "Template type cannot be empty")
    private Integer type;

    @NotEmpty(message = "Sender name cannot be empty")
    private String nickname;

    @NotEmpty(message = "Template content cannot be empty")
    private String content;

    @NotNull(message = "Status cannot be empty")
    @InEnum(value = CommonStatusEnum.class, message = "status must be {value}")
    private Integer status;

    private String remark;

}
