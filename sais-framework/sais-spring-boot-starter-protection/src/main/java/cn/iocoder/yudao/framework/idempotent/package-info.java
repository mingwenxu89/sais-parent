/**
 * Idempotent components, refer to https://github.com/it4alla/idempotent project implementation
 * The implementation principle is that methods with the same parameters can be executed only once within a period of time. In this way, idempotence is guaranteed.
 *
 * Usage scenario: For example, the user quickly double-clicks a button, but the front-end does not disable the button, resulting in two repeated requests being sent.
 *
 * The differences with the it4alla/idempotent component are mainly reflected in two points:
 * 1. We removed the delKey attribute of the @Idempotent annotation. The reason is that essentially when delKey is true, the ability to achieve distributed locking is achieved
 * At this time, we prefer to use the Lock4j component. In principle, a component provides only a single capability.
 * 2. Considering the versatility of the component, we do not use the Redisson RMap structure like the it4alla/idempotent component, but directly use the String data format of Redis.
 */
package cn.iocoder.yudao.framework.idempotent;
