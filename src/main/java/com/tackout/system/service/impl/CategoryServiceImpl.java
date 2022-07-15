package com.tackout.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tackout.system.common.CustomException;
import com.tackout.system.entity.Category;
import com.tackout.system.entity.Dish;
import com.tackout.system.entity.Setmeal;
import com.tackout.system.mapper.CategoryMapper;
import com.tackout.system.service.CategoryService;
import com.tackout.system.service.DishService;
import com.tackout.system.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    public DishService dishService;

    @Autowired
    public SetmealService setmealService;
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品或套餐
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);

        if(count>0){
            throw new CustomException("存在关联菜品");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        count = setmealService.count(setmealLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("存在关联套餐");
        }
        super.removeById(id);
    }
}
