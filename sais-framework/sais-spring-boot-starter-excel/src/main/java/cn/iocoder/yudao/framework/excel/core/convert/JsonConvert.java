package cn.iocoder.yudao.framework.excel.core.convert;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

/**
 * Excel JSON converter
 *
 * @author Yudao Source Code
 */
public class JsonConvert implements Converter<Object> {

 @Override
 public Class<?> supportJavaTypeKey() {
        throw new UnsupportedOperationException("Not supported and not required");
 }

 @Override
 public CellDataTypeEnum supportExcelTypeKey() {
        throw new UnsupportedOperationException("Not supported and not required");
 }

 @Override
 public WriteCellData<String> convertToExcelData(Object value, ExcelContentProperty contentProperty,
 GlobalConfiguration globalConfiguration) {
        // Generate Excel small table
 return new WriteCellData<>(JsonUtils.toJsonString(value));
 }

}
