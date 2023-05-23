package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.dto.DishDto;
import com.yuakk.pojo.Dish;
import com.yuakk.service.DishService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author yuakk
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @PostMapping
    public boolean save(@RequestBody DishDto dishDto){
        return dishService.saveWithFlavor(dishDto);
    }

    @GetMapping("/page")
    public Page<DishDto> page(int page,int pageSize,String name){

        return dishService.getDishByPage(page, pageSize, name);
    }

    @GetMapping("/{id}")
    public DishDto getDishById(@PathVariable Long id){
        return dishService.getByIdWithFlavor(id);
    }

    @PutMapping
    public boolean updateDish(@RequestBody DishDto dishDto){
        return dishService.updateWithFlavor(dishDto);
    }

    @DeleteMapping
    public boolean deleteDish(@RequestParam(name = "ids") List<Long> ids){
        return dishService.deleteDish(ids);
    }

    @PostMapping("/status/{status}")
    public boolean changeDishStatus(@PathVariable int status,@RequestParam List<Long> ids){
        return dishService.changeDishStatus(status, ids);
    }

    @GetMapping("/list")
    public List<DishDto> getDishByCategoryId(Dish dish ){
        return dishService.getDishAndFlavorByCategoryId(dish);
    }
}
