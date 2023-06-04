package com.yuakk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.dto.DishDto;
import com.yuakk.pojo.Dish;
import com.yuakk.service.DishService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "dishCache",key = "#dishDto.categoryId+'_'+#dishDto.status")
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
    @CacheEvict(value = "dishCache",allEntries = true)
    public boolean updateDish(@RequestBody DishDto dishDto){
        return dishService.updateWithFlavor(dishDto);
    }

    @DeleteMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public boolean deleteDish(@RequestParam(name = "ids") List<Long> ids){
        return dishService.deleteDish(ids);
    }

    @PostMapping("/status/{status}")
    @CacheEvict(value = "dishCache",allEntries = true)
    public boolean changeDishStatus(@PathVariable int status,@RequestParam List<Long> ids){
        return dishService.changeDishStatus(status, ids);
    }

    @GetMapping("/list")
    @Cacheable(value = "dishCache",key = "#dish.categoryId+'_'+#dish.status",condition = "#result!=null")
    public List<DishDto> getDishByCategoryId(Dish dish ){
        return dishService.getDishAndFlavorByCategoryId(dish);
    }
}
