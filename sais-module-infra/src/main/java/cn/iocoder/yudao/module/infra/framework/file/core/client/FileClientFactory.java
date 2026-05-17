package cn.iocoder.yudao.module.infra.framework.file.core.client;

import cn.iocoder.yudao.module.infra.framework.file.core.enums.FileStorageEnum;

public interface FileClientFactory {

    /**
     * Get file client
     *
     * @param configId Configuration ID
     * @return file client
     */
    FileClient getFileClient(Long configId);

    /**
     * Create file client
     *
     * @param configId Configuration ID
     * @param storage Storage enumeration {@link FileStorageEnum}
     * @param config File configuration
     */
    <Config extends FileClientConfig> void createOrUpdateFileClient(Long configId, Integer storage, Config config);

}
