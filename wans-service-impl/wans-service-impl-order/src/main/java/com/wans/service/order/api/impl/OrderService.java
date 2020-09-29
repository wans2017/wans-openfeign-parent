package com.wans.service.order.api.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.wans.service.order.api.openfeign.MemberServiceFeign;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    /*
    @SentinelResource 注解方式定义 限流规则
    value 限流规则名称；和启动配置类SentinelApplicationRunner的名称相同
    blockHandler 限流/熔断出现异常所执行方法；
    fallback 服务降级执行方法名；
    限流，熔断，接口超时，接口出现异常 导致服务降级方法执行；但是为了明确是那种异常，最好分开定义；
    所以有了参数 blockHandler 限流/熔断出现异常所执行方法
     */
    @SentinelResource(value = GETORDER_KEY,
            blockHandler = "getOrderQpsException")
    // fallback = "orderToMemberAnnotation"
    @GetMapping("/orderToMemberAnnotation")
    public String orderToMemberAnnotation() {
        return "orderToMemberAnnotation接口方法";
    }
    /*
    BlockException 只在blockHandler对应方法里才有
     */
    public String getOrderQpsException(BlockException e) {
        e.printStackTrace();
        return "orderToMemberAnnotation接口已经被降级啦!";
    }

    /*
    sentinel控制台对此接口进行限流
    注：没有使用@SentinelResource设置流量规则名称
    默认 流量规则名称为接口路径地址，这里为：/getOrderCon
     */
    @SentinelResource(value = "getOrderCon",
            blockHandler = "getOrderQpsException")
    @GetMapping("/getOrderCon")
    public String getOrderConsole() {
        return "getOrderConsole接口服务";
    }

    @SentinelResource(value = "getOrderSig",
            blockHandler = "getOrderQpsException")
    @GetMapping("/getOrderSig")
    public String getOrderSignal() {
        log.info("====="+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (Exception e) { }
        return "getOrderSignal接口服务";
    }

}
