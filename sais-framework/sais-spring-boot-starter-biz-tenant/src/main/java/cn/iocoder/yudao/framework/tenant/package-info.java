/**
 * Multi-tenancy supports the following levels:
 * 1. DB: Based on MyBatis Plus multi-tenant function implementation.
 * 2. Redis: Isolate by splicing tenant IDs on the Redis Key.
 * 3. Web: When requesting the HTTP API, parse the tenant-ID tenant ID of the header and add it to the tenant context.
 * 4. Security: Verify whether the currently logged-in user has unauthorized access to other tenants' data.
 * 5. Job: When the JobHandler executes a task, it will be executed independently and in parallel for each tenant.
 * 6. MQ: When the Producer sends a message, the Header carries the tenant-ID tenant ID; when the Consumer consumes the message, the Header's tenant-ID tenant ID is added to the tenant context.
 * 7. Async: Asynchronous needs to ensure the transitivity of ThreadLocal, which is achieved by using Alibaba’s open source TransmittableThreadLocal. Relevant modification points can be seen:
 * 1)Spring Async: 
 * {@link cn.iocoder.yudao.framework.quartz.config.YudaoAsyncAutoConfiguration#threadPoolTaskExecutorBeanPostProcessor()}
 * 2)Spring Security: 
 * TransmittableThreadLocalSecurityContextHolderStrategy
 * and YudaoSecurityAutoConfiguration#securityContextHolderMethodInvokingFactoryBean() method
 *
 */
package cn.iocoder.yudao.framework.tenant;
