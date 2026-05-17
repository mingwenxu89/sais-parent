package cn.iocoder.yudao.framework.common.util.spring;

import cn.hutool.extra.spring.SpringUtil;

import java.util.Objects;

/**
 * Spring tool class
 *
 * @author Yudao Source Code
 */
public class SpringUtils extends SpringUtil {

 /**
     * Is it a production environment?
 *
     * @return Is it a production environment?
 */
 public static boolean isProd() {
 String activeProfile = getActiveProfile();
 return Objects.equals("prod", activeProfile);
 }

}
