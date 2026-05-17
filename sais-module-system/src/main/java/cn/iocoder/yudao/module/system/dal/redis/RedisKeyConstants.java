package cn.iocoder.yudao.module.system.dal.redis;

import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

/**
 * System Redis Key enumeration class
 *
 * @author Yudao Source Code
 */
public interface RedisKeyConstants {

    /**
     * Cache of all sub-department ID arrays of the specified department
     * <p>
     * KEY format: dept_children_ids:{id}
     * VALUE data type: String sub-department ID collection
     */
    String DEPT_CHILDREN_ID_LIST = "dept_children_ids";

    /**
     * role cache
     * <p>
     * KEY format: role:{id}
     * VALUE data type: String role information
     */
    String ROLE = "role";

    /**
     * Cache of role IDs owned by the user
     * <p>
     * KEY format: user_role_ids:{userId}
     * VALUE data type: String role ID collection
     */
    String USER_ROLE_ID_LIST = "user_role_ids";

    /**
     * Cache that holds the character ID for the specified menu
     * <p>
     * KEY format: menu_role_ids:{menuId}
     * VALUE data type: String role ID collection
     */
    String MENU_ROLE_ID_LIST = "menu_role_ids";

    /**
     * Has a cache of menu ID arrays corresponding to permissions
     * <p>
     * KEY format: permission_menu_ids:{permission}
     * VALUE data type: String menu ID array
     */
    String PERMISSION_MENU_ID_LIST = "permission_menu_ids";

    /**
     * OAuth2 client cache
     * <p>
     * KEY format: oauth_client:{id}
     * VALUE data type: String client information
     */
    String OAUTH_CLIENT = "oauth_client";

    /**
     * Caching of access tokens
     * <p>
     * KEY format: oauth2_access_token:{token}
     * VALUE data type: String access token information {@link OAuth2AccessTokenDO}
     * <p>
     * Due to dynamic expiration time, use RedisTemplate operation
     */
    String OAUTH2_ACCESS_TOKEN = "oauth2_access_token:%s";

    /**
     * Caching of on-site letter templates
     * <p>
     * KEY format: notify_template:{code}
     * VALUE data format: String template information
     */
    String NOTIFY_TEMPLATE = "notify_template";

    /**
     * Email account cache
     * <p>
     * KEY format: mail_account:{id}
     * VALUE data format: String account information
     */
    String MAIL_ACCOUNT = "mail_account";

    /**
     * Caching of email templates
     * <p>
     * KEY Format:mail_template:{code}
     * VALUE data format: String template information
     */
    String MAIL_TEMPLATE = "mail_template";

    /**
     * Caching of SMS templates
     * <p>
     * KEY format: sms_template:{id}
     * VALUE data format: String template information
     */
    String SMS_TEMPLATE = "sms_template";

    /**
     * Mini program subscription template cache
     *
     * KEY format: wxa_subscribe_template:{userType}
     * VALUE data format String, template information
     */
    String WXA_SUBSCRIBE_TEMPLATE = "wxa_subscribe_template";

}
