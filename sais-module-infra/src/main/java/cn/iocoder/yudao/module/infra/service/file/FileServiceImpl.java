package cn.iocoder.yudao.module.infra.service.file;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePresignedUrlRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileMapper;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClient;
import cn.iocoder.yudao.module.infra.framework.file.core.utils.FileTypeUtils;
import com.google.common.annotations.VisibleForTesting;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.hutool.core.date.DatePattern.PURE_DATE_PATTERN;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

/**
 * File Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * The prefix of the uploaded file, whether it contains date (yyyyMMdd)
     *
     * Purpose: divide into categories according to date
     */
    static boolean PATH_PREFIX_DATE_ENABLE = true;
    /**
     * The suffix of the uploaded file, whether it contains a timestamp
     *
     * Purpose: Ensure the uniqueness of files and avoid overwriting
     * Customization: can be adjusted to UUID or other methods as needed
     */
    static boolean PATH_SUFFIX_TIMESTAMP_ENABLE = true;

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(byte[] content, String name, String directory, String type) {
        // 1.1 Handling the situation when type is empty
        if (StrUtil.isEmpty(type)) {
            type = FileTypeUtils.getMineType(content, name);
        }
        // 1.2 Handling the situation when name is empty
        if (StrUtil.isEmpty(name)) {
            name = DigestUtil.sha256Hex(content);
        }
        if (StrUtil.isEmpty(FileUtil.extName(name))) {
            // If name does not have the suffix type, add the suffix
            String extension = FileTypeUtils.getExtension(type);
            if (StrUtil.isNotEmpty(extension)) {
                name = name + extension;
            }
        }

        // 2.1 Generate the upload path, which needs to be unique
        String path = generateUploadPath(name, directory);
        // 2.2 Upload to file storage
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "Client (master) cannot be empty");
        String url = client.upload(content, path, type);

        // 3. Save to database
        fileMapper.insert(new FileDO().setConfigId(client.getId())
                .setName(name).setPath(path).setUrl(url)
                .setType(type).setSize((long) content.length));
        return url;
    }

    @VisibleForTesting
    String generateUploadPath(String name, String directory) {
        // 1. Generate prefixes and suffixes
        String prefix = null;
        if (PATH_PREFIX_DATE_ENABLE) {
            prefix = LocalDateTimeUtil.format(LocalDateTimeUtil.now(), PURE_DATE_PATTERN);
        }
        String suffix = null;
        if (PATH_SUFFIX_TIMESTAMP_ENABLE) {
            suffix = String.valueOf(System.currentTimeMillis());
        }

        // 2.1 Splice the suffix suffix first
        if (StrUtil.isNotEmpty(suffix)) {
            String ext = FileUtil.extName(name);
            if (StrUtil.isNotEmpty(ext)) {
                name = FileUtil.mainName(name) + StrUtil.C_UNDERLINE + suffix + StrUtil.DOT + ext;
            } else {
                name = name + StrUtil.C_UNDERLINE + suffix;
            }
        }
        // 2.2 Re-splicing prefix prefix
        if (StrUtil.isNotEmpty(prefix)) {
            name = prefix + StrUtil.SLASH + name;
        }
        // 2.3 Finally splice the directory directory
        if (StrUtil.isNotEmpty(directory)) {
            name = directory + StrUtil.SLASH + name;
        }
        return name;
    }

    @Override
    @SneakyThrows
    public FilePresignedUrlRespVO presignPutUrl(String name, String directory) {
        // 1. Generate the upload path, which needs to be unique
        String path = generateUploadPath(name, directory);

        // 2. Obtain the file pre-signed address
        FileClient fileClient = fileConfigService.getMasterFileClient();
        String uploadUrl = fileClient.presignPutUrl(path);
        String visitUrl = fileClient.presignGetUrl(path, null);
        return new FilePresignedUrlRespVO().setConfigId(fileClient.getId())
                .setPath(path).setUploadUrl(uploadUrl).setUrl(visitUrl);
    }

    @Override
    public String presignGetUrl(String url, Integer expirationSeconds) {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        return fileClient.presignGetUrl(url, expirationSeconds);
    }

    @Override
    public Long createFile(FileCreateReqVO createReqVO) {
        createReqVO.setUrl(HttpUtils.removeUrlQuery(createReqVO.getUrl())); // Purpose: Remove the signature parameters of the URL in the case of private buckets
        FileDO file = BeanUtils.toBean(createReqVO, FileDO.class);
        fileMapper.insert(file);
        return file.getId();
    }

    @Override
    public FileDO getFile(Long id) {
        return validateFileExists(id);
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // Check existence
        FileDO file = validateFileExists(id);

        // Delete from file storage
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "Client({}) cannot be empty", file.getConfigId());
        client.delete(file.getPath());

        // delete record
        fileMapper.deleteById(id);
    }

    @Override
    @SneakyThrows
    public void deleteFileList(List<Long> ids) {
        // Delete files
        List<FileDO> files = fileMapper.selectByIds(ids);
        for (FileDO file : files) {
            // Get client
            FileClient client = fileConfigService.getFileClient(file.getConfigId());
            Assert.notNull(client, "Client({}) cannot be empty", file.getPath());
            // Delete files
            client.delete(file.getPath());
        }

        // delete record
        fileMapper.deleteByIds(ids);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "Client({}) cannot be empty", configId);
        return client.getContent(path);
    }

}
