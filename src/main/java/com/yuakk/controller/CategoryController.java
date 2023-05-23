package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.pojo.Category;
import com.yuakk.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuakk
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public boolean saveCategory(@RequestBody Category category){
        System.out.println(category);
        System.out.println("=============");
        return categoryService.save(category);
    }

    @GetMapping("/page")
    public Page<Category> getCategoryByPage(int page, int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        return categoryService.page(pageInfo, lambdaQueryWrapper);
    }

    @DeleteMapping
    public boolean deleteCategory(Long ids){
        return categoryService.remove(ids);
    }

    @PutMapping
    public boolean update(@RequestBody Category category){
        return categoryService.updateById(category);
    }

    @GetMapping("/list")
    public List<Category> getCategories(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!=null, Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        return categoryService.list(lambdaQueryWrapper);
    }
}
