package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.dto.SetmealDishDto;
import com.yuakk.dto.SetmealDto;
import com.yuakk.mapper.CategoryMapper;
import com.yuakk.mapper.DishMapper;
import com.yuakk.mapper.SetmealMapper;
import com.yuakk.pojo.Category;
import com.yuakk.pojo.Dish;
import com.yuakk.pojo.Setmeal;
import com.yuakk.pojo.SetmealDish;
import com.yuakk.service.SetmealDishService;
import com.yuakk.service.SetmealService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yuakk
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private DishMapper dishMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSetmeal(SetmealDto setmealDto) {
        boolean saveSetmeal = this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> collect = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        boolean saveSetmealDish = setmealDishService.saveBatch(collect);
        return saveSetmeal && saveSetmealDish;
    }

    @Override
    public Page<SetmealDto> getSetmealByPage(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> lambda = new LambdaQueryWrapper<>();
        lambda.like(name!=null, Setmeal::getName, name);
        lambda.orderByDesc(Setmeal::getUpdateTime);
        this.page(pageInfo, lambda);

        BeanUtils.copyProperties(pageInfo.getRecords(), setmealDtoPage.getRecords(),"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return setmealDtoPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSetmealByIds(List<Long> ids) {

        LambdaQueryWrapper<Setmeal> lambdaQueryWrap=new LambdaQueryWrapper<>();
        lambdaQueryWrap.in(Setmeal::getId, ids);
        lambdaQueryWrap.eq(Setmeal::getStatus, 1);
        long count = this.count(lambdaQueryWrap);
        if (count > 0) {
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0416);
        }

        boolean removeSetMealByIds = this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        boolean removeSetMealDish = setmealDishService.remove(lambdaQueryWrapper);

        return removeSetMealDish && removeSetMealByIds;
    }

    @Override
    public SetmealDto getSetmealWithDishById(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(lambdaQueryWrapper);

        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    @Override
    public boolean updateSetMeal(SetmealDto setmealDto) {
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        boolean updateSetMeal = this.updateById(setmealDto);
        boolean removeSetMealDish = setmealDishService.remove(lambdaQueryWrapper);
        if (!removeSetMealDish || !updateSetMeal){
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_0001);
        }

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        return setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    public boolean changeStatusByIds(int status, List<Long> ids) {
        if (status==1){
            LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(ids.size()>0, SetmealDish::getSetmealId,ids);
            List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);

            Set<Long> dishIdSet = setmealDishList.stream().map(SetmealDish::getDishId).collect(Collectors.toSet());
            LambdaQueryWrapper<Dish> lambdaWrapper=new LambdaQueryWrapper<>();
            lambdaWrapper.in(dishIdSet.size()>0, Dish::getId,dishIdSet);
            lambdaWrapper.eq(Dish::getStatus, 0);
            if (dishMapper.selectCount(lambdaWrapper)>0){
                throw new  BizException(400,ErrorCodeEnum.USER_ERROR_A0417);
            }
        }

        List<Setmeal> setmealList = ids.stream().map((item) -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(item);
            return setmeal;
        }).collect(Collectors.toList());
        return this.updateBatchById(setmealList);
    }


    @Override
    public List<SetmealDishDto> getDetailSetmealDishDto(Long id) {
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id!=null, SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(lambdaQueryWrapper);

        return setmealDishes.stream().map((item) -> {
            SetmealDishDto setmealDishDto = new SetmealDishDto();
            BeanUtils.copyProperties(item, setmealDishDto);

            Long dishId = item.getDishId();
            Dish dish = dishMapper.selectById(dishId);
            setmealDishDto.setImage(dish.getImage());
            return setmealDishDto;
        }).collect(Collectors.toList());
    }
}
