package cn.iocoder.yudao.module.system.controller.admin.notice.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Management background - notification and announcement paging Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticePageReqVO extends PageParam {

    @Schema(description = "Notification announcement name, fuzzy matching", example = "taro road")
    private String title;

    @Schema(description = "Display status, see CommonStatusEnum enumeration class", example = "1")
    private Integer status;

}
