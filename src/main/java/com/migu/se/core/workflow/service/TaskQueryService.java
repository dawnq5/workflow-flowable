package com.migu.se.core.workflow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.migu.se.core.workflow.mode.TaskQueryBo;
import com.migu.se.core.workflow.util.Page;

/**
 * TODO 黎明
 *
 */
@Service
public class TaskQueryService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 查询businessKey
     * @param processInstanceId 流程实例ID
     * @return
     */
    public String queryBusinessKey(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return processInstance == null ? null : processInstance.getBusinessKey();
    }

    /**
     * 查询指定流程待办任务(包括candiate和assigned)
     * @param processKey 流程key
     * @return
     */
    public Page<Task> queryTodoTaskByProcessKey(String processKey, Integer pageNo, Integer pageSize) {
        Page<Task> page = new Page<Task>(pageSize);
        page.setPageNo(pageNo);
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(processKey)
                .listPage(page.getFirst(), page.getPageSize());
        page.setResult(tasks);
        return page;
    }

    /**
     * 查询当前用户指定流程待办任务(包括candiate和assigned)
     * @param processKey 流程key
     * @param userId 用户ID
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Task> queryTodoTaskByUserId(String processKey, String userId, Integer pageNo, Integer pageSize) {
        Page<Task> page = new Page<Task>(pageSize);
        page.setPageNo(pageNo);
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(processKey)
                .taskCandidateOrAssigned(userId).listPage(page.getFirst(), page.getPageSize());
        page.setResult(tasks);
        return page;
    }

    /**
     * 查询当前用户的所有待办任务(包括candiate和assigned)
     * @param userId 用户ID
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Task> queryTodoTaskByUserId(String userId, Integer pageNo, Integer pageSize) {
        Page<Task> page = new Page<Task>(pageSize);
        page.setPageNo(pageNo);
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(userId).listPage(page.getFirst(),
                page.getPageSize());
        page.setResult(tasks);
        return page;
    }

    /**
     * 查询任务的候选人
     *
     * @param taskId 任务实例id
     *
     * @return
     */
    public List<IdentityLink> queryIdentityLinks(String taskId) {
        return taskService.getIdentityLinksForTask(taskId);
    }

    /**
     * 获取用户参与过的还未结束的流程执行实例
     *
     * @param userId 用户ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Execution> queryAcitvieExecutions(String userId, Integer pageNo, Integer pageSize) {
        Page<Task> tasks = this.queryTodoTaskByUserId(userId, pageNo, pageSize);
        List<Execution> executions = new ArrayList<Execution>();
        for (Task task : tasks.getResult()) {
            Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId())
                    .singleResult();
            executions.add(execution);
        }
        Page<Execution> page = new Page<Execution>(pageSize);
        page.setPageNo(pageNo);
        page.setResult(executions);
        return page;
    }

    /**
     * 获取用户参与过的还未结束的task
     *
     * @param userId 用户ID
     * @param processKey 流程key
     * @param pageNo 页码
     * @param pageSize 每页大小
     *
     * @return
     */
    public Page<Task> queryActiveTask(String processKey, String userId, Integer pageNo, Integer pageSize) {
        Page<Task> page = new Page<Task>(pageSize);
        page.setPageNo(pageNo);
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(processKey)
                .taskCandidateOrAssigned(userId).listPage(page.getFirst(), page.getPageSize());
        page.setResult(tasks);
        return page;
    }

    /**
     * 获取用户参与过的还未结束的taskQuery
     *
     * @param userId 用户ID
     * @param processKey 流程key
     * @return
     */
    public TaskQuery getActiveTaskQuery(String processKey, String userId) {
        return this.getActiveTaskQuery(userId).processInstanceBusinessKey(processKey);
    }

    /**
     * 获取用户参与过的还未结束的taskQuery
     *
     * @param userId 用户ID
     * @return
     */
    public TaskQuery getActiveTaskQuery(String userId) {
        return taskService.createTaskQuery().taskCandidateOrAssigned(userId);
    }

    /**
     * 根据流程执行实例查询该实例所有处于活动状态的task
     * @param execution 流程执行实例对象
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Map<String, Task> queryActiveTasks(Execution execution) {
        List<Task> listTask = taskService.createTaskQuery().active().executionId(execution.getId()).list();
        Map<String, Task> result = new HashMap<String, Task>();
        for (Task task : listTask) {
            result.put(task.getId(), task);
        }

        return result;
    }

    /**
     * 根据执行实例获取流程定义对象
     * @param execution 流程执行实例对象
     * @return
     */
    @Transactional(readOnly = true)
    public ProcessDefinition queryProcessDefinition(Execution execution) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(execution.getProcessInstanceId()).singleResult();
        if (processInstance == null) {
            return null;
        }
        return repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
    }

    /**
     * 根据流程实例ID获取流程定义对象
     * @param processDefinitionId 流程执行实例对象
     * @return
     */
    @Transactional(readOnly = true)
    public ProcessDefinition queryProcessDefinition(String processDefinitionId) {
        return repositoryService.getProcessDefinition(processDefinitionId);
    }

    /**
     * 根据流程实例ID查询流程之前经过的活动 (包含有关活动（进程中的节点）的单个执行的信息。)
     * @param processInstanceId 流程实例ID
     * @return
     */
    @Transactional(readOnly = true)
    public Page<HistoricActivityInstance> queryHistoricActivity(String processInstanceId, Integer pageNo,
            Integer pageSize) {
        Page<HistoricActivityInstance> page = new Page<HistoricActivityInstance>(pageSize);
        page.setPageNo(pageNo);

        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).listPage(page.getFirst(), pageSize);
        page.setResult(historicActivityInstances);
        return page;
    }

    /**
     * 根据流程实例ID查询流程之前经过的Task (包含有关当前和过去（已完成和已删除）任务实例的信息)
     * @param processInstanceId 流程实例ID
     * @return page
     */
    public Page<HistoricTaskInstance> queryHistoricTask(String processInstanceId, Integer pageNo, Integer pageSize) {
        Page<HistoricTaskInstance> page = new Page<HistoricTaskInstance>(pageSize);
        page.setPageNo(pageNo);
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).listPage(page.getFirst(), pageSize);
        page.setResult(list);
        return page;
    }

    /**
     * 根据流程实例ID查询流程之前经过的Task
     * @param processInstanceId 流程实例ID
     * @return list
     */
    public List<HistoricTaskInstance> queryHistoricTask(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        return list;
    }

    /**
     * 查询当前流程后续节点
     * @param processInstanceId 流程实例ID
     * @return
     */
    public List<?> traceProcess(String processInstanceId) throws Exception {
        //TODO
        return null;
    }

    /**
     * 获取最新的一个userTask，也就是任务活动纪录
     * @param processInstanceId 流程实例ID
     * @return
     */
    public HistoricActivityInstance getLastActivity(String processInstanceId) throws Exception {
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().desc().listPage(1, 1);
        if (CollectionUtils.isEmpty(historicActivityInstances)) {
            return null;
        }
        return historicActivityInstances.get(0);
    }

    /**
     * 获取当前流程任务
     * @param processInstanceId 流程实例ID
     * @return
     */
    public List<Task> getCurrentTask(String processInstanceId) throws Exception {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    /**
     * 根据taskId和key获取变量
     * @param taskId 任务id
     * @param key
     * @return object
     */
    public Object getTaskMap(String taskId, String key) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        }
        return task.getTaskLocalVariables();
    }

    /**
     * 根据不同的条件获得 HistoricTaskInstanceQuery 自定义变量条件在业务层通过processVariableValues相关接口设置
     * @param taskQueryBo 查询条件bo
     * @return TaskQuery
     */
    @Transactional(readOnly = true)
    public HistoricTaskInstanceQuery getHistoryTaskQuery(TaskQueryBo taskQueryBo) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery();
        if (taskQueryBo != null) {
            String taskId = taskQueryBo.getTaskId();
            if (!StringUtils.isBlank(taskId)) {
                taskQuery = taskQuery.taskId(taskId);
            }

            String taskName = taskQueryBo.getTaskName();
            if (!StringUtils.isBlank(taskName)) {
                taskQuery = taskQuery.taskName(taskName);
            }

            String assignee = taskQueryBo.getAssignee();
            if (!StringUtils.isBlank(assignee)) {
                taskQuery = taskQuery.taskAssignee(assignee);
            }

            String owner = taskQueryBo.getOwner();
            if (!StringUtils.isBlank(owner)) {
                taskQuery = taskQuery.taskOwner(owner);
            }

            String candidate = taskQueryBo.getCandidate();
            if (!StringUtils.isBlank(candidate)) {
                taskQuery = taskQuery.taskCandidateUser(candidate);
            }

            String candidataGroup = taskQueryBo.getCandidataGroup();
            if (!StringUtils.isBlank(candidataGroup)) {
                taskQuery = taskQuery.taskCandidateGroup(candidataGroup);
            }

            String processInstanceId = taskQueryBo.getProcessInstanceId();
            if (!StringUtils.isBlank(processInstanceId)) {
                taskQuery = taskQuery.processInstanceId(processInstanceId);
            }

            String processInstanceBusinessKey = taskQueryBo.getProcessInstanceBusinessKey();
            if (!StringUtils.isBlank(processInstanceBusinessKey)) {
                taskQuery = taskQuery.processInstanceBusinessKey(processInstanceBusinessKey);
            }

            String taskDefinitionKey = taskQueryBo.getTaskDefinitionKey();
            if (!StringUtils.isBlank(taskDefinitionKey)) {
                taskQuery = taskQuery.taskDefinitionKey(taskDefinitionKey);
            }

            String taskCategory = taskQueryBo.getTaskCategory();
            if (!StringUtils.isBlank(taskCategory)) {
                taskQuery = taskQuery.taskCategory(taskCategory);
            }

            String executionId = taskQueryBo.getExecutionId();
            if (!StringUtils.isBlank(taskDefinitionKey)) {
                taskQuery = taskQuery.executionId(executionId);
            }

            String processDefinitionKey = taskQueryBo.getProcessDefinitionKey();
            if (!StringUtils.isBlank(taskDefinitionKey)) {
                taskQuery = taskQuery.processDefinitionKey(processDefinitionKey);
            }

            Date createdOn = taskQueryBo.getCreatedOn();
            if (createdOn != null) {
                taskQuery = taskQuery.taskCreatedOn(createdOn);
            }

            Date createdBefore = taskQueryBo.getCreatedBefore();
            if (createdBefore != null) {
                taskQuery = taskQuery.taskCreatedBefore(createdBefore);
            }

            Date createdAfter = taskQueryBo.getCreatedAfter();
            if (createdAfter != null) {
                taskQuery = taskQuery.taskCreatedAfter(createdAfter);
            }

            Boolean finished = taskQueryBo.getProcessFinished();
            if (finished) {
                taskQuery = taskQuery.processFinished();
            }
        }

        return taskQuery;
    }

    /**
     * 根据不同的条件获得 TaskQuery 自定义变量条件在业务层通过processVariableValues相关接口设置
     * @param taskQueryBo 查询条件bo
     * @return TaskQuery
     */
    @Transactional(readOnly = true)
    public TaskQuery getTaskQuery(TaskQueryBo taskQueryBo) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (taskQueryBo != null) {
            String taskId = taskQueryBo.getTaskId();
            if (!StringUtils.isBlank(taskId)) {
                taskQuery = taskQuery.taskId(taskId);
            }

            String taskName = taskQueryBo.getTaskName();
            if (!StringUtils.isBlank(taskName)) {
                taskQuery = taskQuery.taskName(taskName);
            }

            String assignee = taskQueryBo.getAssignee();
            if (!StringUtils.isBlank(assignee)) {
                taskQuery = taskQuery.taskAssignee(assignee);
            }

            String owner = taskQueryBo.getOwner();
            if (!StringUtils.isBlank(owner)) {
                taskQuery = taskQuery.taskOwner(owner);
            }

            String candidate = taskQueryBo.getCandidate();
            if (!StringUtils.isBlank(candidate)) {
                taskQuery = taskQuery.taskCandidateUser(candidate);
            }

            String candidataGroup = taskQueryBo.getCandidataGroup();
            if (!StringUtils.isBlank(candidataGroup)) {
                taskQuery = taskQuery.taskCandidateGroup(candidataGroup);
            }

            String processInstanceId = taskQueryBo.getProcessInstanceId();
            if (!StringUtils.isBlank(processInstanceId)) {
                taskQuery = taskQuery.processInstanceId(processInstanceId);
            }

            String processInstanceBusinessKey = taskQueryBo.getProcessInstanceBusinessKey();
            if (!StringUtils.isBlank(processInstanceBusinessKey)) {
                taskQuery = taskQuery.processInstanceBusinessKey(processInstanceBusinessKey);
            }

            String taskDefinitionKey = taskQueryBo.getTaskDefinitionKey();
            if (!StringUtils.isBlank(taskDefinitionKey)) {
                taskQuery = taskQuery.taskDefinitionKey(taskDefinitionKey);
            }

            String taskCategory = taskQueryBo.getTaskCategory();
            if (!StringUtils.isBlank(taskCategory)) {
                taskQuery = taskQuery.taskCategory(taskCategory);
            }

            String executionId = taskQueryBo.getExecutionId();
            if (!StringUtils.isBlank(taskDefinitionKey)) {
                taskQuery = taskQuery.executionId(executionId);
            }

            String processDefinitionKey = taskQueryBo.getProcessDefinitionKey();
            if (!StringUtils.isBlank(taskDefinitionKey)) {
                taskQuery = taskQuery.processDefinitionKey(processDefinitionKey);
            }

            String candidateOrAssigned = taskQueryBo.getCandidateOrAssigned();
            if (!StringUtils.isBlank(candidateOrAssigned)) {
                taskQuery = taskQuery.taskCandidateOrAssigned(candidateOrAssigned);
            }

            Date createdOn = taskQueryBo.getCreatedOn();
            if (createdOn != null) {
                taskQuery = taskQuery.taskCreatedOn(createdOn);
            }

            Date createdBefore = taskQueryBo.getCreatedBefore();
            if (createdBefore != null) {
                taskQuery = taskQuery.taskCreatedBefore(createdBefore);
            }

            Date createdAfter = taskQueryBo.getCreatedAfter();
            if (createdAfter != null) {
                taskQuery = taskQuery.taskCreatedAfter(createdAfter);
            }

            if (taskQueryBo.getActive()) {
                taskQuery = taskQuery.active();
            }
        }
        return taskQuery;
    }

    /**
     * 获取流程定义key
     * @param processInstanceId 流程实例ID
     * @return
     */
    public String getProcessDefinitonKey(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (processInstance == null)
            return null;
        return processInstance.getProcessDefinitionKey();
    }

    /**
     * 获取流程定义key
     * @param processDefinitionId 流程实例ID
     * @return
     */
    public String getProcessKeyByDefiniitionId(String processDefinitionId) {
        ProcessDefinition processDefinition = this.queryProcessDefinition(processDefinitionId);
        if (processDefinition == null)
            return null;
        return processDefinition.getKey();
    }
}
