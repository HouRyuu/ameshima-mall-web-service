package com.tmall.goods.web;

import java.util.Map;
import java.util.Set;

import com.tmall.common.dto.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.PublicResult;
import com.tmall.goods.entity.dto.ShoppingCartDTO;
import com.tmall.goods.service.ShoppingCartService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartResource {

    @Autowired
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
