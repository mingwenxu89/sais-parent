package cn.iocoder.yudao.module.system.enums.social;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Type enum for social platforms
 *
 * @author Yudao Source Code
 */
@Getter
@AllArgsConstructor
public enum SocialTypeEnum implements ArrayValuable<Integer> {

    /**
     * Gitee
     *
     * @see <a href="https://gitee.com/API/v5/oauth_doc#/">Access Document</a>
     */
    GITEE(10, "GITEE"),
    /**
     * DingTalk
     *
     * @see <a href="https://developers.dingtalk.com/document/app/obtain-identity-credentials">Access Document</a>
     */
    DINGTALK(20, "DINGTALK"),

    /**
     * Enterprise WeChat
     *
     * @see <a href="https://xkcoding.com/2019/08/06/use-justauth-integration-WeChat-enterprise.html">Access documentation</a>
     */
    WECHAT_ENTERPRISE(30, "WECHAT_ENTERPRISE"),
    /**
     * WeChat Public Platform - Mobile H5
     *
     * @see <a href="https://www.cnblogs.com/juewuzhe/p/11905461.html">Access documents</a>
     */
    WECHAT_MP(31, "WECHAT_MP"),
    /**
     * WeChat Open Platform - Website Application PC Scan QR Code to Authorize Login
     *
     * @see <a href="https://justauth.wiki/guide/oauth/wechat_open/#_2-Application for Developer Qualification Certification">Access Document</a>
     */
    WECHAT_OPEN(32, "WECHAT_OPEN"),
    /**
     * WeChat applet
     *
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html">Access documentation</a>
     */
    WECHAT_MINI_PROGRAM(34, "WECHAT_MINI_PROGRAM"),
    /**
     * Alipay applet
     *
     * @see <a href="https://opendocs.alipay.com/mini/05dxgc?pathHash=1a3ecb13">Access documentation</a>
     */
    ALIPAY_MINI_PROGRAM(40, "ALIPAY"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SocialTypeEnum::getType).toArray(Integer[]::new);

    /**
     * Type
     */
    private final Integer type;
    /**
     * type identifier
     */
    private final String source;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SocialTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
