package com.tmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan({"com.tmall"})
@EnableDiscoveryClient
@EnableFeignClients("com.tmall.remote.*")
@MapperScan("com.tmall.*.mapper")
public class TmallWebUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmallWebUserApplication.class, args);
    }

}
