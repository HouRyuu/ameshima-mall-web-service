package com.ameshima.goods.web;

import java.util.Map;
import java.util.Set;

import com.ameshima.goods.entity.dto.ShoppingCartDTO;
import com.ameshima.goods.service.ShoppingCartService;
import com.ameshima.common.dto.LoginInfo;
import org.springframework.web.bind.annotation.*;

import com.ameshima.common.annotation.LoginRequire;
import com.ameshima.common.dto.PublicResult;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartResource {

    @Resource
    private ShoppingCartService shoppingCartService;

    @LoginRequire
    @PostMapping("/add")
    public PublicResult<?>  add(@RequestBody ShoppingCartDTO param) {
        param.setAccountId(LoginInfo.get().getAccountId());
        return shoppingCartService.add(param);
    }

    @LoginRequire
    @GetMapping("/getCartCount")
    public PublicResult<?>  getCartCount() {
        return PublicResult.success(shoppingCartService.getCartCount(LoginInfo.get().getAccountId()));
    }

    @LoginRequire
    @GetMapping("/findGoods")
    public PublicResult<?>  findGoods() {
        return PublicResult.success(shoppingCartService.findGoods(LoginInfo.get().getAccountId()));
    }

    @LoginRequire
    @PostMapping("/remove")
    public PublicResult<?>  remove(@RequestBody Map<String, Set<Integer>> idMap) {
        return shoppingCartService.remove(idMap.get("ids"), LoginInfo.get().getAccountId());
    }

    @LoginRequire
    @PutMapping("/{cartId}/amount/{amount}/update")
    public PublicResult<?>  updateAmount(@PathVariable int cartId, @PathVariable int amount) {
        return shoppingCartService.updateAmount(cartId, amount, LoginInfo.get().getAccountId());
    }

    @LoginRequire
    @DeleteMapping("/fail/remove")
    public PublicResult<?>  removeFailCart() {
        return shoppingCartService.removeFailCart(LoginInfo.get().getAccountId());
    }

}
