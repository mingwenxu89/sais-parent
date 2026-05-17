package cn.iocoder.yudao;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.io.File.separator;

/**
 * Project modifier that replaces Maven groupId, artifactId, project package, and similar values in one step.
 * <p>
 * Modify the groupIdNew, artifactIdNew, and projectBaseDirNew variables.
 *
 * @author Yudao Source Code
 */
@Slf4j
public class ProjectReactor {

    private static final String GROUP_ID = "cn.iocoder.boot";
    private static final String ARTIFACT_ID = "yudao";
    private static final String PACKAGE_NAME = "cn.iocoder.yudao";
    private static final String TITLE = "Yudao Management System";

    /**
     * Whitelisted files are not rewritten to avoid problems.
     */
    private static final Set<String> WHITE_FILE_TYPES = SetUtils.asSet("gif", "jpg", "svg", "png", // Images
            "eot", "woff2", "ttf", "woff",  // Fonts
            "xdb"); // IP database

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String projectBaseDir = getProjectBaseDir();
        log.info("[main][Original project path ({})]", projectBaseDir);

        // ========== Configuration, modify manually as needed ==========
        String groupIdNew = "cn.star.gg";
        String artifactIdNew = "star";
        String packageNameNew = "cn.start.pp";
        String titleNew = "Potato Management System";
        String projectBaseDirNew = projectBaseDir + "-new"; // Directory of the new project after one-click renaming
        log.info("[main][Checking whether new project directory ({}) exists]", projectBaseDirNew);
        if (FileUtil.exist(projectBaseDirNew)) {
            log.error("[main][New project directory check failed: ({}) already exists. Change to a new directory. Program exits]", projectBaseDirNew);
            return;
        }
        // If the new directory contains PACKAGE_NAME, ARTIFACT_ID, or similar keywords, the path will be replaced and generated files may not be in the expected directory.
        if (StrUtil.containsAny(projectBaseDirNew, PACKAGE_NAME, ARTIFACT_ID, StrUtil.upperFirst(ARTIFACT_ID))) {
            log.error("[main][New project directory `projectBaseDirNew` check failed: ({}) contains conflicting name \"{}\" or \"{}\". Change to a new directory. Program exits]",
                    projectBaseDirNew, PACKAGE_NAME, ARTIFACT_ID);
            return;
        }
        log.info("[main][New project directory check completed. New project path ({})]", projectBaseDirNew);
        // Get files to copy
        log.info("[main][Start collecting files to rewrite. Estimated time: 10-20 seconds]");
        Collection<File> files = listFiles(projectBaseDir);
        log.info("[main][Number of files to rewrite: {}. Estimated time: 15-30 seconds]", files.size());
        // Write files
        files.forEach(file -> {
            // If the file type is whitelisted, copy it directly without rewriting.
            String fileType = getFileType(file);
            if (WHITE_FILE_TYPES.contains(fileType)) {
                copyFile(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
                return;
            }
            // For non-whitelisted file types, rewrite the content before generating the file.
            String content = replaceFileContent(file, groupIdNew, artifactIdNew, packageNameNew, titleNew);
            writeFile(file, content, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        });
        log.info("[main][Rewrite completed] Total time: {} seconds", (System.currentTimeMillis() - start) / 1000);
    }

    private static String getProjectBaseDir() {
        String baseDir = System.getProperty("user.dir");
        if (StrUtil.isEmpty(baseDir)) {
            throw new NullPointerException("Project base path does not exist");
        }
        return baseDir;
    }

    private static Collection<File> listFiles(String projectBaseDir) {
        Collection<File> files = FileUtil.loopFiles(projectBaseDir);
        // Remove IDEA files, Git files, and files compiled by Node.
        files = files.stream()
                .filter(file -> !file.getPath().contains(separator + "target" + separator)
                        && !file.getPath().contains(separator + "node_modules" + separator)
                        && !file.getPath().contains(separator + ".idea" + separator)
                        && !file.getPath().contains(separator + ".git" + separator)
                        && !file.getPath().contains(separator + "dist" + separator)
                        && !file.getPath().contains(".iml")
                        && !file.getPath().contains(".html.gz"))
                .collect(Collectors.toList());
        return files;
    }

    private static String replaceFileContent(File file, String groupIdNew,
                                             String artifactIdNew, String packageNameNew,
                                             String titleNew) {
        String content = FileUtil.readString(file, StandardCharsets.UTF_8);
        // If the file type is whitelisted, do not rewrite it.
        String fileType = getFileType(file);
        if (WHITE_FILE_TYPES.contains(fileType)) {
            return content;
        }
        // Rewrite all file content.
        return content.replaceAll(GROUP_ID, groupIdNew)
                .replaceAll(PACKAGE_NAME, packageNameNew)
                .replaceAll(ARTIFACT_ID, artifactIdNew) // Must be replaced last because ARTIFACT_ID is too short.
                .replaceAll(StrUtil.upperFirst(ARTIFACT_ID), StrUtil.upperFirst(artifactIdNew))
                .replaceAll(TITLE, titleNew);
    }

    private static void writeFile(File file, String fileContent, String projectBaseDir,
                                  String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        FileUtil.writeUtf8String(fileContent, newPath);
    }

    private static void copyFile(File file, String projectBaseDir,
                                 String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        String newPath = buildNewFilePath(file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew);
        FileUtil.copyFile(file, new File(newPath));
    }

    private static String buildNewFilePath(File file, String projectBaseDir,
                                           String projectBaseDirNew, String packageNameNew, String artifactIdNew) {
        return file.getPath().replace(projectBaseDir, projectBaseDirNew) // New directory
                .replace(PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(separator)),
                        packageNameNew.replaceAll("\\.", Matcher.quoteReplacement(separator)))
                .replace(ARTIFACT_ID, artifactIdNew) //
                .replaceAll(StrUtil.upperFirst(ARTIFACT_ID), StrUtil.upperFirst(artifactIdNew));
    }

    private static String getFileType(File file) {
        return file.length() > 0 ? FileTypeUtil.getType(file) : "";
    }

}
