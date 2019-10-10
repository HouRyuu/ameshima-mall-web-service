package com.tmall.goods.web;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.AjaxResult;
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
    public AjaxResult add(@RequestBody ShoppingCartDTO param) {
        return shoppingCartService.add(param);
    }

    @LoginRequire
    @GetMapping("/getCartCount")
    public AjaxResult getCartCount() {
        return AjaxResult.success(shoppingCartService.getCartCount());
    }

    @LoginRequire
    @GetMapping("/findGoods")
    public AjaxResult findGoods() {
        return AjaxResult.success(shoppingCartService.findGoods());
    }

    @LoginRequire
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody Map<String, Set<Integer>> idMap) {
        shoppingCartService.remove(idMap.get("ids"));
        return AjaxResult.success();
    }

}
