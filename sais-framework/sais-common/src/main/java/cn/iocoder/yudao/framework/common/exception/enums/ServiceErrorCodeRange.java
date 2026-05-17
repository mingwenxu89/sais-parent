package cn.iocoder.yudao.framework.common.exception.enums;

/**
 * Error code range of business exceptions, solution: Solve the error code definitions of each module to avoID duplication. It is only stated here without actual use.
 *
 * There are 10 people in total, divided into four sections.
 *
 * First paragraph, 1 bit, type
 * 1 - Business level exception
 * x - reserved
 * Second section, 3 digits, system type
 * 001 - User system
 * 002 - Commodity System
 * 003 - Order System
 * 004 - Payment System
 * 005 - Coupon System
 *... -...
 * The third section, 3 digits, module
 * No restrictive rules.
 * It is generally recommended that each system may have multiple modules and can be segmented. Take the user system as an example:
 * 001 - OAuth2 module
 * 002 - User module
 * 003 - MobileCode module
 * The fourth segment, 3 digits, error code
 * No restrictive rules.
 * It is generally recommended that each module be incremented by itself.
 *
 * @author Yudao Source Code
 */
public class ServiceErrorCodeRange {

    // Module infra error code range [1-001-000-000 ~ 1-002-000-000)
    // Module system error code range [1-002-000-000 ~ 1-003-000-000)
    // Module report error code range [1-003-000-000 ~ 1-004-000-000)
    // Module member error code range [1-004-000-000 ~ 1-005-000-000)
    // Module mp error code range [1-006-000-000 ~ 1-007-000-000)
    // Module pay error code range [1-007-000-000 ~ 1-008-000-000)
    // Module bpm error code range [1-009-000-000 ~ 1-010-000-000)

    // Module product error code range [1-008-000-000 ~ 1-009-000-000)
    // Module trade error code range [1-011-000-000 ~ 1-012-000-000)
    // Module promotion error code range [1-013-000-000 ~ 1-014-000-000)

    // Module crm error code range [1-020-000-000 ~ 1-021-000-000)

    // Module ai error code range [1-022-000-000 ~ 1-023-000-000)

}
