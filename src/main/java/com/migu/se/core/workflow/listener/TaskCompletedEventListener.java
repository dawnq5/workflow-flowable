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

import com.migu.se.core.workflow.handler.ITaskCompletedHandler;

/**
 * 流程中任务完成事件监听类
 * 必须要处理事务，异常后支持回滚
 * 提供handler接口，处理完成后的业务处理
 * @author haow
 *
 */
@Component
public class TaskCompletedEventListener implements FlowableEventListener {
	
	/**
	 * 日志处理器
	 */
	public final static Logger logger = LoggerFactory.getLogger(TaskCompletedEventListener.class);

	@Autowired(required=false)
	@Qualifier("taskCompleteHandler")
	private ITaskCompletedHandler taskCompleteHandler;

	public void onEvent(FlowableEvent event) {
		String eventType = event.getType().name();
		// 根据事件的类型ID,找到对应的事件处理器
		if (FlowableEngineEventType.TASK_COMPLETED.name().equals(eventType)) {
			logger.debug("envent type is ========>" + eventType);
			FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl) event;
			TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();

			if (null != this.taskCompleteHandler) {
				try {
					this.taskCompleteHandler.handler(taskEntity);
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
