package com.migu.se.core.workflow.handler;

import org.flowable.task.api.Task;

/**
 * 任务完成后，业务处理接口
 */
public interface ITaskCompletedHandler {

    /**
     * 任务完成事件处理
     * @param task task对象
     */
    void handler(Task task);
}
