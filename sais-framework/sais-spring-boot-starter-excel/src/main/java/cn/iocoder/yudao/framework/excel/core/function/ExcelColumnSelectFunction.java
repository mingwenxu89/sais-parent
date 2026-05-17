package cn.iocoder.yudao.framework.excel.core.function;

import java.util.List;

/**
 * Excel column drop-down data source acquisition interface
 *
 * Why not parse the dictionary directly and create an interface? Considering that some drop-down data is not obtained from the dictionary, a compatible

 * @author HUIHUI
 */
public interface ExcelColumnSelectFunction {

 /**
     * Get method name
 *
     * @return method name
 */
 String getName();

 /**
     * Get column dropdown data source
 *
     * @return Drop down data source
 */
 List<String> getOptions();

}
