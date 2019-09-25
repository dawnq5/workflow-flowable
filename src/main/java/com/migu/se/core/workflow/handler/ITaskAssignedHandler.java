package com.migu.se.core.workflow.handler;

import org.flowable.task.api.Task;

/**
 * 流程的任务签收后，业务处理接口
 */
public interface ITaskAssignedHandler {

    /**
     * 签收事件
     * 
     * @param task task对象
     */
    void handler(Task task);
}
