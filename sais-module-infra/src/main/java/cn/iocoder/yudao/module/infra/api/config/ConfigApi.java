package cn.iocoder.yudao.module.infra.api.config;

/**
 * Parameter configuration API API
 *
 * @author Yudao Source Code
 */
public interface ConfigApi {

    /**
     * Query parameter value based on parameter key
     *
     * @param key parameter key
     * @return Parameter value
     */
    String getConfigValueByKey(String key);

}
