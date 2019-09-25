package com.migu.se.core.workflow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.migu.se.core.workflow.mode.TaskInfoBo;


@Service
public class TaskHandlerService {
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    /**
     * 开启一个新的流程
     * @param processKey 流程key
     * @param applyUserName 提交人
     * @param paramMap 流程自定义变量(k-v键值对)
     * @throws Exception 流程不存在异常
     */
    public ProcessInstance startProcess(String processKey, String applyUserName,  Map<String, Object> paramMap) {
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (null == processDefinition) {
            throw new RuntimeException("流程" + processKey + "不存在");
        }
        
        paramMap = paramMap == null ? new HashMap<String, Object>() : paramMap;
//        paramMap.put(PROCESS_DEFINITION_ID, processDefinition.getId());
//        paramMap.put(ACTIVITI_APPLY_USERNAME, applyUserName);
        return this.runtimeService.startProcessInstanceByKey(processKey, paramMap);
    }
    
    /**
     * 任务接管, 把组任务变成个人任务， 
     * 注：任务被接管后组任务成员就不能再查询到任务
     * @param processId 流程id, 启动流程时初始化"re_proc_def"的值
     * @param userName 用户名
     * @return 任务信息
     */
    public TaskInfoBo claimTask(String processId, String taskId, String userName){
        Task task = this.taskService.createTaskQuery()
                .processInstanceId(processId)
                .taskId(taskId)
                .taskCandidateUser(userName).singleResult();
        if (null == task) {
            return null;
        }
        this.taskService.claim(taskId, userName);
        TaskInfoBo TaskInfoBo = new TaskInfoBo(task);
        return TaskInfoBo;
    }

    /**
     * 任务接管, 把组任务变成个人任务， 
     * 注：任务被接管后组任务成员就不能再查询到任务
     * @param processId 流程id, 启动流程时初始化"re_proc_def"的值
     * @param userName 用户名
     * @return 任务信息
     */
    public List<TaskInfoBo> claimBatchTask(String processId, String userName){
        List<TaskInfoBo> TaskInfoBoList = new ArrayList<TaskInfoBo>();
        List<Task> taskList = this.taskService.createTaskQuery()
                .processInstanceId(processId)
                .taskCandidateUser(userName).list();
        if (null == taskList || taskList.size() == 0) {
            return null;
        }
        for(Task task : taskList){
            this.taskService.claim(task.getId(), userName);
            TaskInfoBo TaskInfoBo = new TaskInfoBo(task);
            TaskInfoBoList.add(TaskInfoBo);
        }
        return TaskInfoBoList;
    }
    
    /**
     * 组任务归还，归还后组任务成员可以查询到组任务
     * @param processId 流程id, 启动流程时初始化"re_proc_def"的值
     * @param userName 用户名
     * @param taskId 任务id
     * @return 返回任务信息, 不为null 表示归还成功.
     */
    public TaskInfoBo unclaimTask(String processId, String userName, String taskId){
        Task task = this.taskService.createTaskQuery()
                .processDefinitionId(processId)
                .taskAssignee(userName)
                .taskId(taskId)
                .singleResult();
        if (null == task) {
            return null;
        }
        
        this.taskService.unclaim(taskId);
        return new TaskInfoBo(task);
    }
    
    /**
     * 审批任务
     * 场景：向下的分支只有唯一一个
     * @throws Exception 任务不存在异常
     */
    public void completeTask(String processId, String userName, String taskId, Map<String, Object> map) throws Exception{
        Task task = this.taskService.createTaskQuery()
                .processInstanceId(processId)
                .taskAssignee(userName)
                .taskId(taskId)
                .singleResult();
        if (null == task) {
            throw new RuntimeException("任务, taskId=" + taskId + ", 不存在");
        }
        
        if (null == map ||map.isEmpty()) {
            this.taskService.complete(task.getId());
        } else {
            this.taskService.complete(task.getId(), map);
        }
    }

    /**
     * 审批任务
     * 
     * 场景一：
     * 对于任务完成后，向下存在多个分支情况：
     * 审批通过后，往下一个节点走，通过变量形式设值
     * 统一变量名称为：outcome
     * 例如：
     * outcome为1时，流向A任务
     * outcome为2时，流向B任务
     * @param processId 流程id
     * @param userName 用户名 
     * @param taskId 任务id
     * @param outCome 流向分支标识符
     * @param auditDescribeMap 审批的描述信息.
     * @throws Exception 
     */
    public void completeTask(String processId, String userName, String taskId, String outCome, Map<String, Object> auditDescribeMap) throws Exception{
        Task task = taskService.createTaskQuery()
                .processInstanceId(processId)
                .taskAssignee(userName)
                .taskId(taskId)
                .singleResult();
        if (null == task) {
            throw new RuntimeException("任务, taskId=" + taskId + ", 不存在");
        }
        
        // 通过流程变量的指定来决定走那条路线,一定要放到map中，在调用complete把map放到流程变量之中
        Map<String, Object> map = new HashMap<String,Object>();
        if (null != auditDescribeMap) {
            map.putAll(auditDescribeMap);
        }
        
        map.put("outcome", outCome);
        this.taskService.complete(task.getId(), map);
    }

    /**
     * 添加全局变量
     */
    public void addTaskVariable(String taskId, String variableName, Object value) {
        taskService.setVariable(taskId,variableName,value);
    }
}
