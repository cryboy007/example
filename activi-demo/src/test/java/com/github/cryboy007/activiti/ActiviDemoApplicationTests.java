package com.github.cryboy007.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

@Slf4j
public class ActiviDemoApplicationTests {
//    @Autowired
//    private  ActivitiService activitiService;
//
//    @Autowired
//    private RepositoryService repositoryService;
//
//    @Test
//    public void contextLoads() {
//        activitiService.doProcess(ActivitiVo.builder().key("001").build());
//    }

    @Test
    public void createDeployment() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        log.info(defaultProcessEngine.getName());
    }

}
