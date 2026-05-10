package cn.iocoder.yudao.module.agri.controller.admin.sensor.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Sensor Response VO")
@Data
@ExcelIgnoreUnannotated
public class SensorRespVO {

    private Long id;

    private String sensorCode;

    private Integer sensorType;

    private String model;

    private Long farmId;

    private String farmName;

    private Long fieldId;

    private String fieldName;

    private Boolean isMock;

    private Integer status;

}
