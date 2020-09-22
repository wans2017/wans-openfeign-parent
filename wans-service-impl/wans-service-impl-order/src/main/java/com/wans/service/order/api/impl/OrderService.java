package com.wans.service.order.api.impl;

import com.wans.service.order.api.openfeign.MemberServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
