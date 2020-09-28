package com.wans.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * sentinel配置类
 * ApplicationRunner接口(或CommandLineRunner)，springboot启动时执行
 * Created by admin on 2020/9/28.
 */
@Component
@Slf4j
public class SentinelApplicationRunner implements ApplicationRunner {
    private static final String GETORDER_KEY = "orderToMember";  // 限流规则名称
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(GETORDER_KEY);                // 设置 资源名
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);    // QPS限流模式(QPS和thread)
        rule1.setCount(1);                              // QPS数量控制在2以内
        rule1.setLimitApp("default");
        rules.add(rule1);                               // 添加 限流规则
        FlowRuleManager.loadRules(rules);
        log.info("...."+GETORDER_KEY+"限流配置初始化成功....");
    }
}
