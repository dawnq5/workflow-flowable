package com.migu.se.core.workflow.listener;


import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.migu.se.core.workflow.handler.ITaskCreatedHandler;

/**
 * 流程中任务创建事件监听类（处理给组任务分配权限）
 * 必须要处理事务，异常后支持回滚
 * 提供handler接口，处理完成后的业务处理
 * @author haow
 *
 */
@Component
public class TaskCreatedEventListener implements FlowableEventListener {
	/**
	 * 日志处理器
	 */
	public final static Logger logger = LoggerFactory.getLogger(TaskCreatedEventListener.class);

	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;

//	@Autowired
//	private ApproveAuthService approveAuthService;

	@Autowired(required=false)
	@Qualifier("taskCreatedHandler")
	private ITaskCreatedHandler taskCreatedHandler;

	public void onEvent(FlowableEvent event) {
		RepositoryService repositoryService = this.processEngineConfiguration.getRepositoryService();
		String eventType = event.getType().name();
		if (FlowableEngineEventType.TASK_CREATED.name().equals(eventType)) {
		    FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl) event;
			TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();

			// 根据流程id, 流程key, 任务id名称 共同获取任务用户成员
			String processDefinitionId = taskEntity.getProcessDefinitionId();
			String taskDefinitionKey = taskEntity.getTaskDefinitionKey();

			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId).singleResult();
			String processKey = null;
			if (null != processDefinition) {
				processKey = processDefinition.getKey();
			}

			if (StringUtils.isBlank(processKey)) {
				throw new RuntimeException("无法将任务分配给组成员，processKey为空， processDefinitionId=" + processDefinitionId
						+ ",taskDefinitionKey:" + taskDefinitionKey);
			}

//			MemberShip memberShip = new MemberShip();
//			memberShip.setProcessKey(processKey);
//			memberShip.setTaskKey(taskDefinitionKey);
//			String groupId = this.approveAuthService.getProcessTaskGroupId(memberShip);
//			if (groupId == null ) {
//				throw new RuntimeException("无法将任务分配给组，因为组数量为0,  processDefinitionId=" + processDefinitionId
//						+ ",taskDefinitionKey:" + taskDefinitionKey);
//			}

			String groupId = "";
			if (null != this.taskCreatedHandler) {
				try{
					this.taskCreatedHandler.handler(taskEntity, groupId);
				}catch (Exception e){
					logger.error("taskCreatedHandler fail ========>" + e.getMessage());
					throw new RuntimeException(e);
				}

			}
			taskEntity.addCandidateGroup(groupId);
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
