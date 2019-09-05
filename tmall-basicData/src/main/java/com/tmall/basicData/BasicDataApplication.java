package com.tmall.basicData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SpringBootApplication
@ComponentScan({ "com.tmall" })
@EnableEurekaClient
@MapperScan("com.tmall.*.mapper")
public class BasicDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicDataApplication.class, args);
    }

}
