package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.dto.SetmealDishDto;
import com.yuakk.dto.SetmealDto;
import com.yuakk.pojo.Setmeal;
import com.yuakk.service.SetmealService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuakk
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @PostMapping
    @CacheEvict(value = "setMealCache",key = "#setmealDto.categoryId+'_'+#setmealDto.status")
    public boolean save(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto{}",setmealDto);
        return setmealService.saveSetmeal(setmealDto);
    }

    @GetMapping("/page")
    public Page<SetmealDto> getSetmealByPage(int page,int pageSize,String name){
        return setmealService.getSetmealByPage(page, pageSize, name);
    }

    @DeleteMapping
    @CacheEvict(value = "setMealCache",allEntries = true)
    public boolean delete(@RequestParam List<Long> ids){
        return setmealService.deleteSetmealByIds(ids);
    }

    @GetMapping("/{id}")
    public SetmealDto getSetmealById(@PathVariable Long id){
        return setmealService.getSetmealWithDishById(id);
    }

    @PutMapping
    @CacheEvict(value = "setMealCache",allEntries = true)
    public boolean update(@RequestBody SetmealDto setmealDto){
        return setmealService.updateSetMeal(setmealDto);
    }

    @PostMapping("/status/{status}")
    @CacheEvict(value = "setMealCache",allEntries = true)
    public boolean changeStatus(@PathVariable int status, @RequestParam List<Long> ids){
        return setmealService.changeStatusByIds(status,ids);
    }

    @GetMapping("/list")
    @Cacheable(value = "setMealCache",key = "#setmeal.categoryId+'_'+#setmeal.status",condition = "#result!=null")
    public List<Setmeal> getSetmealAndDished(Setmeal setmeal){
        log.info("setMeal{}",setmeal.toString());
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId()!=null, Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        return setmealService.list(lambdaQueryWrapper);
    }

    @GetMapping("/dish/{id}")
    public List<SetmealDishDto> getSetmealDishList(@PathVariable Long id){
        return setmealService.getDetailSetmealDishDto(id);

    }

}
