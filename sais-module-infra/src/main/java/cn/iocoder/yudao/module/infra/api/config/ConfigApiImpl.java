package cn.iocoder.yudao.module.infra.api.config;

import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

/**
 * Parameter configuration API implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
public class ConfigApiImpl implements ConfigApi {

    @Resource
    private ConfigService configService;

    @Override
    public String getConfigValueByKey(String key) {
        ConfigDO config = configService.getConfigByKey(key);
        return config != null ? config.getValue() : null;
    }

}
