/**
 * 1. Scheduled tasks, using Quartz to implement task execution within the process.
 * Considering high availability, Quartz’s own MySQL cluster solution is used.
 *
 * 2. Asynchronous tasks are executed asynchronously using Spring Async.
 */
package cn.iocoder.yudao.framework.quartz;
