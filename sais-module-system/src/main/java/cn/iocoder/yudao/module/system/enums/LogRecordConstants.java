package cn.iocoder.yudao.module.system.enums;

/**
 * System operation log enum
 * Centralises operation-log strings so Service classes don't accumulate them ad-hoc.
 */
public interface LogRecordConstants {

    // ======================= SYSTEM_USER =======================

    String SYSTEM_USER_TYPE = "SYSTEM User";
    String SYSTEM_USER_CREATE_SUB_TYPE = "Create user";
    String SYSTEM_USER_CREATE_SUCCESS = "Created user [{{#user.nickname}}]";
    String SYSTEM_USER_UPDATE_SUB_TYPE = "Update user";
    String SYSTEM_USER_UPDATE_SUCCESS = "Updated user [{{#user.nickname}}]: {_DIFF{#updateReqVO}}";
    String SYSTEM_USER_DELETE_SUB_TYPE = "Delete user";
    String SYSTEM_USER_DELETE_SUCCESS = "Deleted user [{{#user.nickname}}]";
    String SYSTEM_USER_UPDATE_PASSWORD_SUB_TYPE = "Reset user password";
    String SYSTEM_USER_UPDATE_PASSWORD_SUCCESS = "Reset password for user [{{#user.nickname}}] from [{{#user.password}}] to [{{#newPassword}}]";

    // ======================= SYSTEM_ROLE =======================

    String SYSTEM_ROLE_TYPE = "SYSTEM Role";
    String SYSTEM_ROLE_CREATE_SUB_TYPE = "Create role";
    String SYSTEM_ROLE_CREATE_SUCCESS = "Created role [{{#role.name}}]";
    String SYSTEM_ROLE_UPDATE_SUB_TYPE = "Update role";
    String SYSTEM_ROLE_UPDATE_SUCCESS = "Updated role [{{#role.name}}]: {_DIFF{#updateReqVO}}";
    String SYSTEM_ROLE_DELETE_SUB_TYPE = "Delete role";
    String SYSTEM_ROLE_DELETE_SUCCESS = "Deleted role [{{#role.name}}]";

}
