package com.migu.se.core.workflow.po;

/**
 * 流程流向对象
 * @author haow
 *
 */
public class FlowPo {
	public static final String FLOW_IN = "in";
	public static final String FLOW_OUT = "out";
	
	private String id;
	private String flowId;
	private String flowName;
	private String sourceRef;
	private String targetRef;
	private String conditionExpression;
	private String skipExpression;
	private String processName;
	private Integer processVersion;
	// in表示来源，out表示去向
	private String type;
	
	private String processId;
	private String processKey;
	private String taskKey;
	private String taskType;
	private String createTime;
	
	public FlowPo() {
	}
	
	public FlowPo(String flowId, String name, String sourceRef, String targetRef,
			String conditionExpression, String skipExpression, String type,
			String processDefinitionId, String processDefinitionKey, String taskKey, String taskType,
	        String processName,Integer processVersion) {
		this.flowId = flowId;
		this.flowName = name;
		this.sourceRef = sourceRef;
		this.targetRef = targetRef;
		this.conditionExpression = conditionExpression;
		this.skipExpression = skipExpression;
		this.type = type;
		this.processId = processDefinitionId;
		this.processKey = processDefinitionKey;
		this.taskKey = taskKey;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(String targetRef) {
		this.targetRef = targetRef;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public String getSkipExpression() {
		return skipExpression;
	}

	public void setSkipExpression(String skipExpression) {
		this.skipExpression = skipExpression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
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
}
