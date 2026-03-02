package com.anread.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 启动事务管理
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.anread.feign")
public class AnreadServerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnreadServerUserApplication.class, args);
    }

}
