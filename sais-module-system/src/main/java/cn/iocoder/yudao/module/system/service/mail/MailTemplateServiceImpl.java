package cn.iocoder.yudao.module.system.service.mail;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.template.MailTemplatePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.template.MailTemplateSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailTemplateDO;
import cn.iocoder.yudao.module.system.dal.mysql.mail.MailTemplateMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MAIL_TEMPLATE_CODE_EXISTS;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.MAIL_TEMPLATE_NOT_EXISTS;

/**
 * Email template Service implementation class
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
@Service
@Validated
@Slf4j
public class MailTemplateServiceImpl implements MailTemplateService {

    /**
     * Regular expression to match variables in {}
     */
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");

    @Resource
    private MailTemplateMapper mailTemplateMapper;

    @Override
    public Long createMailTemplate(MailTemplateSaveReqVO createReqVO) {
        // Verify whether the code is unique
        validateCodeUnique(null, createReqVO.getCode());

        // Insert
        MailTemplateDO template = BeanUtils.toBean(createReqVO, MailTemplateDO.class)
                .setParams(parseTemplateTitleAndContentParams(createReqVO.getTitle(), createReqVO.getContent()));
        mailTemplateMapper.insert(template);
        return template.getId();
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.MAIL_TEMPLATE,
            allEntries = true) // allEntries Clear all caches, because the code field may be modified and it is difficult to clean.
    public void updateMailTemplate(@Valid MailTemplateSaveReqVO updateReqVO) {
        // Check if it exists
        validateMailTemplateExists(updateReqVO.getId());
        // Verify whether the code is unique
        validateCodeUnique(updateReqVO.getId(),updateReqVO.getCode());

        // Update
        MailTemplateDO updateObj = BeanUtils.toBean(updateReqVO, MailTemplateDO.class)
                .setParams(parseTemplateTitleAndContentParams(updateReqVO.getTitle(), updateReqVO.getContent()));
        mailTemplateMapper.updateById(updateObj);
    }

    @VisibleForTesting
    void validateCodeUnique(Long id, String code) {
        MailTemplateDO template = mailTemplateMapper.selectByCode(code);
        if (template == null) {
            return;
        }
        // When template record exists
        if (id == null // When adding, the description is repeated
                || ObjUtil.notEqual(id, template.getId())) { // When updating, if the ids are inconsistent, it means duplication
            throw exception(MAIL_TEMPLATE_CODE_EXISTS);
        }
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.MAIL_TEMPLATE,
            allEntries = true) // allEntries Clear all caches, because the id is not a direct cache code and is not easy to clean.
    public void deleteMailTemplate(Long id) {
        // Check if it exists
        validateMailTemplateExists(id);

        // Delete
        mailTemplateMapper.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.MAIL_TEMPLATE,
            allEntries = true) // allEntries Clear all caches, because the id is not a direct cache code and is not easy to clean.
    public void deleteMailTemplateList(List<Long> ids) {
        mailTemplateMapper.deleteByIds(ids);
    }

    private void validateMailTemplateExists(Long id) {
        if (mailTemplateMapper.selectById(id) == null) {
            throw exception(MAIL_TEMPLATE_NOT_EXISTS);
        }
    }

    @Override
    public MailTemplateDO getMailTemplate(Long id) {return mailTemplateMapper.selectById(id);}

    @Override
    @Cacheable(value = RedisKeyConstants.MAIL_TEMPLATE, key = "#code", unless = "#result == null")
    public MailTemplateDO getMailTemplateByCodeFromCache(String code) {
        return mailTemplateMapper.selectByCode(code);
    }

    @Override
    public PageResult<MailTemplateDO> getMailTemplatePage(MailTemplatePageReqVO pageReqVO) {
        return mailTemplateMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MailTemplateDO> getMailTemplateList() {return mailTemplateMapper.selectList();}

    @Override
    public String formatMailTemplateContent(String content, Map<String, Object> params) {
        // 1. Replace template variables first
        String formattedContent = StrUtil.format(content, params);

        // Related Pull Request: https://gitee.com/zhijiantianya/ruoyi-vue-pro/pulls/1461 Discussion
        // 2.1 Anti-escape HTML special characters
        formattedContent = unescapeHtml(formattedContent);
        // 2.2 Process code blocks (make sure the <pre><code> tags are in the correct format)
        formattedContent = formatHtmlCodeBlocks(formattedContent);
        // 2.3 Replace the outermost pre tag with a div tag
        formattedContent = replaceOuterPreWithDiv(formattedContent);
        return formattedContent;
    }

    private String replaceOuterPreWithDiv(String content) {
        if (StrUtil.isEmpty(content)) {
            return content;
        }
        // Use regular expressions to match all <pre> tags, including nested <code> tags
        String regex = "(?s)<pre[^>]*>(.*?)</pre>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // Extract the content within the <pre> tag
            String innerContent = matcher.group(1);
            // Returns the content wrapped by the div tag
            matcher.appendReplacement(sb, "<div>" + innerContent + "</div>");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Unescape HTML special characters
     *
     * @param input input string
     * @return Unescaped string
     */
    private String unescapeHtml(String input) {
        if (StrUtil.isEmpty(input)) {
            return input;
        }
        return input
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ");
    }

    /**
     * Format code blocks in HTML
     *
     * @param content Email content
     * @return Formatted email content
     */
    private String formatHtmlCodeBlocks(String content) {
        // Code blocks matching <pre><code> tags
        Pattern codeBlockPattern = Pattern.compile("<pre\\s*.*?><code\\s*.*?>(.*?)</code></pre>", Pattern.DOTALL);
        Matcher matcher = codeBlockPattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // Get code block content
            String codeBlock = matcher.group(1);
            // Add styles to code blocks
            String replacement = "<pre style=\"background-color: #f5f5f5; padding: 10px; border-radius: 5px; overflow-x: auto;\"><code>" + codeBlock + "</code></pre>";
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public long getMailTemplateCountByAccountId(Long accountId) {
        return mailTemplateMapper.selectCountByAccountId(accountId);
    }

    /**
     * Parse parameters in title and content
     */
    @VisibleForTesting
    public List<String> parseTemplateTitleAndContentParams(String title, String content) {
        List<String> titleParams = ReUtil.findAllGroup1(PATTERN_PARAMS, title);
        List<String> contentParams = ReUtil.findAllGroup1(PATTERN_PARAMS, content);
        // mergeparametersand remove duplicates
        List<String> allParams = new ArrayList<>(titleParams);
        for (String param : contentParams) {
            if (!allParams.contains(param)) {
                allParams.add(param);
            }
        }
        return allParams;
    }

    /**
     * Get the parameters in the email template, in the form of {key}
     *
     * @param content Content
     * @return Parameter list
     */
    List<String> parseTemplateContentParams(String content) {
        return ReUtil.findAllGroup1(PATTERN_PARAMS, content);
    }

}