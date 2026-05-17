package cn.iocoder.yudao.framework.excel.core.convert;

import cn.hutool.core.convert.Convert;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel data locale converter
 *
 * @author HUIHUI
 */
@Slf4j
public class AreaConvert implements Converter<Object> {

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
        // Parse area code
 String label = readCellData.getStringValue();
 Area area = AreaUtils.parseArea(label);
 if (area == null) {
            log.error("[convertToJavaData][label({}) cannot be parsed]", label);
 return null;
 }
        // Convert value into the corresponding attribute
 Class<?> fieldClazz = contentProperty.getField().getType();
 return Convert.convert(fieldClazz, area.getId());
 }

}
