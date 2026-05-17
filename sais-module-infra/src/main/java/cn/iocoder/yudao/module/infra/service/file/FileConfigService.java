package cn.iocoder.yudao.module.infra.service.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.config.FileConfigPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.config.FileConfigSaveReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileConfigDO;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClient;
import jakarta.validation.Valid;

import java.util.List;

/**
 * File configuration Service API
 *
 * @author Yudao Source Code
 */
public interface FileConfigService {

    /**
     * Create file configuration
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createFileConfig(@Valid FileConfigSaveReqVO createReqVO);

    /**
     * Update file configuration
     *
     * @param updateReqVO Update information
     */
    void updateFileConfig(@Valid FileConfigSaveReqVO updateReqVO);

    /**
     * Update file configuration as Master
     *
     * @param id ID
     */
    void updateFileConfigMaster(Long id);

    /**
     * Delete file configuration
     *
     * @param id ID
     */
    void deleteFileConfig(Long id);

    /**
     * Deleting file configurations in batches
     *
     * @param ids IDed list
     */
    void deleteFileConfigList(List<Long> ids);

    /**
     * Get file configuration
     *
     * @param id ID
     * @return File configuration
     */
    FileConfigDO getFileConfig(Long id);

    /**
     * Get file configuration pagination
     *
     * @param pageReqVO Page query
     * @return File configuration paging
     */
    PageResult<FileConfigDO> getFileConfigPage(FileConfigPageReqVO pageReqVO);

    /**
     * Test whether the file configuration is correct by uploading the file
     *
     * @param id ID
     * @return File URL
     */
    String testFileConfig(Long id) throws Exception;

    /**
     * Get the file client with the specified ID
     *
     * @param id Configuration ID
     * @return file client
     */
    FileClient getFileClient(Long id);

    /**
     * Get the Master file client
     *
     * @return file client
     */
    FileClient getMasterFileClient();

}
