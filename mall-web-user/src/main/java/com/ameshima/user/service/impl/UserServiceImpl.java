package com.ameshima.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.remote.store.api.IStoreService;
import com.ameshima.user.entity.dto.StoreUserDTO;
import com.ameshima.user.entity.po.StoreUserPO;
import com.ameshima.user.mapper.StoreUserMapper;
import com.ameshima.user.service.UserService;
import com.ameshima.common.constants.GlobalConfig;
import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.LoginUser;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.common.redis.key.CommonKey;
import com.ameshima.user.entity.po.UserPO;
import com.ameshima.user.mapper.UserMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;


import static com.ameshima.common.constants.GlobalConfig.USER_DEFAULT_AVATAR;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private GlobalConfig globalConfig;
    @Resource
    private RedisClient redisClient;
    @Resource
    private StoreUserMapper storeUserMapper;
    @Resource
    private IStoreService storeService;


    @Override
    public int createUser(LoginUser user) {
        UserPO userPo = new UserPO();
        userPo.setAccountId(user.getAccountId());
        userPo.setNickName(user.getNickName().trim());
        userPo.setAvatar(user.getAvatar());
        if (StringUtils.isBlank(user.getAvatar())) {
            userPo.setAvatar(globalConfig.get(USER_DEFAULT_AVATAR));
        }
        userPo.setGender(user.getGender());
        userMapper.insertSelective(userPo);
        return userPo.getId();
    }

    @Override
    public PublicResult<?> update(LoginUser user) {
        // Assert.isTrue(account == null || account.getAccountId() < 1 || StringUtils.isBlank(account.getNickName()));
        UserPO userPo = new UserPO();
        userPo.setAccountId(user.getAccountId());
        userPo.setNickName(user.getNickName().trim());
        userPo.setAvatar(user.getAvatar());
        if (StringUtils.isBlank(user.getAvatar())) {
            userPo.setAvatar(globalConfig.get(USER_DEFAULT_AVATAR));
        }
        userPo.setGender(user.getGender());
        Example example = new Example(UserPO.class);
        example.and().andEqualTo("accountId", user.getAccountId())
                .andEqualTo("isDelete", MallConstant.NO);
        try {
            if (userMapper.updateByExampleSelective(userPo, example) == 1 && redisClient.setNoLog(CommonKey.TOKEN, LoginInfo.getToken(), user)) {
                // ログイン情報を更新
                LoginUser loginUser = LoginInfo.get();
                loginUser.setAvatar(user.getAvatar());
                loginUser.setNickName(user.getNickName());
                loginUser.setGender(user.getGender());
                redisClient.set(CommonKey.TOKEN, LoginInfo.getToken(), loginUser);
                return PublicResult.success();
            }
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(user) + "に変えるのはエラーが発生した", e);
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<?> registerStore(StoreUserDTO storeUser) {
        LoginUser loginUser = LoginInfo.get();
        try {
            PublicResult<Integer> registerResult = storeService.register(storeUser.getStoreName());
            if (registerResult.getErrCode() != PublicResult.OK_CODE) {
                return registerResult;
            }
            StoreUserPO storeUserPO = new StoreUserPO(loginUser.getAccountId(), registerResult.getData(),
                    storeUser.getStoreName(), storeUser.getBusinessLicense());
            storeUserMapper.insertSelective(storeUserPO);
            // ログイン情報を更新
            loginUser.setStoreId(registerResult.getData());
            loginUser.setStoreName(storeUser.getStoreName());
            redisClient.set(CommonKey.TOKEN, LoginInfo.getToken(), loginUser);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error(String.format("店舗登録エラー=>%s", new Gson().toJson(storeUser)), e);
        }
        return PublicResult.error();
    }

}
