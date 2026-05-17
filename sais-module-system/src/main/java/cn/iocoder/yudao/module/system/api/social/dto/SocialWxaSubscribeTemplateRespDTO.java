package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;


/**
 * Mini program subscription message template Response DTO
 *
 * @author HUIHUI
 */
@Data
public class SocialWxaSubscribeTemplateRespDTO {

    /**
     * Template ID
     */
    private String id;

    /**
     * Template title
     */
    private String title;

    /**
     * Template content
     */
    private String content;

    /**
     * Template content example
     */
    private String example;

    /**
     * template type
     *
     * 2: For one-time subscription
     * 3: For long-term subscription
     */
    private Integer type;

}
