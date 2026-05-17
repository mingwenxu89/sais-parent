package cn.iocoder.yudao.framework.ip.core.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.iocoder.yudao.framework.ip.core.Area;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;

/**
 * IP tools
 *
 * The IP data source comes from the lite version of ip2region.xDB, based on the <a href="https://gitee.com/zhijiantianya/ip2region"/> project
 *
 * @author wanglhup
 */
@Slf4j
public class IPUtils {

 /**
     * Initialize SEARCHER
 */
 @SuppressWarnings("InstantiationOfUtilityClass")
 private final static IPUtils INSTANCE = new IPUtils();

 /**
     * IP queryer, start loading into memory
 */
 private static Searcher SEARCHER;

 /**
     * privatization structure
 */
 private IPUtils() {
 try {
 long now = System.currentTimeMillis();
 byte[] bytes = ResourceUtil.readBytes("ip2region.xdb");
 SEARCHER = Searcher.newWithBuffer(bytes);
            log.info("Started loading IPUtils successfully, taking ({}) milliseconds", System.currentTimeMillis() - now);
 } catch (IOException e) {
            log.error("Failed to load IPUtils on startup", e);
 }
 }

 /**
     * Query the area code corresponding to the IP
 *
     * @param ip IP address in the format 127.0.0.1
     * @return region id
 */
 @SneakyThrows
 public static Integer getAreaId(String ip) {
 return Integer.parseInt(SEARCHER.search(ip.trim()));
 }

 /**
     * Query the area code corresponding to the IP
 *
     * @param ip The timestamp of the IP address, the format refers to the return of {@link Searcher#checkIP(String)}
     * @return area code
 */
 @SneakyThrows
 public static Integer getAreaId(long ip) {
 return Integer.parseInt(SEARCHER.search(ip));
 }

 /**
     * Query the region corresponding to the IP
 *
     * @param ip IP address in the format 127.0.0.1
     * @return area
 */
 public static Area getArea(String ip) {
 return AreaUtils.getArea(getAreaId(ip));
 }

 /**
     * Query the region corresponding to the IP
 *
     * @param ip The timestamp of the IP address, the format refers to the return of {@link Searcher#checkIP(String)}
     * @return area
 */
 public static Area getArea(long ip) {
 return AreaUtils.getArea(getAreaId(ip));
 }
}
