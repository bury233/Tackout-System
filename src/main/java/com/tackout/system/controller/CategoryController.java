package com.tackout.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tackout.system.common.R;
import com.tackout.system.entity.Category;
import com.tackout.system.entity.Employee;
import com.tackout.system.service.CategoryService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> getpage(int page,int pageSize){
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<Category>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> insertCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }

    @PutMapping
    public R<String> editCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("删除成功");
    }

    @DeleteMapping
    public R<String> deleteCtegory(Long ids){
        categoryService.remove(ids);//使用自己实现的方法
        return  R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
