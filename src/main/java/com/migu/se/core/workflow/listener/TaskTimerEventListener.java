package com.migu.se.core.workflow.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.event.impl.FlowableActivityCancelledEventImpl;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migu.se.core.workflow.handler.ITaskTimerHandler;


/**
 * 流程中定时任务监听事件（处理给组任务分配权限）
 * 必须要处理事务，异常后支持回滚
 * 提供handler接口，处理完成后的业务处理
 */
@Component
public class TaskTimerEventListener implements FlowableEventListener {

    /**
     * 日志处理器
     */
    public final static Logger logger = LoggerFactory.getLogger(TaskTimerEventListener.class);

    @Autowired(required=false)
    @Qualifier("taskTimerHandler")
    private ITaskTimerHandler taskTimerHandler;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    public void onEvent(FlowableEvent event) {
        String eventType = event.getType().name();
        if (FlowableEngineEventType.ACTIVITY_CANCELLED.name().equals(eventType)) {
            TaskService taskService = processEngineConfiguration.getTaskService();
            FlowableActivityCancelledEventImpl eventImpl = (FlowableActivityCancelledEventImpl) event;

            Task task = taskService.createTaskQuery()
                    .processInstanceId(eventImpl
                    .getProcessInstanceId())
                    .executionId(eventImpl
                    .getExecutionId()).singleResult();
            if (null != this.taskTimerHandler) {
                try {
                    this.taskTimerHandler.handler(task.getId(),eventImpl.getProcessInstanceId());
                } catch (Exception e) {
                    logger.error("taskCompleteHandler fail ========>" + e.getMessage());
                    throw new RuntimeException(e);
                }

            }

        }
    }

    public boolean isFailOnException() {
        return true;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getOnTransaction() {
        // TODO Auto-generated method stub
        return null;
    }
}
