package cn.iocoder.yudao.framework.datasource.core.enums;

/**
 * Corresponds to different data source configurations in multiple data sources
 *
 * Set the data source used by using the {@link com.baomidou.dynamic.datasource.annotation.DS} annotation on the method.
 * Note that the default is the {@link #MASTER} data source
 *
 * The corresponding official document is http://dynamic-datasource.com/guide/customize/Annotation.html
 */
public interface DataSourceEnum {

 /**
     * Main library, it is recommended to use {@link com.baomidou.dynamic.datasource.annotation.Master} annotation
 */
 String MASTER = "master";
 /**
     * From the library, it is recommended to use the {@link com.baomidou.dynamic.datasource.annotation.Slave} annotation
 */
 String SLAVE = "slave";

}
