package com.migu.se.core.workflow.handler;


/**
 * 定时任务触发后，业务处理接口
 */
public interface ITaskTimerHandler {

    /**
     * 任务创建过程中的操作（包含事务）
     * @param taskId 任务ID
     * @param processInstId 流程实例ID
     */
    void handler(String taskId, String processInstId);
}
