package cn.iocoder.yudao.framework.excel.core.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;
import cn.iocoder.yudao.framework.excel.core.annotations.ExcelColumnSelect;
import cn.iocoder.yudao.framework.excel.core.function.ExcelColumnSelectFunction;
import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.write.handler.SheetWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * Implement drop-down box based on fixed sheet
 *
 * @author HUIHUI
 */
@Slf4j
public class SelectSheetWriteHandler implements SheetWriteHandler {

 /**
     * The data starting row starts from 0
 *
     * Convention: The first row of this project has a title, so it starts from 1. If your Excel has multiple rows of titles, please change it yourself.
 */
 public static final int FIRST_ROW = 1;
 /**
     * The number of rows required to create a drop-down box in the drop-down column. The default is 2,000 rows. Please adjust it if you need more.
 */
 public static final int LAST_ROW = 2000;

    private static final String DICT_SHEET_NAME = "dictionary sheet";

 /**
     * key: column value: drop-down data source
 */
 private final Map<Integer, List<String>> selectMap = new HashMap<>();

 public SelectSheetWriteHandler(Class<?> head) {
        // Parse drop-down data
 int colIndex = 0;
 boolean ignoreUnannotated = head.isAnnotationPresent(ExcelIgnoreUnannotated.class);
 for (Field field: head.getDeclaredFields()) {
            // Related https://github.com/YunaiV/ruoyi-vue-pro/pull/853
            // 1.1 Ignore static final or transient fields
 if (isStaticFinalOrTransient(field) ) {
 continue;
 }
            // 1.2 Ignored fields are skipped
 if ((ignoreUnannotated && !field.isAnnotationPresent(ExcelProperty.class))
 || field.isAnnotationPresent(ExcelIgnore.class)) {
 continue;
 }

            // 2. Core: Processing fields annotated with ExcelColumnSelect
 if (field.isAnnotationPresent(ExcelColumnSelect.class)) {
 ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
 if (excelProperty != null && excelProperty.index() != -1) {
 colIndex = excelProperty.index();
 }
 getSelectDataList(colIndex, field);
 }
 colIndex++;
 }
 }

 /**
     * Determine whether the field is static, final, or transient
     * Reason: FastExcel ignores static final or transient fields by default, so you need to judge
 *
     * @param field Field
     * @return Whether it is static, final, transient
 */
 private boolean isStaticFinalOrTransient(Field field) {
 return (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
 || Modifier.isTransient(field.getModifiers());
 }


 /**
     * Get drop-down data and add it to {@link #selectMap}
 *
     * @param colIndex column index
     * @param field Field
 */
 private void getSelectDataList(int colIndex, Field field) {
 ExcelColumnSelect columnSelect = field.getAnnotation(ExcelColumnSelect.class);
 String dictType = columnSelect.dictType();
 String functionName = columnSelect.functionName();
 Assert.isTrue(ObjectUtil.isNotEmpty(dictType) || ObjectUtil.isNotEmpty(functionName),
                "@ExcelColumnSelect annotation of Field({}), dictType and functionName cannot be empty at the same time", field.getName());

        // Scenario 1: Use dictType to obtain drop-down data
        if (StrUtil.isNotEmpty(dictType)) { // Case 1: Dictionary data (default)
 selectMap.put(colIndex, DictFrameworkUtils.getDictDataLabelList(dictType));
 return;
 }

        // Case 2: Use functionName to obtain drop-down data
 Map<String, ExcelColumnSelectFunction> functionMap = SpringUtil.getApplicationContext().getBeansOfType(ExcelColumnSelectFunction.class);
 ExcelColumnSelectFunction function = CollUtil.findOne(functionMap.values(), item -> item.getName().equals(functionName));
        Assert.notNull(function, "The corresponding function({}) was not found", functionName);
 selectMap.put(colIndex, function.getOptions());
 }

 @Override
 public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
 if (CollUtil.isEmpty(selectMap)) {
 return;
 }

        // 1. Obtain the corresponding operation object
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper(); // Data validation assistant for sheet pages where drop-down boxes need to be set
        Workbook workbook = writeWorkbookHolder.getWorkbook(); // Get workbook
 List<KeyValue<Integer, List<String>>> keyValues = convertList(selectMap.entrySet(), entry -> new KeyValue<>(entry.getKey(), entry.getValue()));
        keyValues.sort(Comparator.comparing(item -> item.getValue().size())); // Ascending order, otherwise an error will be reported when creating a drop-down

        // 2. Create the sheet page of the data dictionary
 Sheet dictSheet = workbook.createSheet(DICT_SHEET_NAME);
 for (KeyValue<Integer, List<String>> keyValue: keyValues) {
 int rowLength = keyValue.getValue().size();
            // 2.1 Set the value of the dictionary sheet page, one dictionary item for each column
 for (int i = 0; i < rowLength; i++) {
 Row row = dictSheet.getRow(i);
 if (row == null) {
 row = dictSheet.createRow(i);
 }
 row.createCell(keyValue.getKey()).setCellValue(keyValue.getValue().get(i));
 }
            // 2.2 Set cell drop-down selection
 setColumnSelect(writeSheetHolder, workbook, helper, keyValue);
 }
 }

 /**
     * Set cell drop-down selection
 */
 private static void setColumnSelect(WriteSheetHolder writeSheetHolder, Workbook workbook, DataValidationHelper helper,
 KeyValue<Integer, List<String>> keyValue) {
        // 1.1 Create a name that can be referenced by other cells
 Name name = workbook.createName();
 String excelColumn = ExcelUtil.indexToColName(keyValue.getKey());
        // 1.2 Drop-down box data source eg: dictionary sheet!$B1:$B2
 String refers = DICT_SHEET_NAME + "!$" + excelColumn + "$1:$" + excelColumn + "$" + keyValue.getValue().size();
        name.setNameName("dict" + keyValue.getKey()); // Set the name of the name
        name.setRefersToFormula(refers); // Set formula

        // 2.1 Set constraints
        DataValidationConstraint constraint = helper.createFormulaListConstraint("dict" + keyValue.getKey()); // Set reference constraints
        // Set the first row, last row, first column, and last column of the drop-down cell
 CellRangeAddressList rangeAddressList = new CellRangeAddressList(FIRST_ROW, LAST_ROW,
 keyValue.getKey(), keyValue.getKey());
 DataValidation validation = helper.createValidation(constraint, rangeAddressList);
 if (validation instanceof HSSFDataValidation) {
 validation.setSuppressDropDownArrow(false);
 } else {
 validation.setSuppressDropDownArrow(true);
 validation.setShowErrorBox(true);
 }
        // 2.2 Prevent input of non-drop-down box values
 validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.createErrorBox("hint", "This value does not exist in the dropdown selection!");
        // 2.3 Add drop-down box constraints
 writeSheetHolder.getSheet().addValidationData(validation);
 }

}
