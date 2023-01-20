package com.tmall.user.mapper;

import com.tmall.user.entity.po.AccountPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAlipayMapperTest {

    @Resource
    private UserAlipayMapper userAlipayMapper;
    @Resource
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
