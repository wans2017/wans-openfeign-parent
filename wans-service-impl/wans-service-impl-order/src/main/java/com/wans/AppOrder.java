package com.wans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务 启动类
 * Created by wans on 2020/7/8.
 */
@SpringBootApplication
@EnableFeignClients // 开启OpenFeign
public class AppOrder {
    public static void main(String[] args) {
        SpringApplication.run(AppOrder.class);
    }
}
