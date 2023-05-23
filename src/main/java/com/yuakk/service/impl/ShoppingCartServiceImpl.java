package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BaseContext;
import com.yuakk.mapper.ShoppingCartMapper;
import com.yuakk.pojo.ShoppingCart;
import com.yuakk.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yuakk
 */
@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查询是否已有该菜品

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        log.info("userid:{}",BaseContext.getCurrentId());
        lambdaQueryWrapper.eq(shoppingCart.getDishId()!=null, ShoppingCart::getDishId, shoppingCart.getDishId());
        lambdaQueryWrapper.eq(shoppingCart.getSetmealId()!=null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart exitShoppingCart = this.getOne(lambdaQueryWrapper);

        if (exitShoppingCart!=null){
            log.info("==============111");
            Integer number = exitShoppingCart.getNumber();
            exitShoppingCart.setNumber(number+1);
            this.updateById(exitShoppingCart);
        } else {
            shoppingCart.setNumber(1);
            log.info("shoppingCart{}",shoppingCart);
            this.save(shoppingCart);
            exitShoppingCart=shoppingCart;
        }
        return exitShoppingCart;
    }

    @Override
    public boolean subShoppingCart(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> shoppingLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingLambdaQueryWrapper.eq(shoppingCart.getDishId()!=null, ShoppingCart::getDishId, shoppingCart.getDishId());
        shoppingLambdaQueryWrapper.eq(shoppingCart.getSetmealId()!=null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingOne = this.getOne(shoppingLambdaQueryWrapper);

        if (shoppingOne!=null && shoppingOne.getNumber()==1){
            return this.removeById(shoppingOne.getId());
        } else {
            assert shoppingOne != null;
            shoppingOne.setNumber(shoppingOne.getNumber()-1);
            return this.updateById(shoppingOne);
        }
    }
}
