package com.oaspi.web.listener;

import com.alibaba.fastjson2.JSON;
import com.oaspi.common.utils.spring.SpringUtils;
import com.oaspi.system.domain.Actor;
import com.oaspi.system.domain.Leaveapply;
import com.oaspi.system.mapper.LeaveapplyMapper;
import com.oaspi.system.service.impl.SysUserServiceImpl;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DataImport implements JavaDelegate {

    @Autowired
    RestTemplate restTemplate;


    private static final Logger log = LoggerFactory.getLogger(DataImport.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 调用REST接口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Actor data = new Actor();
        data.setAge(10);
        data.setName("zhangsw");
        HttpEntity<Actor> entity = new HttpEntity<Actor>(data, headers);
        try {
            Actor a = restTemplate.postForObject("http://localhost:8081/actors", entity ,Actor.class);
            log.info("接口调用返回{}", JSON.toJSONString(a));
            execution.setVariable("result", 200);
        } catch (Exception e) {
            e.printStackTrace();
            execution.setVariable("result", 500);
        }

    }
}
