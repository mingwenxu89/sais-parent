package cn.iocoder.yudao.module.infra.framework.file.core.client;

/**
 * file client
 *
 * @author Yudao Source Code
 */
public interface FileClient {

    /**
     * Get client ID
     *
     * @return client ID
     */
    Long getId();

    /**
     * Upload file
     *
     * @param content file stream
     * @param path    relative path
     * @return Full path, i.e. HTTP access address
     * @throws Exception When uploading a file, an Exception is thrown.
     */
    String upload(byte[] content, String path, String type) throws Exception;

    /**
     * Delete files
     *
     * @param path relative path
     * @throws Exception When deleting a file, an Exception exception is thrown
     */
    void delete(String path) throws Exception;

    /**
     * Get the contents of the file
     *
     * @param path relative path
     * @return file content
     */
    byte[] getContent(String path) throws Exception;

    // ========== File signature, currently only supported by S3 ==========

    /**
     * Obtain the file pre-signed address for uploading
     *
     * @param path relative path
     * @return File pre-signed address
     */
    default String presignPutUrl(String path) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /**
     * Generate file pre-signed address for reading
     *
     * @param url Complete file access address
     * @param expirationSeconds Access validity period, in seconds
     * @return File pre-signed address
     */
    default String presignGetUrl(String url, Integer expirationSeconds) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

}
