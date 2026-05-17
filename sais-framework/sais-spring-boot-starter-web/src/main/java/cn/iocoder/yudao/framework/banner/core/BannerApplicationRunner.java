package cn.iocoder.yudao.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * After the project is successfully launched, provide the address related to the document
 *
 * @author Yudao Source Code
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

 @Override
 public void run(ApplicationArguments args) {
 }

 private static boolean isNotPresent(String className) {
 return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
 }

}
