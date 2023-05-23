package com.yuakk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.dto.SetmealDishDto;
import com.yuakk.dto.SetmealDto;
import com.yuakk.pojo.Setmeal;

import java.util.List;

/**
 * @author yuakk
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 保存套餐及其具体菜品
     * @param setmealDto Dto
     * @return boolean
     */
    boolean saveSetmeal(SetmealDto setmealDto);

    /**
     * 分页查询
     * @param page 当前页数
     * @param pageSize 每页大小
     * @param name 搜索名称
     * @return Page<SetmealDto>
     */
    Page<SetmealDto> getSetmealByPage(int page,int pageSize,String name);

    /**
     * 删除setMeal
     * @param ids setmealId
     * @return boolean
     */
    boolean deleteSetmealByIds(List<Long> ids);

    /**
     * 获取套餐详情
     * @param id setMealId
     * @return setMealDto
     */
    SetmealDto getSetmealWithDishById(Long id);

    /**
     * 更新
     * @param setmealDto dto
     * @return boolean
     */
    boolean updateSetMeal(SetmealDto setmealDto);

    /**
     * 更改状态
     * @param status 状态
     * @param ids List<ids></ids>
     * @return boolean
     */
    boolean changeStatusByIds(int status,List<Long> ids);

    /**
     * 获取套餐详情
     * @param id id
     * @return List<SetmealDishDto>
     */
    List<SetmealDishDto> getDetailSetmealDishDto(Long id);
}
