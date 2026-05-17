package cn.iocoder.yudao.framework.excel.core.convert;

import cn.hutool.core.convert.Convert;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel Data Dictionary Converter
 *
 * @author Yudao Source Code
 */
@Slf4j
public class DictConvert implements Converter<Object> {

 @Override
 public Class<?> supportJavaTypeKey() {
        throw new UnsupportedOperationException("Not supported and not required");
 }

 @Override
 public CellDataTypeEnum supportExcelTypeKey() {
        throw new UnsupportedOperationException("Not supported and not required");
 }

 @Override
 public Object convertToJavaData(ReadCellData readCellData, ExcelContentProperty contentProperty,
 GlobalConfiguration globalConfiguration) {
        // Use dictionary parsing
 String type = getType(contentProperty);
 String label = readCellData.getStringValue();
 String value = DictFrameworkUtils.parseDictDataValue(type, label);
 if (value == null) {
            log.error("[convertToJavaData][type({}) cannot be parsed label({})]", type, label);
 return null;
 }
        // Convert the value of String into the corresponding attribute
 Class<?> fieldClazz = contentProperty.getField().getType();
 return Convert.convert(fieldClazz, value);
 }

 @Override
 public WriteCellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty,
 GlobalConfiguration globalConfiguration) {
        // When empty, return empty
 if (object == null) {
 return new WriteCellData<>("");
 }

        // Use dictionary formatting
 String type = getType(contentProperty);
 String value = String.valueOf(object);
 String label = DictFrameworkUtils.parseDictDataLabel(type, value);
 if (label == null) {
            log.error("[convertToExcelData][type({}) cannot be converted label({})]", type, value);
 return new WriteCellData<>("");
 }
        // Generate Excel small table
 return new WriteCellData<>(label);
 }

 private static String getType(ExcelContentProperty contentProperty) {
 return contentProperty.getField().getAnnotation(DictFormat.class).value();
 }

}
