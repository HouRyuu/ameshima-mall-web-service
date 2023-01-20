package com.tmall.user.web;

import java.util.List;

import javax.annotation.Resource;

import com.tmall.common.annotation.LoginRequire;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.PublicResult;
import com.tmall.user.entity.dto.AddressDTO;
import com.tmall.user.service.AddressService;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RestController
@RequestMapping("/address")
public class AddressSource {

    @Resource
    private AddressService addressService;

    @LoginRequire
    @PutMapping("/save")
    public PublicResult<?>  save(@RequestBody AddressDTO address) {
        return addressService.save(address, LoginInfo.get().getAccountId());
    }

    @LoginRequire
    @DeleteMapping("/{id}")
    public PublicResult<?>  remove(@PathVariable int id) {
        return addressService.remove(id, LoginInfo.get().getAccountId());
    }

    @LoginRequire
    @GetMapping("/list")
    public PublicResult<List<AddressDTO>> findList() {
        return PublicResult.success(addressService.findList(LoginInfo.get().getAccountId()));
    }

    @LoginRequire
    @PutMapping("/{id}/default")
    public PublicResult<?>  setDefault(@PathVariable int id) {
        return addressService.setDefault(id, LoginInfo.get().getAccountId());
    }


}
