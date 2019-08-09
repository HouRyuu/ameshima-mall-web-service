package com.tmall.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.tmall.goods.mapper")
public class TmallWebGoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmallWebGoodsApplication.class, args);
	}

}
