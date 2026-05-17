package cn.iocoder.yudao.framework.operatelog.core.service;

import cn.iocoder.yudao.framework.common.biz.system.logger.OperateLogCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Operation log ILogRecordService implementation class
 *
 * Implemented based on {@link OperateLogCommonAPI} to record operation logs
 *
 * @author HUIHUI
 */
@Slf4j
public class LogRecordServiceImpl implements ILogRecordService {

 @Resource
 private OperateLogCommonApi operateLogApi;

 @Override
 public void record(LogRecord logRecord) {
 OperateLogCreateReqDTO reqDTO = new OperateLogCreateReqDTO();
 try {
 reqDTO.setTraceId(TracerUtils.getTraceId());
            // Supplement user information
 fillUserFields(reqDTO);
            // Complete module information
 fillModuleFields(reqDTO, logRecord);
            // Completion request information
 fillRequestFields(reqDTO);

            // 2. Asynchronous logging
 operateLogApi.createOperateLogAsync(reqDTO);
 } catch (Throwable ex) {
            // Due to the asynchronous call of @Async, the log is printed here to make it easier to follow.
            log.error("[record][url({}) log({}) Exception occurred]", reqDTO.getRequestUrl(), reqDTO, ex);
 }
 }

 private static void fillUserFields(OperateLogCreateReqDTO reqDTO) {
        // Use SecurityFrameworkUtils. Because you have to consider, rpc, MQ, job, it is not actually the web;
 LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
 if (loginUser == null) {
 return;
 }
 reqDTO.setUserId(loginUser.getId());
 reqDTO.setUserType(loginUser.getUserType());
 }

 public static void fillModuleFields(OperateLogCreateReqDTO reqDTO, LogRecord logRecord) {
        reqDTO.setType(logRecord.getType()); // Large module types, such as: CRM Customer
        reqDTO.setSubType(logRecord.getSubType());// Operation name, for example: transfer customer
        reqDTO.setBizId(Long.parseLong(logRecord.getBizNo())); // Business number, for example: customer number
        reqDTO.setAction(logRecord.getAction());// Operation content, for example: modify the user information numbered 1, change the gender from male to female, and change the name from Yudao to Yuandao.
        reqDTO.setExtra(logRecord.getExtra()); // Expand fields. Some complex businesses need to record some fields (JSON format), for example, record order number, { orderId: "1"}
 }

 private static void fillRequestFields(OperateLogCreateReqDTO reqDTO) {
        // Get the Request object
 HttpServletRequest request = ServletUtils.getRequest();
 if (request == null) {
 return;
 }
        // Completion request information
 reqDTO.setRequestMethod(request.getMethod());
 reqDTO.setRequestUrl(request.getRequestURI());
 reqDTO.setUserIp(ServletUtils.getClientIP(request));
 reqDTO.setUserAgent(ServletUtils.getUserAgent(request));
 }

 @Override
 public List<LogRecord> queryLog(String bizNo, String type) {
        throw new UnsupportedOperationException("Use OperateLogAPI to query operation logs");
 }

 @Override
 public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        throw new UnsupportedOperationException("Use OperateLogAPI to query operation logs");
 }

}