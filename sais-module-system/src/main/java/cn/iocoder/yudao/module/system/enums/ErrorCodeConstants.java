package cn.iocoder.yudao.module.system.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * System error code enum
 *
 * system module uses the 1-002-000-000 range
 */
public interface ErrorCodeConstants {

    // ========== AUTH 1-002-000-000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_002_000_000, "Login failed: incorrect username or password");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_002_000_001, "Login failed: account is disabled");
    ErrorCode AUTH_LOGIN_CAPTCHA_CODE_ERROR = new ErrorCode(1_002_000_004, "Captcha is invalid, reason: {}");
    ErrorCode AUTH_THIRD_LOGIN_NOT_BIND = new ErrorCode(1_002_000_005, "Account is not bound, binding required");
    ErrorCode AUTH_MOBILE_NOT_EXISTS = new ErrorCode(1_002_000_007, "Mobile number does not exist");
    ErrorCode AUTH_REGISTER_CAPTCHA_CODE_ERROR = new ErrorCode(1_002_000_008, "Captcha is invalid, reason: {}");

    // ========== Menu 1-002-001-000 ==========
    ErrorCode MENU_NAME_DUPLICATE = new ErrorCode(1_002_001_000, "A menu with this name already exists");
    ErrorCode MENU_PARENT_NOT_EXISTS = new ErrorCode(1_002_001_001, "Parent menu does not exist");
    ErrorCode MENU_PARENT_ERROR = new ErrorCode(1_002_001_002, "Cannot set itself as the parent menu");
    ErrorCode MENU_NOT_EXISTS = new ErrorCode(1_002_001_003, "Menu does not exist");
    ErrorCode MENU_EXISTS_CHILDREN = new ErrorCode(1_002_001_004, "Cannot delete: child menus exist");
    ErrorCode MENU_PARENT_NOT_DIR_OR_MENU = new ErrorCode(1_002_001_005, "Parent menu must be a directory or menu type");
    ErrorCode MENU_COMPONENT_NAME_DUPLICATE = new ErrorCode(1_002_001_006, "A menu with this component name already exists");

    // ========== Role 1-002-002-000 ==========
    ErrorCode ROLE_NOT_EXISTS = new ErrorCode(1_002_002_000, "Role does not exist");
    ErrorCode ROLE_NAME_DUPLICATE = new ErrorCode(1_002_002_001, "A role named [{}] already exists");
    ErrorCode ROLE_CODE_DUPLICATE = new ErrorCode(1_002_002_002, "A role with code [{}] already exists");
    ErrorCode ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE = new ErrorCode(1_002_002_003, "Cannot modify a built-in system role");
    ErrorCode ROLE_IS_DISABLE = new ErrorCode(1_002_002_004, "Role [{}] is disabled");
    ErrorCode ROLE_ADMIN_CODE_ERROR = new ErrorCode(1_002_002_005, "Role code [{}] is reserved and cannot be used");

