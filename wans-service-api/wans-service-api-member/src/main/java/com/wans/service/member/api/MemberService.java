package com.wans.service.member.api;

import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员接口
 * Created by wans on 2020/7/6.
 */
public interface MemberService {

    /**
     * 提供用户查询接口
     * @param userId
     * @return
     */
    @GetMapping("/getUser")
    String getUser(Integer userId);

    @GetMapping("/getGateWayPort")
    String getGateWayPort(HttpServletRequest request);
}
