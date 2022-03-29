package com.tmall.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.tmall.remote.order.*"})
@ComponentScan("com.tmall")
@MapperScan("com.tmall.*.mapper")
public class TmallWebGoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmallWebGoodsApplication.class, args);
	}

}
