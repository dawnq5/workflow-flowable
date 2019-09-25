package com.migu.se.core.workflow.service;

import org.springframework.stereotype.Service;

@Service
public class ProcessHandlerService {

    /**
     * 部署一个新的流程
     * @param name 流程名称
     * @param category 流程类别
     * @param bpmnPath 流程图classpath下的路径
     * @param bpmnPng 流程图片classpath下的路径
     * @throws Exception 部署异常
     */
    public void deployProcess(String name, String category, String bpmnPath, String bpmnPng) throws Exception {
        
    }
    
    
    /**
     * 根据流程id获取流程的key
     * @param processDefinitionId 流程id
     * @return 流程key
     */
    public String getProcessDefinitionKey(String processDefinitionId) {
        
        return null;
    }
    

    /**
     * 删除流程
     * @param deployId 部署id
     * @param cascade 是否级联删除
     */
    public void deleteProcess(String deployId, boolean cascade) {
    }
    
    /**
     * 删除流程实例
     * @param processInstanceId 流程实例ID
     * @param deleteReason 流程实例ID
     *
     */
    public void deleteProcessInstance(String processInstanceId,String deleteReason) {
        
    }
    
    
    /**
     * 校验流程定义id和key是否匹配
     * @param processDefinitionId
     * @param processDefinitionKey
     * @return
     * @return boolean
     */
    public boolean validateProcessDefAndKey(String processDefinitionId, String processDefinitionKey){
        return false;
    }
}
