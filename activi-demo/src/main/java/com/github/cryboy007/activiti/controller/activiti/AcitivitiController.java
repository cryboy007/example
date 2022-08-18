package com.github.cryboy007.activiti.controller.activiti;

import com.github.cryboy007.activiti.service.ActivitiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AcitiviController
 * @Author tao.he
 * @Since 2022/7/1 14:59
 */
@RestController
@RequestMapping("activiti")
@RequiredArgsConstructor
public class AcitivitiController {

    private final ActivitiService activitiService;

    @GetMapping("createDeployment")
    public void createDeployment() {
        activitiService.createDeployment();
    }
}
