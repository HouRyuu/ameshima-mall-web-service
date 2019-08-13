package com.tmall.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan({ "com.tmall" })
@EnableEurekaClient
@MapperScan("com.tmall.*.mapper")
public class TmallWebStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmallWebStoreApplication.class, args);
	}

}
