package com.tmall.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.constants.LoginErrResultEnum;
import com.tmall.common.dto.AjaxResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.utils.CommonUtil;
import com.tmall.remote.goods.api.IGoodsService;
import com.tmall.remote.goods.dto.GoodsDTO;
import com.tmall.user.entity.dto.LoginInfo;
import com.tmall.user.entity.dto.LoginUser;
import com.tmall.user.entity.po.AccountPO;
import com.tmall.user.keys.UserKey;
import com.tmall.user.service.AccountService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
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
    public AjaxResult loginInfo() {
        return AjaxResult.success(LoginInfo.get());
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody AccountPO account) {
        LoginUser loginUser = accountService.login(account);
        if (loginUser == null) {
            return AjaxResult.error(LoginErrResultEnum.LOGIN_FAIL);
        }
        String token = CommonUtil.getUuid();
        redisClient.set(UserKey.TOKEN, token, loginUser);
        return AjaxResult.success(token);
    }

    @LoginRequire
    @GetMapping("/logout")
    public AjaxResult logout() {
        redisClient.removeKey(UserKey.TOKEN, LoginInfo.getToken());
        return AjaxResult.success();
    }

}
