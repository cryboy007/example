package com.github.cryboy007.activiti.service;

import com.github.cryboy007.activiti.model.ActivitiVo;

import java.util.List;
import java.util.Map;

/**
 * @InterfaceName ActivitiService
 * @Author HETAO
 * @Date 2022/7/1 15:01
 */
public interface ActivitiService {

    void doProcess(ActivitiVo activitiVo);

    List<Map> queryUserTaskByUserName(ActivitiVo activitiVo);

    void createDeployment();

}
