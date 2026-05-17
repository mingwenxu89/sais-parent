package cn.iocoder.yudao.framework.quartz.core.handler;

/**
 * task processor
 *
 * @author Yudao Source Code
 */
public interface JobHandler {

    /**
     * perform tasks
     *
     * @param param parameter
     * @return result
     * @throws Exception Exception
     */
    String execute(String param) throws Exception;

}