    // ========== User 1-002-003-000 ==========
    ErrorCode USER_USERNAME_EXISTS = new ErrorCode(1_002_003_000, "Username already exists");
    ErrorCode USER_MOBILE_EXISTS = new ErrorCode(1_002_003_001, "Mobile number already exists");
    ErrorCode USER_EMAIL_EXISTS = new ErrorCode(1_002_003_002, "Email already exists");
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_002_003_003, "User does not exist");
    ErrorCode USER_IMPORT_LIST_IS_EMPTY = new ErrorCode(1_002_003_004, "Imported user list cannot be empty");
    ErrorCode USER_PASSWORD_FAILED = new ErrorCode(1_002_003_005, "Password verification failed");
    ErrorCode USER_IS_DISABLE = new ErrorCode(1_002_003_006, "User [{}] is disabled");
    ErrorCode USER_COUNT_MAX = new ErrorCode(1_002_003_008, "Cannot create user: tenant quota ({}) exceeded");
    ErrorCode USER_IMPORT_INIT_PASSWORD = new ErrorCode(1_002_003_009, "Initial password is required");
    ErrorCode USER_MOBILE_NOT_EXISTS = new ErrorCode(1_002_003_010, "This mobile number is not registered");
    ErrorCode USER_REGISTER_DISABLED = new ErrorCode(1_002_003_011, "Registration is disabled");

    // ========== Department 1-002-004-000 ==========
    ErrorCode DEPT_NAME_DUPLICATE = new ErrorCode(1_002_004_000, "A department with this name already exists");
    ErrorCode DEPT_PARENT_NOT_EXITS = new ErrorCode(1_002_004_001, "Parent department does not exist");
    ErrorCode DEPT_NOT_FOUND = new ErrorCode(1_002_004_002, "Department does not exist");
    ErrorCode DEPT_EXITS_CHILDREN = new ErrorCode(1_002_004_003, "Cannot delete: child departments exist");
    ErrorCode DEPT_PARENT_ERROR = new ErrorCode(1_002_004_004, "Cannot set itself as the parent department");
    ErrorCode DEPT_NOT_ENABLE = new ErrorCode(1_002_004_006, "Department ({}) is disabled and cannot be selected");
    ErrorCode DEPT_PARENT_IS_CHILD = new ErrorCode(1_002_004_007, "Cannot set its own child department as the parent");

    // ========== Post 1-002-005-000 ==========
    ErrorCode POST_NOT_FOUND = new ErrorCode(1_002_005_000, "Post does not exist");
    ErrorCode POST_NOT_ENABLE = new ErrorCode(1_002_005_001, "Post ({}) is disabled and cannot be selected");
    ErrorCode POST_NAME_DUPLICATE = new ErrorCode(1_002_005_002, "A post with this name already exists");
    ErrorCode POST_CODE_DUPLICATE = new ErrorCode(1_002_005_003, "A post with this code already exists");

    // ========== Dict Type 1-002-006-000 ==========
    ErrorCode DICT_TYPE_NOT_EXISTS = new ErrorCode(1_002_006_001, "Dict type does not exist");
    ErrorCode DICT_TYPE_NOT_ENABLE = new ErrorCode(1_002_006_002, "Dict type is disabled and cannot be selected");
    ErrorCode DICT_TYPE_NAME_DUPLICATE = new ErrorCode(1_002_006_003, "A dict type with this name already exists");
    ErrorCode DICT_TYPE_TYPE_DUPLICATE = new ErrorCode(1_002_006_004, "A dict type with this code already exists");
    ErrorCode DICT_TYPE_HAS_CHILDREN = new ErrorCode(1_002_006_005, "Cannot delete: dict data still references this dict type");

    // ========== Dict Data 1-002-007-000 ==========
    ErrorCode DICT_DATA_NOT_EXISTS = new ErrorCode(1_002_007_001, "Dict data does not exist");
    ErrorCode DICT_DATA_NOT_ENABLE = new ErrorCode(1_002_007_002, "Dict data ({}) is disabled and cannot be selected");
    ErrorCode DICT_DATA_VALUE_DUPLICATE = new ErrorCode(1_002_007_003, "Dict data with this value already exists");

    // ========== Notice 1-002-008-000 ==========
    ErrorCode NOTICE_NOT_FOUND = new ErrorCode(1_002_008_001, "Notice does not exist");

    // ========== SMS Channel 1-002-011-000 ==========
    ErrorCode SMS_CHANNEL_NOT_EXISTS = new ErrorCode(1_002_011_000, "SMS channel does not exist");
    ErrorCode SMS_CHANNEL_DISABLE = new ErrorCode(1_002_011_001, "SMS channel is disabled and cannot be selected");
    ErrorCode SMS_CHANNEL_HAS_CHILDREN = new ErrorCode(1_002_011_002, "Cannot delete: SMS templates still reference this channel");

    // ========== SMS Template 1-002-012-000 ==========
    ErrorCode SMS_TEMPLATE_NOT_EXISTS = new ErrorCode(1_002_012_000, "SMS template does not exist");
    ErrorCode SMS_TEMPLATE_CODE_DUPLICATE = new ErrorCode(1_002_012_001, "An SMS template with code [{}] already exists");
    ErrorCode SMS_TEMPLATE_API_ERROR = new ErrorCode(1_002_012_002, "SMS API template call failed, reason: {}");
    ErrorCode SMS_TEMPLATE_API_AUDIT_CHECKING = new ErrorCode(1_002_012_003, "SMS API template unavailable: pending audit");
    ErrorCode SMS_TEMPLATE_API_AUDIT_FAIL = new ErrorCode(1_002_012_004, "SMS API template unavailable: audit rejected, {}");
    ErrorCode SMS_TEMPLATE_API_NOT_FOUND = new ErrorCode(1_002_012_005, "SMS API template unavailable: template does not exist");

    // ========== SMS Send 1-002-013-000 ==========
    ErrorCode SMS_SEND_MOBILE_NOT_EXISTS = new ErrorCode(1_002_013_000, "Mobile number does not exist");
    ErrorCode SMS_SEND_MOBILE_TEMPLATE_PARAM_MISS = new ErrorCode(1_002_013_001, "Template parameter ({}) is missing");
    ErrorCode SMS_SEND_TEMPLATE_NOT_EXISTS = new ErrorCode(1_002_013_002, "SMS template does not exist");

    // ========== SMS Code 1-002-014-000 ==========
    ErrorCode SMS_CODE_NOT_FOUND = new ErrorCode(1_002_014_000, "Verification code does not exist");
    ErrorCode SMS_CODE_EXPIRED = new ErrorCode(1_002_014_001, "Verification code has expired");
    ErrorCode SMS_CODE_USED = new ErrorCode(1_002_014_002, "Verification code has already been used");
    ErrorCode SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY = new ErrorCode(1_002_014_004, "Daily SMS send quota exceeded");
    ErrorCode SMS_CODE_SEND_TOO_FAST = new ErrorCode(1_002_014_005, "SMS send rate too high");

    // ========== Tenant 1-002-015-000 ==========
    ErrorCode TENANT_NOT_EXISTS = new ErrorCode(1_002_015_000, "Tenant does not exist");
    ErrorCode TENANT_DISABLE = new ErrorCode(1_002_015_001, "Tenant [{}] is disabled");
    ErrorCode TENANT_EXPIRE = new ErrorCode(1_002_015_002, "Tenant [{}] has expired");
    ErrorCode TENANT_CAN_NOT_UPDATE_SYSTEM = new ErrorCode(1_002_015_003, "The system tenant cannot be modified or deleted");
    ErrorCode TENANT_NAME_DUPLICATE = new ErrorCode(1_002_015_004, "A tenant named [{}] already exists");
    ErrorCode TENANT_WEBSITE_DUPLICATE = new ErrorCode(1_002_015_005, "A tenant with domain [{}] already exists");

    // ========== Tenant Package 1-002-016-000 ==========
    ErrorCode TENANT_PACKAGE_NOT_EXISTS = new ErrorCode(1_002_016_000, "Tenant package does not exist");
    ErrorCode TENANT_PACKAGE_USED = new ErrorCode(1_002_016_001, "This package is in use by tenants; reassign their packages before deleting");
    ErrorCode TENANT_PACKAGE_DISABLE = new ErrorCode(1_002_016_002, "Tenant package [{}] is disabled");
    ErrorCode TENANT_PACKAGE_NAME_DUPLICATE = new ErrorCode(1_002_016_003, "A tenant package with this name already exists");

    // ========== Social User 1-002-018-000 ==========
    ErrorCode SOCIAL_USER_AUTH_FAILURE = new ErrorCode(1_002_018_000, "Social authorization failed, reason: {}");
    ErrorCode SOCIAL_USER_NOT_FOUND = new ErrorCode(1_002_018_001, "Social authorization failed: matching user not found");

    ErrorCode SOCIAL_CLIENT_WEIXIN_MINI_APP_PHONE_CODE_ERROR = new ErrorCode(1_002_018_200, "Failed to obtain mobile number");
    ErrorCode SOCIAL_CLIENT_WEIXIN_MINI_APP_QRCODE_ERROR = new ErrorCode(1_002_018_201, "Failed to obtain mini-program QR code");
    ErrorCode SOCIAL_CLIENT_WEIXIN_MINI_APP_SUBSCRIBE_TEMPLATE_ERROR = new ErrorCode(1_002_018_202, "Failed to obtain mini-program subscription template");
    ErrorCode SOCIAL_CLIENT_WEIXIN_MINI_APP_SUBSCRIBE_MESSAGE_ERROR = new ErrorCode(1_002_018_203, "Failed to send mini-program subscription message");
    ErrorCode SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_UPLOAD_SHIPPING_INFO_ERROR = new ErrorCode(1_002_018_204, "Failed to upload mini-program order shipping info");
    ErrorCode SOCIAL_CLIENT_WEIXIN_MINI_APP_ORDER_NOTIFY_CONFIRM_RECEIVE_ERROR = new ErrorCode(1_002_018_205, "Failed to upload mini-program order receipt info");
    ErrorCode SOCIAL_CLIENT_NOT_EXISTS = new ErrorCode(1_002_018_210, "Social client does not exist");
    ErrorCode SOCIAL_CLIENT_UNIQUE = new ErrorCode(1_002_018_211, "Social client configuration already exists");

    // ========== OAuth2 Client 1-002-020-000 =========
    ErrorCode OAUTH2_CLIENT_NOT_EXISTS = new ErrorCode(1_002_020_000, "OAuth2 client does not exist");
    ErrorCode OAUTH2_CLIENT_EXISTS = new ErrorCode(1_002_020_001, "OAuth2 client ID already exists");
    ErrorCode OAUTH2_CLIENT_DISABLE = new ErrorCode(1_002_020_002, "OAuth2 client is disabled");
    ErrorCode OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS = new ErrorCode(1_002_020_003, "Unsupported grant type");
    ErrorCode OAUTH2_CLIENT_SCOPE_OVER = new ErrorCode(1_002_020_004, "Requested scope exceeds allowed scope");
    ErrorCode OAUTH2_CLIENT_REDIRECT_URI_NOT_MATCH = new ErrorCode(1_002_020_005, "Invalid redirect_uri: {}");
    ErrorCode OAUTH2_CLIENT_CLIENT_SECRET_ERROR = new ErrorCode(1_002_020_006, "Invalid client_secret: {}");

    // ========== OAuth2 Grant 1-002-021-000 =========
    ErrorCode OAUTH2_GRANT_CLIENT_ID_MISMATCH = new ErrorCode(1_002_021_000, "client_id mismatch");
    ErrorCode OAUTH2_GRANT_REDIRECT_URI_MISMATCH = new ErrorCode(1_002_021_001, "redirect_uri mismatch");
    ErrorCode OAUTH2_GRANT_STATE_MISMATCH = new ErrorCode(1_002_021_002, "state mismatch");

    // ========== OAuth2 Code 1-002-022-000 =========
    ErrorCode OAUTH2_CODE_NOT_EXISTS = new ErrorCode(1_002_022_000, "code does not exist");
    ErrorCode OAUTH2_CODE_EXPIRE = new ErrorCode(1_002_022_001, "code has expired");

    // ========== Mail Account 1-002-023-000 ==========
    ErrorCode MAIL_ACCOUNT_NOT_EXISTS = new ErrorCode(1_002_023_000, "Mail account does not exist");
    ErrorCode MAIL_ACCOUNT_RELATE_TEMPLATE_EXISTS = new ErrorCode(1_002_023_001, "Cannot delete: mail templates still reference this account");

    // ========== Mail Template 1-002-024-000 ==========
    ErrorCode MAIL_TEMPLATE_NOT_EXISTS = new ErrorCode(1_002_024_000, "Mail template does not exist");
    ErrorCode MAIL_TEMPLATE_CODE_EXISTS = new ErrorCode(1_002_024_001, "Mail template code ({}) already exists");

    // ========== Mail Send 1-002-025-000 ==========
    ErrorCode MAIL_SEND_TEMPLATE_PARAM_MISS = new ErrorCode(1_002_025_000, "Template parameter ({}) is missing");
    ErrorCode MAIL_SEND_MAIL_NOT_EXISTS = new ErrorCode(1_002_025_001, "Mailbox does not exist");

    // ========== Notify Template 1-002-026-000 ==========
    ErrorCode NOTIFY_TEMPLATE_NOT_EXISTS = new ErrorCode(1_002_026_000, "Notify template does not exist");
    ErrorCode NOTIFY_TEMPLATE_CODE_DUPLICATE = new ErrorCode(1_002_026_001, "A notify template with code [{}] already exists");

    // ========== Notify Template 1-002-027-000 ==========

    // ========== Notify Send 1-002-028-000 ==========
    ErrorCode NOTIFY_SEND_TEMPLATE_PARAM_MISS = new ErrorCode(1_002_028_000, "Template parameter ({}) is missing");

}
