package com.ameshima.user.web;

import java.util.List;

import javax.annotation.Resource;

import com.ameshima.user.entity.dto.AddressDTO;
import com.ameshima.user.service.AddressService;
import com.ameshima.common.annotation.LoginRequire;
import org.springframework.web.bind.annotation.*;

import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.PublicResult;

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
