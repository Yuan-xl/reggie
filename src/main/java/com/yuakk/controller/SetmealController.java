package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.dto.SetmealDishDto;
import com.yuakk.dto.SetmealDto;
import com.yuakk.pojo.Setmeal;
import com.yuakk.pojo.SetmealDish;
import com.yuakk.service.SetmealDishService;
import com.yuakk.service.SetmealService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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

    @Resource
    private SetmealDishService setmealDishService;


    @PostMapping
    public boolean save(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto{}",setmealDto);
        return setmealService.saveSetmeal(setmealDto);
    }

    @GetMapping("/page")
    public Page<SetmealDto> getSetmealByPage(int page,int pageSize,String name){
        return setmealService.getSetmealByPage(page, pageSize, name);
    }

    @DeleteMapping
    public boolean delete(@RequestParam List<Long> ids){
        return setmealService.deleteSetmealByIds(ids);
    }

    @GetMapping("/{id}")
    public SetmealDto getSetmealById(@PathVariable Long id){
        return setmealService.getSetmealWithDishById(id);
    }

    @PutMapping
    public boolean update(@RequestBody SetmealDto setmealDto){
        return setmealService.updateSetMeal(setmealDto);
    }

    @PostMapping("/status/{status}")
    public boolean changeStatus(@PathVariable int status, @RequestParam List<Long> ids){
        return setmealService.changeStatusByIds(status,ids);
    }

    @GetMapping("/list")
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
