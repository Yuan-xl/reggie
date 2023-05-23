package com.yuakk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.pojo.Category;

/**
 * @author yuakk
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据categoryId删除
     * @param id 菜品id
     * @return boolean
     */
    boolean remove(Long id);
}
