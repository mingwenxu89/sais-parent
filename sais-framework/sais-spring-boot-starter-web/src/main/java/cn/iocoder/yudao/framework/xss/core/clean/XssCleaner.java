package cn.iocoder.yudao.framework.xss.core.clean;

/**
 * Clean data with Xss risk in html text
 */
public interface XssCleaner {

 /**
     * Clean text at risk of Xss
 *
     * @param html original html
     * @return Cleaned html
 */
 String clean(String html);

}
