package com.migu.se.core.workflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.migu.se.core.workflow.po.FlowPo;
import com.migu.se.core.workflow.po.ProcessTaskPo;

@Service
public class ProcessTaskService {
    
    /**
     * 根据流程id和任务key获取任务流出列表
     * @param processId 流程id
     * @param taskKey 任务key
     * @return 流向列表
     */
    public List<FlowPo> getTaskOutGoingList(String processId, String taskKey) {
        return null;
    }
    
    /**
     * 根据流程id和任务key获取任务流入列表
     * @param processId 流程id
     * @param taskKey 任务key
     * @return 流向列表
     */
    public List<FlowPo> getTaskInComingList(String processId, String taskKey) {
        return null;
    }

    /**
     * 根据processKey和taskKey获取ProcessTask对象
     * @param processKey
     * @param taskKey
     * @return
     */
    public List<ProcessTaskPo> getProcessTaskList(String processKey, String taskKey) {
        return null;
    }
}
