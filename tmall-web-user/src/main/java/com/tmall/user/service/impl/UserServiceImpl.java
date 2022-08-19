package com.tmall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.redis.key.CommonKey;
import com.tmall.user.entity.po.UserPO;
import com.tmall.user.mapper.UserMapper;
import com.tmall.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;


import static com.tmall.common.constants.GlobalConfig.USER_DEFAULT_AVATAR;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
                .andCondition("is_delete=0");
        try {
            if (userMapper.updateByExample(userPo, example) == 1 && redisClient.setNoLog(CommonKey.TOKEN, LoginInfo.getToken(), user)) {
                return PublicResult.success();
            }
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(user) + "に変えるのはエラーが発生した", e);
        }
        return PublicResult.error();
    }

}
