package com.tmall.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tmall.common.dto.LoginUser;
import com.tmall.user.entity.po.UserPO;
import com.tmall.user.mapper.UserMapper;
import com.tmall.user.service.UserService;

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

    // private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Override
    public int createUser(LoginUser user) {
        UserPO userPo = new UserPO();
        userPo.setAccountId(user.getAccountId());
        userPo.setNickName(user.getNickName().trim());
        userPo.setAvatar(user.getAvatar());
        userPo.setGender(user.getGender());
        userMapper.insertSelective(userPo);
        return userPo.getId();
    }

}
