package com.wans.service.member.api.impl;

import com.wans.service.member.api.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员接口实现
 * Created by wans on 2020/7/6.
 */
@RestController
@RequestMapping("/wans")
//@RestController("/wans")
public class MemberServiceImpl implements MemberService {
    @Value("${server.port}")
    private String serverPort;
    @Override
    // 这里完全无需添加，通过实现接口已然继承 @GetMapping("/getUser")
    public String getUser(Integer userId) {
        return "我是wans，提供会员服务,端口号："+serverPort;
    }
}
