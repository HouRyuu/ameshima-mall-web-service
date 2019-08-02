package com.tmall.user.mapper;

import com.tmall.user.entity.po.AccountPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAlipayMapperTest {

    @Autowired
    private UserAlipayMapper userAlipayMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void saveOrUpdate() {
        userAlipayMapper.saveOrUpdate(null);
    }

    @Test
    public void saveAccount() {
        AccountPO account = new AccountPO();
        accountMapper.insertSelective(account);
        System.out.println(account.getId());
    }
}
