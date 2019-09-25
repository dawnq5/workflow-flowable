package com.migu.se.core.workflow.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessCancelledEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migu.se.core.workflow.constant.ProcessStatusEnum;
import com.migu.se.core.workflow.handler.IProcessCompletedHandler;

/**
 * 流程完成事件监听类 必须要处理事务，
 * 异常后支持回滚 提供handler接口，
 * 处理完成后的业务处理 
 */
@Component
public class ProcessCompletedEventListener implements FlowableEventListener {

    public final static Logger logger = LoggerFactory.getLogger(ProcessCompletedEventListener.class);

    @Autowired(required = false)
    @Qualifier("processCompletedHandler")
    private IProcessCompletedHandler processCompletedHandler;

    @Override
    public void onEvent(FlowableEvent event) {
        String eventType = event.getType().name();
        // 根据事件的类型ID,找到对应的事件处理器
        if (FlowableEngineEventType.PROCESS_COMPLETED.name().equals(eventType)) {
            logger.debug("envent type is ========>" + eventType);
            FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl) event;
            ExecutionEntity executionEntity = (ExecutionEntity) eventImpl.getEntity();

            if (null != this.processCompletedHandler) {
                try {
                    this.processCompletedHandler.handler(executionEntity.getProcessInstanceId(),
                            ProcessStatusEnum.DONE.getKey());
                } catch (Exception e) {
                    logger.error("processCompletedHandler fail ========>" + e.getMessage());
                    throw new RuntimeException(e);
                }

            }
        } else if (FlowableEngineEventType.PROCESS_CANCELLED.name().equals(eventType)) {

            FlowableProcessCancelledEventImpl eventImpl = (FlowableProcessCancelledEventImpl) event;
            String processInstanceId = eventImpl.getProcessInstanceId();

            if (null != this.processCompletedHandler) {
                try {
                    this.processCompletedHandler.handler(processInstanceId, ProcessStatusEnum.CANCEL.getKey());
                } catch (Exception e) {
                    logger.error("processCompletedHandler fail ========>" + e.getMessage());
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
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}