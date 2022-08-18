package com.github.cryboy007.activiti.service.impl;

import com.github.cryboy007.activiti.model.ActivitiVo;
import com.github.cryboy007.activiti.service.ActivitiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ActivitiServiceImpl
 * @Author tao.he
 * @Since 2022/7/1 15:02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivitiServiceImpl implements ActivitiService {

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;


    @Override
    public void doProcess(ActivitiVo activitiVo) {
        final DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        final List<Deployment> deployments = deploymentQuery.deploymentKey(activitiVo.getKey()).list();

        final ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        final List<ProcessDefinition> list = processDefinitionQuery.deploymentId(deployments.get(0).getId()).list();

        //变量
        Map<String,Object> variables = new HashMap<>();

        runtimeService.startProcessInstanceById(list.get(0).getId(),activitiVo.getKey(),variables);

    }

    @Override
    public List<Map> queryUserTaskByUserName(ActivitiVo activitiVo) {
        final TaskQuery taskQuery = taskService.createTaskQuery();
        final List<Task> list = taskQuery.taskAssignee(activitiVo.getUsername()).list();
        return null;
    }

    @Override
    public void createDeployment() {
        repositoryService.createDeployment()
                .key("test")
                .name("测试流程")
                .addClasspathResource("bpmn/test2.bpmn")
                .deploy();
        log.info("流程创建成功");
    }
}
