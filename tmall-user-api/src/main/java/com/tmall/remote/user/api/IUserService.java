package com.tmall.remote.user.api;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@FeignClient(value = "tmall-user", path = "/user")
public interface IUserService {


}
