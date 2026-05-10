package cn.iocoder.yudao.module.agri.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Agri error code enum
 *
 * agri module uses the 1-005-000-000 range
 */
public interface ErrorCodeConstants {

    // ========== Crop 1-005-001-000 ==========
    ErrorCode CROP_NOT_EXISTS = new ErrorCode(1_005_001_000, "Crop does not exist");
    ErrorCode CROP_GROWTH_STAGE_NOT_EXISTS = new ErrorCode(1_005_001_001, "Crop growth stage does not exist");

    // ========== Field 1-005-002-000 ==========
    ErrorCode FIELD_NOT_EXISTS = new ErrorCode(1_005_002_000, "Field does not exist");
    ErrorCode FARM_NOT_EXISTS = new ErrorCode(1_005_002_001, "Farm does not exist, please set up the farm location first");

    // ========== IoT Device 1-005-003-000 ==========
    ErrorCode DEVICE_NOT_EXISTS = new ErrorCode(1_005_003_000, "IoT device does not exist");
    ErrorCode DEVICE_CODE_DUPLICATE = new ErrorCode(1_005_003_001, "Device code already exists");

    // ========== Sensor Data 1-005-004-000 ==========
    ErrorCode SENSOR_DATA_NOT_EXISTS = new ErrorCode(1_005_004_000, "Sensor data does not exist");

    // ========== Irrigation Plan 1-005-011-000 ==========
    ErrorCode IRRIGATION_PLAN_NOT_EXISTS = new ErrorCode(1_005_011_000, "Irrigation plan does not exist");

    // ========== Weather Data 1-005-006-000 ==========
    ErrorCode WEATHER_DATA_NOT_EXISTS = new ErrorCode(1_005_006_000, "Weather data does not exist");

    // ========== Alert 1-005-007-000 ==========
    ErrorCode ALERT_NOT_EXISTS = new ErrorCode(1_005_007_000, "Alert does not exist");

    // ========== Crop Plan 1-005-008-000 ==========
    ErrorCode CROP_PLAN_NOT_EXISTS = new ErrorCode(1_005_008_000, "Crop plan does not exist");
    ErrorCode CROP_PLAN_NOT_UNSTARTED = new ErrorCode(1_005_008_001, "Crop plan cannot be modified because it has already started");

    // ========== Sensor 1-005-009-000 ==========
    ErrorCode SENSOR_NOT_EXISTS = new ErrorCode(1_005_009_000, "Sensor does not exist");
    ErrorCode SENSOR_CODE_DUPLICATE = new ErrorCode(1_005_009_001, "Sensor code already exists");

    // ========== Irrigation Device 1-005-010-000 ==========
    ErrorCode IRRIGATION_DEVICE_NOT_EXISTS = new ErrorCode(1_005_010_000, "Irrigation device does not exist");
    ErrorCode IRRIGATION_DEVICE_FIELD_OCCUPIED = new ErrorCode(1_005_010_001, "This field already has an irrigation device");
    ErrorCode IRRIGATION_DEVICE_NOT_ACTIVE = new ErrorCode(1_005_010_002, "Irrigation device is not active, cannot start or stop watering");

}
