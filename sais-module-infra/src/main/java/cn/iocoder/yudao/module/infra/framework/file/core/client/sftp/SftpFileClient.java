package cn.iocoder.yudao.module.infra.framework.file.core.client.sftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.Sftp;
import cn.iocoder.yudao.framework.common.util.io.FileUtils;
import cn.iocoder.yudao.module.infra.framework.file.core.client.AbstractFileClient;
import com.jcraft.jsch.JSch;

import java.io.File;

/**
 * SFTP file client
 *
 * @author Yudao Source Code
 */
public class SftpFileClient extends AbstractFileClient<SftpFileClientConfig> {

    /**
     * Connection timeout, unit: milliseconds
     */
    private static final Long CONNECTION_TIMEOUT = 3000L;
    /**
     * Read and write timeout, unit: milliseconds
     */
    private static final Long SO_TIMEOUT = 10000L;

    static {
        // Some old SFTP servers only support the ssh-dss protocol. This protocol is not secure and is not supported by default. Add it as needed.
        JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-dss");
    }

    private Sftp sftp;

    public SftpFileClient(Long id, SftpFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // Initialize SFTP object
        FtpConfig ftpConfig = new FtpConfig(config.getHost(), config.getPort(), config.getUsername(), config.getPassword(),
                CharsetUtil.CHARSET_UTF_8, null, null);
        ftpConfig.setConnectionTimeout(CONNECTION_TIMEOUT);
        ftpConfig.setSoTimeout(SO_TIMEOUT);
        this.sftp = new Sftp(ftpConfig);
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        // perform write
        String filePath = getFilePath(path);
        String fileName = FileUtil.getName(filePath);
        String dir = StrUtil.removeSuffix(filePath, fileName);
        File file = FileUtils.createTempFile(content);
        reconnectIfTimeout();
        sftp.mkDirs(dir); // You need to create a parent directory, otherwise an error will be reported
        boolean success = sftp.upload(filePath, file);
        if (!success) {
            throw new JschRuntimeException(StrUtil.format("Failed to upload file to target directory ({})", filePath));
        }
        // splice return path
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        reconnectIfTimeout();
        sftp.delFile(filePath);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        File destFile = FileUtils.createTempFile();
        reconnectIfTimeout();
        sftp.download(filePath, destFile);
        return FileUtil.readBytes(destFile);
    }

    private String getFilePath(String path) {
        return config.getBasePath() + File.separator + path;
    }

    private synchronized void reconnectIfTimeout() {
        sftp.reconnectIfTimeout();
    }

}
