package cn.iocoder.yudao.module.infra.framework.file.core.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * File TypeUtils
 *
 * @author Yudao Source Code
 */
@Slf4j
public class FileTypeUtils {

    private static final Tika TIKA = new Tika();

    /**
     * Obtain the mineType of the file. There will be errors for doc, jar and other files.
     *
     * @param data File content
     * @return When mineType is not recognized, "application/octet-stream" will be returned.
     */
    @SneakyThrows
    public static String getMineType(byte[] data) {
        return TIKA.detect(data);
    }

    /**
     * Knowing the file name, obtaining the file type is more accurate than passing the byte array in some cases. For example, when using jar files, passing the name is more accurate.
     *
     * @param name file name
     * @return When mineType is not recognized, "application/octet-stream" will be returned.
     */
    public static String getMineType(String name) {
        return TIKA.detect(name);
    }

    /**
     * This method is best and most accurate when you have files and data
     *
     * @param data File content
     * @param name file name
     * @return When mineType is not recognized, "application/octet-stream" will be returned.
     */
    public static String getMineType(byte[] data, String name) {
        return TIKA.detect(data, name);
    }

    /**
     * Get file suffix based on mineType
     *
     * Note: If it cannot be obtained or an exception occurs, null will be returned.
     *
     * @param mineType Type
     * @return suffix, for example .pdf
     */
    public static String getExtension(String mineType) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(mineType).getExtension();
        } catch (MimeTypeException e) {
            log.warn("[getExtension][Failed to obtain file suffix ({})]", mineType, e);
            return null;
        }
    }

    /**
     * Return to attachment
     *
     * @param response response
     * @param filename file name
     * @param content  Attachment content
     */
    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) throws IOException {
        // Set header and contentType
        String mineType = getMineType(content, filename);
        response.setContentType(mineType);
        // Set content display and download file name: https://www.cnblogs.com/wq-9/articles/12165056.html
        if (isImage(mineType)) {
            // See https://github.com/YunaiV/ruoyi-vue-pro/issues/692 for discussion
            response.setHeader("Content-Disposition", "inline;filename=" + HttpUtils.encodeUtf8(filename));
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=" + HttpUtils.encodeUtf8(filename));
        }
        // Special processing for video to solve the compatibility problem of video addresses played on mobile terminals
        if (StrUtil.containsIgnoreCase(mineType, "video")) {
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(content.length));
        }
        // Output attachment
        IoUtil.write(response.getOutputStream(), false, content);
    }

    /**
     * Determine whether it is a picture
     *
     * @param mineType Type
     * @return Is it a picture
     */
    public static boolean isImage(String mineType) {
        return StrUtil.startWith(mineType, "image/");
    }

}
