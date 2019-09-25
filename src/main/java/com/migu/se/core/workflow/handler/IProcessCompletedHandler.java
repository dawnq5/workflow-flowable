package com.migu.se.core.workflow.handler;


/**
 * 流程完成后，业务处理接口
 */
public interface IProcessCompletedHandler {

    /**
     * 流程撤销事件
     * @param processInstId 流程实例ID
     */
    void handler(String processInstId,Integer compeleteType);
}
