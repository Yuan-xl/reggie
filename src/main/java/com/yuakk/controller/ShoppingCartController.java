package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuakk.common.BaseContext;
import com.yuakk.pojo.ShoppingCart;
import com.yuakk.service.ShoppingCartService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuakk
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public List<ShoppingCart> getShoppingCartList(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        return shoppingCartService.list(lambdaQueryWrapper);
    }

    @PostMapping("/add")
    public ShoppingCart saveShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.saveShoppingCart(shoppingCart);
    }

    @DeleteMapping("/clean")
    public boolean cleanShoppingCart(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        return shoppingCartService.remove(lambdaQueryWrapper);
    }

    @PostMapping("/sub")
    public boolean subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.subShoppingCart(shoppingCart);
    }
}
