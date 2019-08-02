package com.tmall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.tmall" })
@EnableEurekaClient
@EnableFeignClients("com.tmall.remote.*")
@MapperScan("com.tmall.user.mapper")
public class TmallWebUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmallWebUserApplication.class, args);
    }

}
