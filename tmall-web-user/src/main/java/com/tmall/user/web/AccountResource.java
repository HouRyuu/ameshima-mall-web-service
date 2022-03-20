package com.tmall.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.redis.key.CommonKey;
import com.tmall.remote.goods.api.IGoodsService;
import com.tmall.remote.goods.dto.GoodsDTO;
import com.tmall.user.entity.dto.RegisterDTO;
import com.tmall.user.entity.po.AccountPO;
import com.tmall.user.service.AccountService;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
public class AccountResource {

    @Resource
    private IGoodsService goodsService;
    @Resource
    private RedisClient redisClient;
    @Resource
    private AccountService accountService;

    @GetMapping("/hello")
    public String hello() {
        return "hello user";
    }

    @GetMapping("/getGoods")
    public GoodsDTO getGoods() {
        return goodsService.getGoods(1);
    }

    @LoginRequire
    @GetMapping("/loginInfo")
    public PublicResult<?>  loginInfo() {
        return PublicResult.success(LoginInfo.get());
    }

    @PostMapping("/login")
    public PublicResult<?>  login(@RequestBody AccountPO account) {
        return accountService.login(account);
    }

    @LoginRequire
    @GetMapping("/logout")
    public PublicResult<?>  logout() {
        redisClient.removeKey(CommonKey.TOKEN, LoginInfo.getToken());
        return PublicResult.success();
    }

    @GetMapping("/sendRegisterCaptcha")
    public PublicResult<?>  sendRegisterCaptcha(String account) {
        return accountService.sendRegisterCaptcha(account);
    }

    @PostMapping("/register")
    public PublicResult<?>  register(@RequestBody RegisterDTO registerInfo) {
        return accountService.register(registerInfo);
    }

    @GetMapping("/sendForgetCaptcha")
    public PublicResult<?>  sendForgetCaptcha(String account) {
        return accountService.sendForgetCaptcha(account);
    }

    @PostMapping("/forgetPwd")
    public PublicResult<?>  forgetPwd(@RequestBody RegisterDTO registerInfo) {
        return accountService.forgetPwd(registerInfo);
    }

}
