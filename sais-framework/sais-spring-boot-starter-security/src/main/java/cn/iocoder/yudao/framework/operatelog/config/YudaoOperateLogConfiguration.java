package cn.iocoder.yudao.framework.operatelog.config;

import cn.iocoder.yudao.framework.operatelog.core.service.LogRecordServiceImpl;
import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Operation log configuration class
 *
 * @author HUIHUI
 */
@EnableLogRecord(tenant = "") // It seems that there is no need for tenant. Please leave this thing empty.
@AutoConfiguration
@Slf4j
public class YudaoOperateLogConfiguration {

 @Bean
 @Primary
 public ILogRecordService iLogRecordServiceImpl() {
 return new LogRecordServiceImpl();
 }

}
