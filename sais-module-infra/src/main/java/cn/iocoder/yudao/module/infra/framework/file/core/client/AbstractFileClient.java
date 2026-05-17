package cn.iocoder.yudao.module.infra.framework.file.core.client;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class of file client, providing template methods to reduce redundant code in subclasses
 *
 * @author Yudao Source Code
 */
@Slf4j
public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {

    /**
     * Configuration ID
     */
    private final Long id;
    /**
     * File configuration
     */
    protected Config config;
    /**
     * original file configuration
     *
     * Reason: {@link #config} may be modified by subclasses and cannot be used to determine whether the configuration has changed.
     * @link <a href="https://t.zsxq.com/29wkW">Related cases</a>
     */
    private Config originalConfig;

    public AbstractFileClient(Long id, Config config) {
        this.id = id;
        this.config = config;
        this.originalConfig = config;
    }

    /**
     * initialization
     */
    public final void init() {
        doInit();
        log.debug("[init][Configuration ({}) initialization completed]", config);
    }

    /**
     * Custom initialization
     */
    protected abstract void doInit();

    public final void refresh(Config config) {
        // Determine whether to update
        if (config.equals(this.originalConfig)) {
            return;
        }
        log.info("[refresh][Configuration ({}) changes, reinitialize]", config);
        this.config = config;
        this.originalConfig = config;
        // initialization
        this.init();
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * URL access address of the formatted file
     * Usage scenarios: local, FTP, DB, obtain file content through getFile of FileController
     *
     * @param domain Custom domain name
     * @param path file path
     * @return URL access address
     */
    protected String formatFileUrl(String domain, String path) {
        return StrUtil.format("{}/admin-api/infra/file/{}/get/{}", domain, getId(), path);
    }

}
