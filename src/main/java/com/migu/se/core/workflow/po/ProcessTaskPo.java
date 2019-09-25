package com.migu.se.core.workflow.po;

import java.util.List;

/**
 * 流程中的任务对象.
 * @author haow
 *
 */
public class ProcessTaskPo {
	
	private long id;
	private String processDefinitionId;
	private String taskKey;
	private String taskName;
	private String processKey;
	private String taskType;
	private String createTime;
	private String processName;
	private Integer processVersion;
	
	private List<FlowPo> inCome;
	private List<FlowPo> outCome;
	public ProcessTaskPo() {
	}
	
	public ProcessTaskPo(String processDefinitionId,
			String processDefinitionKey,
			String taskKey,
			String taskName,
			String taskType,
			String	processName,
			Integer processVersion) {
		this.processDefinitionId = processDefinitionId;
		this.processKey = processDefinitionKey;
		this.taskKey = taskKey;
		this.taskName = taskName;
		this.taskType = taskType;
		this.processName = processName;
		this.processVersion = processVersion;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getProcessVersion() {
		return processVersion;
	}

	public void setProcessVersion(Integer processVersion) {
		this.processVersion = processVersion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<FlowPo> getInCome() {
		return inCome;
	}

	public void setInCome(List<FlowPo> inCome) {
		this.inCome = inCome;
	}

	public List<FlowPo> getOutCome() {
		return outCome;
	}

	public void setOutCome(List<FlowPo> outCome) {
		this.outCome = outCome;
	}
	
}
