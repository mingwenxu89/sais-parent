package cn.iocoder.yudao.module.infra.controller.admin.file;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.*;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.infra.framework.file.core.utils.FileTypeUtils.writeAttachment;

@Tag(name = "Management backend - file storage")
@RestController
@RequestMapping("/infra/file")
@Validated
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Mode 1: Backend upload files")
    @Parameter(name = "file", description = "file attachment", required = true,
            schema = @Schema(type = "string", format = "binary"))
    public CommonResult<String> uploadFile(@Valid FileUploadReqVO uploadReqVO) throws Exception {
        MultipartFile file = uploadReqVO.getFile();
        byte[] content = IoUtil.readBytes(file.getInputStream());
        return success(fileService.createFile(content, file.getOriginalFilename(),
                uploadReqVO.getDirectory(), file.getContentType()));
    }

    @GetMapping("/presigned-url")
    @Operation(summary = "Get file pre-signed address (upload)", description = "Mode 2: Front-end upload files: Used by the frontend to directly upload file storage such as Qiniu and Alibaba Cloud OSS.")
    @Parameters({
            @Parameter(name = "name", description = "File name", required = true),
            @Parameter(name = "directory", description = "File directory")
    })
    public CommonResult<FilePresignedUrlRespVO> getFilePresignedUrl(
            @RequestParam("name") String name,
            @RequestParam(value = "directory", required = false) String directory) {
        return success(fileService.presignPutUrl(name, directory));
    }

    @PostMapping("/create")
    @Operation(summary = "Create file", description = "Mode 2: Upload files on the front end: Cooperate with the presigned-URL API to record the uploaded files.")
    public CommonResult<Long> createFile(@Valid @RequestBody FileCreateReqVO createReqVO) {
        return success(fileService.createFile(createReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "Get files")
    @Parameter(name = "id", description = "ID", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<FileRespVO> getFile(@RequestParam("id") Long id) {
        return success(BeanUtils.toBean(fileService.getFile(id), FileRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete files")
    @Parameter(name = "id", description = "ID", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:delete')")
    public CommonResult<Boolean> deleteFile(@RequestParam("id") Long id) throws Exception {
        fileService.deleteFile(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "Delete files in batches")
    @Parameter(name = "ids", description = "IDed list", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:delete')")
    public CommonResult<Boolean> deleteFileList(@RequestParam("ids") List<Long> ids) throws Exception {
        fileService.deleteFileList(ids);
        return success(true);
    }

    @GetMapping("/{configId}/get/**")
    @PermitAll
    @TenantIgnore
    @Operation(summary = "Download file")
    @Parameter(name = "configId", description = "Configuration ID", required = true)
    public void getFileContent(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable("configId") Long configId) throws Exception {
        // Get the requested path
        String path = StrUtil.subAfter(request.getRequestURI(), "/get/", false);
        if (StrUtil.isEmpty(path)) {
            throw new IllegalArgumentException("The trailing path must be passed");
        }
        // Decoding to solve the problem of Chinese paths
        // https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/807/
        // https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/1432/
        path = URLUtil.decode(path, StandardCharsets.UTF_8, false);

        // Read content
        byte[] content = fileService.getFileContent(configId, path);
        if (content == null) {
            log.warn("[getFileContent][configId({}) path({}) file does not exist]", configId, path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        writeAttachment(response, path, content);
    }

    @GetMapping("/page")
    @Operation(summary = "Get file pagination")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getFilePage(@Valid FilePageReqVO pageVO) {
        PageResult<FileDO> pageResult = fileService.getFilePage(pageVO);
        return success(BeanUtils.toBean(pageResult, FileRespVO.class));
    }

}
