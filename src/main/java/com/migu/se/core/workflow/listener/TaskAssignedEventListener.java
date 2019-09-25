package com.migu.se.core.workflow.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migu.se.core.workflow.handler.ITaskAssignedHandler;

/**
 * 流程中任务接管事件监听类 必须要处理事务，
 * 异常后支持回滚 提供handler接口，
 * 处理完成后的业务处理
 * 
 */
@Component
public class TaskAssignedEventListener implements FlowableEventListener {

    public final static Logger logger = LoggerFactory.getLogger(TaskAssignedEventListener.class);

    @Autowired(required = false)
    @Qualifier("taskAssignedHandler")
    private ITaskAssignedHandler taskAssignedHandler;

    public void onEvent(FlowableEvent event) {
        String eventType = event.getType().name();
        // 根据事件的类型ID,找到对应的事件处理器
        if (FlowableEngineEventType.TASK_ASSIGNED.name().equals(eventType)) {
            logger.debug("envent type is ========>" + eventType);
            FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl) event;
            TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();

            if (null != this.taskAssignedHandler) {
                try {
                    this.taskAssignedHandler.handler(taskEntity);
                } catch (Exception e) {
                    logger.error("taskAssignedHandler fail ========>" + e.getMessage());
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
