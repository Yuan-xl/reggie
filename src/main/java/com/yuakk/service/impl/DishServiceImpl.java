package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.dto.DishDto;
import com.yuakk.mapper.CategoryMapper;
import com.yuakk.mapper.DishMapper;
import com.yuakk.mapper.SetmealDishMapper;
import com.yuakk.mapper.SetmealMapper;
import com.yuakk.pojo.*;
import com.yuakk.service.DishFlavorService;
import com.yuakk.service.DishService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yuakk
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SetmealDishMapper setmealDishMapper;

    @Resource
    private SetmealMapper setmealMapper;

    @Resource
    private RedisTemplate<String,List<DishDto>> redisTemplate;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveWithFlavor(DishDto dishDto) {

        String key = "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        Set<String> keys = redisTemplate.keys("dish_*");
        assert keys != null;
        redisTemplate.delete(keys);

        //保存dish
        boolean saveDish = this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek((item)-> item.setDishId(dishId)).collect(Collectors.toList());
        boolean saveFlavor = dishFlavorService.saveBatch(flavors);
        return saveDish&&saveFlavor;
    }

    @Override
    public Page<DishDto> getDishByPage(int page, int pageSize, String name) {

        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null, Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        this.page(pageInfo, lambdaQueryWrapper);
        BeanUtils.copyProperties(pageInfo, dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateWithFlavor(DishDto dishDto) {
        String key = "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        boolean dish = this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        boolean deleteDishIdInFlavor = dishFlavorService.remove(lambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors=flavors.stream().map((item)-> {
            item.setId(null);
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        boolean saveFlavorBatch = dishFlavorService.saveBatch(flavors);
        return dish && deleteDishIdInFlavor && saveFlavorBatch;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDish(List<Long> ids) {

        for (Long id : ids) {
            //是否停售
            LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(Dish::getStatus, 1).eq(Dish::getId, id);
            Dish dish = this.getOne(dishLambdaQueryWrapper);
            if (dish!=null){
                throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0416);
            }
            //是否存在于菜品
            LambdaQueryWrapper<SetmealDish> setMealDishWrapper=new LambdaQueryWrapper<>();
            setMealDishWrapper.in(SetmealDish::getDishId, ids);
            List<SetmealDish> setmealDishes = setmealDishMapper.selectList(setMealDishWrapper);
            if (setmealDishes!=null && setmealDishes.size()>0){
                throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0415);
            }
            //是否有关联口味
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, id);
            Map<String, Object> map = dishFlavorService.getMap(lambdaQueryWrapper);
            if (map!=null){
                boolean removeDishFlavor = dishFlavorService.remove(lambdaQueryWrapper);
                if (!removeDishFlavor){
                    throw new BizException(400, ErrorCodeEnum.USER_ERROR_0001);
                }
            }
        }
        return this.removeBatchByIds(ids);
    }

    @Override
    public boolean changeDishStatus(int status, List<Long> ids) {
        if (status==0){
            LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(ids.size()>0, SetmealDish::getDishId,ids);
            List<SetmealDish> setmealDishes = setmealDishMapper.selectList(lambdaQueryWrapper);
            Set<Long> setMealIdSet = setmealDishes.stream().map(SetmealDish::getSetmealId).collect(Collectors.toSet());
            LambdaQueryWrapper<Setmeal> lambdaWrapper=new LambdaQueryWrapper<>();
            lambdaWrapper.in(setMealIdSet.size()>0, Setmeal::getId,setMealIdSet);
            lambdaWrapper.eq(Setmeal::getStatus,1);
            if (setmealMapper.selectCount(lambdaWrapper)>0){
                throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0418);
            }
        }
        List<Dish> dishList;
        dishList=ids.stream().map((item)->{
            Dish dish = new Dish();
            dish.setId(item);
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        return this.updateBatchById(dishList);
    }

    @Override
    public List<DishDto> getDishAndFlavorByCategoryId(Dish dish) {
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //先从redis中取，存在直接返回，反之从数据库查询
        List<DishDto> dishDtoList = null;
        dishDtoList = redisTemplate.opsForValue().get(key);
        if (dishDtoList != null){
            return dishDtoList;
        }

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId())
                .like(dish.getName()!=null, Dish::getName,dish.getName());
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = this.list(lambdaQueryWrapper);

        dishDtoList = dishes.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryMapper.selectById(item.getCategoryId());
            if (category!=null){
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> lambdaWrapper = new LambdaQueryWrapper<>();
            lambdaWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);

        return dishDtoList;
    }


}
