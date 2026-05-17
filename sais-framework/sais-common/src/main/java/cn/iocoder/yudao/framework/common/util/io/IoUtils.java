package cn.iocoder.yudao.framework.common.util.io;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

import java.io.InputStream;

/**
 * IO tool class for {@link cn.hutool.core.io.IoUtil} missing methods
 *
 * @author Yudao Source Code
 */
public class IoUtils {

 /**
     * Read UTF8 encoded content from the stream
 *
     * @param in input stream
     * @param isClose Whether to close
     * @return content
     * @throws IORuntimeException IO exception
 */
 public static String readUtf8(InputStream in, boolean isClose) throws IORuntimeException {
 return StrUtil.utf8Str(IoUtil.read(in, isClose));
 }

}
