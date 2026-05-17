package cn.iocoder.yudao.framework.common.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description="Paging parameters")
@Data
public class PageParam implements Serializable {

 private static final Integer PAGE_NO = 1;
 private static final Integer PAGE_SIZE = 10;

 /**
     * Number of items per page - no pagination
 *
     * For example, when exporting an interface, you can set {@link #pageSize} to -1 to query all data without paging.
 */
 public static final Integer PAGE_SIZE_NONE = -1;

    @Schema(description = "Page number, starting from 1", requiredMode = Schema.RequiredMode.REQUIRED,example = "1")
    @NotNull(message = "page number cannot be empty")
    @Min(value = 1, message = "The minimum page number is 1")
 private Integer pageNo = PAGE_NO;

    @Schema(description = "Number of items per page, maximum value is 200", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "the number of items per page cannot be empty")
    @Min(value = 1, message = "The minimum number of items per page is 1")
    @Max(value = 200, message = "The maximum number of items per page is 200")
 private Integer pageSize = PAGE_SIZE;

}
