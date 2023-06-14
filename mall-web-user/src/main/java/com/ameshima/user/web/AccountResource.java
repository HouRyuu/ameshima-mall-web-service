package com.ameshima.user.web;

import com.ameshima.remote.goods.api.IGoodsService;
import com.ameshima.remote.goods.dto.GoodsDTO;
import com.ameshima.user.entity.dto.RegisterDTO;
import com.ameshima.user.entity.po.AccountPO;
import com.ameshima.user.service.AccountService;
import com.ameshima.user.service.UserService;
import com.ameshima.common.annotation.LoginRequire;
import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.LoginUser;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.common.redis.key.CommonKey;
import com.ameshima.common.utils.FileUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RestController
public class AccountResource {

    @Resource
    private IGoodsService goodsService;
    @Resource
    private RedisClient redisClient;
    @Resource
    private AccountService accountService;
    @Resource
    private UserService userService;

    @GetMapping("/getGoods")
    public GoodsDTO getGoods() {
        return goodsService.getGoods(1);
    }

    @LoginRequire
    @GetMapping("/loginInfo")
    public PublicResult<?> loginInfo() {
        return PublicResult.success(LoginInfo.get());
    }

    @PostMapping("/login")
    public PublicResult<?> login(@RequestBody AccountPO account) {
        return accountService.login(account);
    }

    @LoginRequire
    @GetMapping("/logout")
    public PublicResult<?> logout() {
        redisClient.removeKey(CommonKey.TOKEN, LoginInfo.getToken());
        return PublicResult.success();
    }

    @GetMapping("/sendRegisterCaptcha")
    public PublicResult<?> sendRegisterCaptcha(String account) {
        return accountService.sendRegisterCaptcha(account);
    }

    @PostMapping("/register")
    public PublicResult<?> register(@RequestBody RegisterDTO registerInfo) {
        return accountService.register(registerInfo);
    }

    @GetMapping("/sendForgetCaptcha")
    public PublicResult<?> sendForgetCaptcha(String account) {
        return accountService.sendForgetCaptcha(account);
    }

    @PostMapping("/forgetPwd")
    public PublicResult<?> forgetPwd(@RequestBody RegisterDTO registerInfo) {
        return accountService.forgetPwd(registerInfo);
    }

    @LoginRequire
    @PostMapping("/avatar/upload")
    public PublicResult<String> avatarUpload(@RequestParam("avatarFile") MultipartFile avatarFile) throws IOException {
        String avatar = FileUtil.compressImg(avatarFile.getInputStream(), avatarFile.getContentType());
        LoginUser loginUser = LoginInfo.get();
        loginUser.setAvatar(avatar);
        userService.update(loginUser);
        return PublicResult.success(avatar);
    }

    @LoginRequire
    @PutMapping("/update")
    public PublicResult<?> updateUserInfo(@RequestBody LoginUser user) {
        LoginUser loginUser = LoginInfo.get();
        loginUser.setNickName(user.getNickName());
        loginUser.setGender(user.getGender());
        userService.update(loginUser);
        return PublicResult.success();
    }

}
