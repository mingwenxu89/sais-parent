package cn.iocoder.yudao.framework.common.util.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.SneakyThrows;

import java.io.File;

/**
 * File tool class
 *
 * @author Yudao Source Code
 */
public class FileUtils {

 /**
     * Create temporary files
     * This file will be deleted when the JVM exits
 *
     * @param data File content
     * @return document
 */
 @SneakyThrows
 public static File createTempFile(String data) {
 File file = createTempFile();
        // Write content
 FileUtil.writeUtf8String(data, file);
 return file;
 }

 /**
     * Create temporary files
     * This file will be deleted when the JVM exits
 *
     * @param data File content
     * @return document
 */
 @SneakyThrows
 public static File createTempFile(byte[] data) {
 File file = createTempFile();
        // Write content
 FileUtil.writeBytes(data, file);
 return file;
 }

 /**
     * Temporary file created with no content
     * This file will be deleted when the JVM exits
 *
     * @return document
 */
 @SneakyThrows
 public static File createTempFile() {
        // Create files, guaranteed to be unique by UUID
 File file = File.createTempFile(IdUtil.simpleUUID(), null);
        // Mark the JVM to be automatically deleted when it exits
 file.deleteOnExit();
 return file;
 }

}
