package cn.iocoder.yudao.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Project bootstrap class.
 *
 * If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
 * If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
 * If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
 *
 * @author Yudao Source Code
 */
@SuppressWarnings("SpringComponentScan") // Ignore IDEA not recognizing ${yudao.info.base-package}
@SpringBootApplication(scanBasePackages = {"${yudao.info.base-package}.server", "${yudao.info.base-package}.module"})
public class SaisBootstrapApplication {

    public static void main(String[] args) {
        // If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
        // If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
        // If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.

        SpringApplication.run(SaisBootstrapApplication.class, args);
//        new SpringApplicationBuilder(SaisBootstrapApplication.class)
//                .applicationStartup(new BufferingApplicationStartup(20480))
//                .run(args);

        // If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
        // If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
        // If you encounter startup issues, carefully read https://doc.iocoder.cn/quick-start/.
    }

}
