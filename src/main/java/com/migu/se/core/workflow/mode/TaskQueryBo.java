package com.migu.se.core.workflow.mode;

import java.util.Date;

/**
 * 任务查询对象
 */
public class TaskQueryBo {

    private String taskId;//任务id
    private String taskName;//任务名
    private String assignee;//任务签收人
    private String owner;//任务拥有者
    private String candidate;//候选人
    private String candidataGroup;//候选人组
    private String processInstanceId;//流程实例id
    private String processInstanceBusinessKey;//业务key 开始流程的时候可以传入
    private String processDefinitionKey;//流程定义key bpmn图的名称
    private String taskDefinitionKey;//任务key
    private String taskCategory;//任务类别
    private String executionId;//实例执行id
    private String candidateOrAssigned;//候选人或者签收人
    private Date   createdBefore; //任务级别时间
    private Date   createdAfter;
    private Date   createdOn;
    private Boolean active = true; //是否是激活状态
    private Boolean processFinished = true;//流程是否完成,查询历史使用

    public Boolean getProcessFinished() {
        return processFinished;
    }

    public void setProcessFinished(Boolean processFinished) {
        this.processFinished = processFinished;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getCandidataGroup() {
        return candidataGroup;
    }

    public void setCandidataGroup(String candidataGroup) {
        this.candidataGroup = candidataGroup;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceBusinessKey() {
        return processInstanceBusinessKey;
    }

    public void setProcessInstanceBusinessKey(String processInstanceBusinessKey) {
        this.processInstanceBusinessKey = processInstanceBusinessKey;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getCandidateOrAssigned() {
        return candidateOrAssigned;
    }

    public void setCandidateOrAssigned(String candidateOrAssigned) {
        this.candidateOrAssigned = candidateOrAssigned;
    }

    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
