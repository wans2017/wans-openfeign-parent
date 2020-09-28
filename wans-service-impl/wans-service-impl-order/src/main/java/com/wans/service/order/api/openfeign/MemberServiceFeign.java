package com.wans.service.order.api.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 会员接口 OpenFeign客户端
 * Created by wans on 2020/7/8.
 */
@FeignClient("wans-member")    // 标注为openfeign客户端,名称为服务名称
public interface MemberServiceFeign {

    /**
     * 提供用户查询接口
     * 注意：openfeign必须加 @RequestMapping/@RequestParam/@PathVariable/@RequestHeader
     * @param userId
     * @return  123
     */
    @GetMapping("/getUser")
    String getUser(@RequestParam(value = "userId", required = false) Integer userId);
}
