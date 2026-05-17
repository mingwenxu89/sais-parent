package cn.iocoder.yudao.framework.common.exception.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Global error code enumeration
 * 0-999 system exception coding reserved
 *
 * Generally, use the HTTP response status code https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 * Although the HTTP response status code is weak in expressiveness for business use, it is still very good at the system level.
 * What's special is that because 0 has been used as success before, 200 is no longer used.
 *
 * @author Yudao Source Code
 */
public interface GlobalErrorCodeConstants {

    ErrorCode SUCCESS = new ErrorCode(0, "success");

    // ========== Client Error Section ==========

    ErrorCode BAD_REQUEST = new ErrorCode(400, "The request parameters are incorrect");
    ErrorCode UNAUTHORIZED = new ErrorCode(401, "Account not logged in");
    ErrorCode FORBIDDEN = new ErrorCode(403, "No permission for this operation");
    ErrorCode NOT_FOUND = new ErrorCode(404, "Request not found");
    ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "The request method is incorrect");
    ErrorCode LOCKED = new ErrorCode(423, "Request failed, please try again later"); // Concurrent requests, not allowed
    ErrorCode TOO_MANY_REQUESTS = new ErrorCode(429, "The request is too frequent, please try again later.");

    // ========== Server error section ==========

    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "System exception");
    ErrorCode NOT_IMPLEMENTED = new ErrorCode(501, "Function not implemented/not enabled");
    ErrorCode ERROR_CONFIGURATION = new ErrorCode(502, "Wrong configuration item");

    // ========== Custom error section ==========
    ErrorCode REPEATED_REQUESTS = new ErrorCode(900, "Repeat request, please try again later"); // Repeat request
    ErrorCode DEMO_DENY = new ErrorCode(901, "Demo mode, write operations are forbidden");

    ErrorCode UNKNOWN = new ErrorCode(999, "unknown error");

}
