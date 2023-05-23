package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.mapper.CategoryMapper;
import com.yuakk.pojo.Category;
import com.yuakk.pojo.Dish;
import com.yuakk.pojo.Setmeal;
import com.yuakk.service.CategoryService;
import com.yuakk.service.DishService;
import com.yuakk.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuakk
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public boolean remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        if (dishService.count(dishLambdaQueryWrapper)>0){
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0403);
        }

        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setMealLambdaQueryWrapper)>0){
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0405);
        }
        return super.removeById(id);
    }
}
