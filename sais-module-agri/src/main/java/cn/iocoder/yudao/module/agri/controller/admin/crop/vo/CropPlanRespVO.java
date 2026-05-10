package cn.iocoder.yudao.module.agri.controller.admin.crop.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Crop Plan Response VO")
@Data
@ExcelIgnoreUnannotated
public class CropPlanRespVO {

    @Schema(description = "Crop plan ID")
    private Long id;

    @Schema(description = "Crop ID")
    private Long cropId;

    @Schema(description = "Crop name")
    private String cropName;

    @Schema(description = "Field ID")
    private Long fieldId;

    @Schema(description = "Field name")
    private String fieldName;

    @Schema(description = "Growth status: 1=Unstarted, 2=Ongoing, 3=Finished")
    private Integer growStatus;

    @Schema(description = "Start date")
    @NotNull(message = "Start date cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "End date")
    @NotNull(message = "Start date cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "Create time")
    private LocalDateTime createTime;

}
