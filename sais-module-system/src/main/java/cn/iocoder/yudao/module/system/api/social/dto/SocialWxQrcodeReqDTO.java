package cn.iocoder.yudao.module.system.api.social.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * Get applet code Request DTO
 *
 * @author HUIHUI
 * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html">Get unlimited miniapp code</a>
 */
@Data
public class SocialWxQrcodeReqDTO {

    /**
     * The page path cannot carry parameters (please put the parameters in the scene field)
     */
    public static final String SCENE = "";
    /**
     * QR code width
     */
    public static final Integer WIDTH = 430;
    /**
     * Automatically configure the line color. If the color is still black, it means that it is not recommended to configure the main color.
     */
    public static final Boolean AUTO_COLOR = true;
    /**
     * Check if page exists
     */
    public static final Boolean CHECK_PATH = true;
    /**
     * DO you need a transparent background?
     *
     * When hyaline is true, a small program code with a transparent background is generated.
     */
    public static final Boolean HYALINE = true;

    /**
     * scene
     */
    @NotEmpty(message = "Scene cannot be empty")
    private String scene;
    /**
     * Page path
     */
    @NotEmpty(message = "Page path cannot be empty")
    private String path;
    /**
     * QR code width
     */
    private Integer width;

    /**
     * DO you need a transparent background?
     */
    private Boolean autoColor;
    /**
     * Whether to check whether page exists
     */
    private Boolean checkPath;
    /**
     * DO you need a transparent background?
     */
    private Boolean hyaline;

}
