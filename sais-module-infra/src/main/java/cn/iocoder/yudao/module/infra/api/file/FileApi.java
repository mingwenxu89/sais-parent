package cn.iocoder.yudao.module.infra.api.file;

import jakarta.validation.constraints.NotEmpty;

/**
 * File API API
 *
 * @author Yudao Source Code
 */
public interface FileApi {

    /**
     * Save the file and return the access path of the file
     *
     * @param content File content
     * @return file path
     */
    default String createFile(byte[] content) {
        return createFile(content, null, null, null);
    }

    /**
     * Save the file and return the access path of the file
     *
     * @param content File content
     * @param name File name, empty allowed
     * @return file path
     */
    default String createFile(byte[] content, String name) {
        return createFile(content, name, null, null);
    }

    /**
     * Save the file and return the access path of the file
     *
     * @param content File content
     * @param name File name, empty allowed
     * @param directory Directory, empty allowed
     * @param type MIME type of the file, empty is allowed
     * @return file path
     */
    String createFile(@NotEmpty(message = "File content cannot be empty") byte[] content,
                      String name, String directory, String type);

    /**
     * Generate file pre-signed address for reading
     *
     * @param url Complete file access address
     * @param expirationSeconds Access validity period, in seconds
     * @return File pre-signed address
     */
    String presignGetUrl(@NotEmpty(message = "URL cannot be empty") String url,
                         Integer expirationSeconds);

}
