package com.migu.se.core.workflow.handler;

import org.flowable.task.api.Task;

/**
 * 任务创建过程中的操作接口
 * @author haow
 *
 */
public interface ITaskCreatedHandler {
	
	/**
	 * 任务创建过程中的操作（包含事务）
	 * @param task task对象
	 * @param groupId 用户组
	 */
	void handler(Task task, String groupId);
}
