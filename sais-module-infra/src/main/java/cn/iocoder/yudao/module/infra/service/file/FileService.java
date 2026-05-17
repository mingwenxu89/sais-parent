package cn.iocoder.yudao.module.infra.service.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePresignedUrlRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * File Service API
 *
 * @author Yudao Source Code
 */
public interface FileService {

    /**
     * Get file pagination
     *
     * @param pageReqVO Page query
     * @return File paging
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * Save the file and return the access path of the file
     *
     * @param content   File content
     * @param name      File name, empty allowed
     * @param directory Directory, empty allowed
     * @param type      MIME type of the file, empty is allowed
     * @return file path
     */
    String createFile(@NotEmpty(message = "File content cannot be empty") byte[] content,
                      String name, String directory, String type);

    /**
     * Generate file pre-signed address information for uploading
     *
     * @param name      file name
     * @param directory Directory
     * @return Pre-signed address information
     */
    FilePresignedUrlRespVO presignPutUrl(@NotEmpty(message = "File name cannot be empty") String name,
                                         String directory);
    /**
     * Generate file pre-signed address information for reading
     *
     * @param url Complete file access address
     * @param expirationSeconds Access validity period, in seconds
     * @return File pre-signed address
     */
    String presignGetUrl(String url, Integer expirationSeconds);

    /**
     * Create file
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createFile(FileCreateReqVO createReqVO);
    FileDO getFile(Long id);

    /**
     * Delete files
     *
     * @param id ID
     */
    void deleteFile(Long id) throws Exception;

    /**
     * Delete files in batches
     *
     * @param ids IDed list
     */
    void deleteFileList(List<Long> ids) throws Exception;

    /**
     * Get file content
     *
     * @param configId Configuration ID
     * @param path     file path
     * @return File content
     */
    byte[] getFileContent(Long configId, String path) throws Exception;

}
