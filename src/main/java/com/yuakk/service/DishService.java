package com.yuakk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.dto.DishDto;
import com.yuakk.dto.SetmealDishDto;
import com.yuakk.pojo.Dish;

import java.util.List;

/**
 * @author yuakk
 */
public interface DishService extends IService<Dish> {

    boolean saveWithFlavor(DishDto dishDto);

    Page<DishDto> getDishByPage(int page,int pageSize,String name);

    DishDto getByIdWithFlavor(Long id);

    boolean updateWithFlavor(DishDto dishDto);

    boolean deleteDish(List<Long> ids);

    boolean changeDishStatus(int status, List<Long> ids);

    /**
     * 根据分类id获取dish和flavor
     * @param dish Dish
     * @return List<DishDto>
     */
    List<DishDto> getDishAndFlavorByCategoryId(Dish dish);




}
