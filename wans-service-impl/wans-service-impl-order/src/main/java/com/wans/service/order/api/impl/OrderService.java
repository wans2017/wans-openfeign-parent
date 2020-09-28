package com.wans.service.order.api.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.wans.service.order.api.openfeign.MemberServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务接口
 * Created by wans on 2020/7/8.
 */
@RestController
public class OrderService {
    @Autowired  // 注入会员的openfeign客户端
    private MemberServiceFeign memberServiceFeign;
    /**
     * 基于feign客户端实现rpc远程调用
     * @return
     */
    @GetMapping("orderFeignToMember")
    public String orderFeignToMember() {
        String res = memberServiceFeign.getUser(1);
        return "订单服务通过OpenFeign调用会员服务接口，返回"+res;
    }

    @GetMapping("/testOne")
    public String testOne() {
        return "test成功";
    }

    private static final String GETORDER_KEY = "orderToMember";  // 限流规则名称
    /*
    创建限流规则
     */
    @GetMapping("/initFlowQpsRule")
    public String initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(GETORDER_KEY);                // 设置 资源名
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);    // QPS限流模式(QPS和thread)
        rule1.setCount(1);                              // QPS数量控制在2以内
        rule1.setLimitApp("default");
        rules.add(rule1);                               // 添加 限流规则
        FlowRuleManager.loadRules(rules);
        return "...."+GETORDER_KEY+"限流配置初始化成功....";
    }
    /*
    引用限流规则
     */
    @GetMapping("/orderToMember")
    public String orderToMember() {
        Entry entry = null;
        try {
            entry = SphU.entry(GETORDER_KEY);
            // 执行我们服务需要保护的业务逻辑
            return "orderToMember接口";
        } catch (Exception e) {
            e.printStackTrace();
            return "orderToMember接口已经达到上限,稍后尝试.";
        } finally {
            // SphU.entry(xxx) 需要与 entry.exit() 成对出现,否则会导致调用链记录异常
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
