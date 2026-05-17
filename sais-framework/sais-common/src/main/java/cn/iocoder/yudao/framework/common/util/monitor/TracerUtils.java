package cn.iocoder.yudao.framework.common.util.monitor;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * Link tracking tools
 *
 * Considering that every starter needs to use this tool class, it is placed under the util package under the common module.
 *
 * @author Yudao Source Code
 */
public class TracerUtils {

 /**
     * Private constructor
 */
 private TracerUtils() {
 }

 /**
     * Obtain the link tracking number and directly return SkyWalking's TraceId.
     * If it does not exist, it will be an empty string! ! !
 *
     * @return link tracking number
 */
 public static String getTraceId() {
 return TraceContext.traceId();
 }

}
