package com.yuakk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.pojo.ShoppingCart;

/**
 * @author yuakk
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 保存购物车
     * @param shoppingCart ShoppingCart
     * @return ShoppingCart
     */
    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);

    /**
     * 减少购物车数量
     * @param shoppingCart ShoppingCart
     * @return boolean
     */
    boolean subShoppingCart(ShoppingCart shoppingCart);
}
