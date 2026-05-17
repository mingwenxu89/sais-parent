package cn.iocoder.yudao.framework.excel.core.util;

import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.converters.longconverter.LongStringConverter;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.excel.core.handler.ColumnWidthMatchStyleStrategy;
import cn.iocoder.yudao.framework.excel.core.handler.SelectSheetWriteHandler;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Excel tools
 *
 * @author Yudao Source Code
 */
public class ExcelUtils {

 /**
     * Respond the list to the front end in Excel
 *
     * @param response response
     * @param filename file name
     * @param sheetName Excel sheet name
     * @param head Excel head
     * @param data Data list
     * @param <T> Generic, ensuring the consistency of head and data types
     * @throws IOException when writing fails
 */
 public static <T> void write(HttpServletResponse response, String filename, String sheetName,
 Class<T> head, List<T> data) throws IOException {
        // Output Excel
 FastExcelFactory.write(response.getOutputStream(), head)
                .autoCloseStream(false) // Don't close it automatically, leave it to the Servlet to handle it yourself
                .registerWriteHandler(new ColumnWidthMatchStyleStrategy()) // Automatic adaptation based on column length. Maximum 255 width
                .registerWriteHandler(new SelectSheetWriteHandler(head)) // Implement drop-down box based on fixed sheet
                .registerConverter(new LongStringConverter()) // AvoID loss of precision for Long types
.sheet(sheetName).doWrite(data);
        // Set header and contentType. The reason for writing it at the end is to avoID reporting an error when the response contentType has been modified.
 response.addHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8(filename));
 response.setContentType("application/vnd.ms-excel;charset=UTF-8");
 }

 public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        // Refer to the post https://t.zsxq.com/zM77F, add try processing, and be compatible with windows scenarios
 try (InputStream inputStream = file.getInputStream()) {
 return FastExcelFactory.read(inputStream, head, null)
                    .autoCloseStream(false) // Don't close it automatically, leave it to the Servlet to handle it yourself
.doReadAllSync();
 }
 }

}
