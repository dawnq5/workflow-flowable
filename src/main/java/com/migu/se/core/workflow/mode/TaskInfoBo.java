package com.migu.se.core.workflow.mode;

import java.util.Map;

import org.flowable.task.api.Task;


/**
 * 任务对象。
 * @author haow
 *
 */
public class TaskInfoBo {
	
	// 封装工作流中的任务id
	private String id;
	
	private String key;
	
	// 封装工作流中的任务变量
	private Map<String, Object> mapVar;
	
	public TaskInfoBo(Task task){
		this.id = task.getId();
		this.key = task.getTaskDefinitionKey();
		this.mapVar = task.getProcessVariables();
	}

	public TaskInfoBo(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getMapVar() {
		return mapVar;
	}

	public void setMapVar(Map<String, Object> mapVar) {
		this.mapVar = mapVar;
	}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
	
}
