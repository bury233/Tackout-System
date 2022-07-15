package com.tackout.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tackout.system.common.R;
import com.tackout.system.entity.*;
import com.tackout.system.service.CategoryService;
import com.tackout.system.service.DishService;
import com.tackout.system.service.SetmealDishService;
import com.tackout.system.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    public SetmealService setmealService;

    @Autowired
    public DishService dishService;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page> getSetmealPage(int page, int pageSize, String name){
        Page<Setmeal> page1 = new Page<>(page,pageSize);
        Page<SetmealDTo> page2 = new Page<>();
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(page1,lambdaQueryWrapper);

        BeanUtils.copyProperties(page1,page2,"records");//忽略records属性

        List<Setmeal> records = page1.getRecords();

        List<SetmealDTo> setmealDToList = records.stream().map((item)->{
            SetmealDTo setmealDTo = new SetmealDTo();
            BeanUtils.copyProperties(item,setmealDTo);
            Long categoryid = item.getCategoryId();
            String CatecotyName = categoryService.getById(categoryid).getName();
            setmealDTo.setCategoryName(CatecotyName);
            return setmealDTo;
        }).collect(Collectors.toList());

        page2.setRecords(setmealDToList);
        return R.success(page2);
    }

    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam(value = "ids") List<Long> ids){
        setmealService.removeByIds(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> SetmealStatusByStatus(@PathVariable int status,@RequestParam(value = "ids") List<Long> ids){
        for (Long id: ids) {
            Setmeal dish = new Setmeal();
            dish.setId(id);
            dish.setStatus(status);
            setmealService.updateById(dish);
        }
        return R.success("ok");
    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDTo setmealDTo){
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTo,setmeal);
        setmealService.save(setmeal);
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTo.getSetmealDishes().stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
        return R.success("ok");
    }

    @GetMapping("/{id}")
    public R<SetmealDTo> querySetmealById(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        SetmealDTo setmealDTo = new SetmealDTo();
        BeanUtils.copyProperties(setmeal,setmealDTo);
        Long categoryid = setmeal.getCategoryId();
        String categoryname = categoryService.getById(categoryid).getName();
        setmealDTo.setCategoryName(categoryname);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDishDTo> setmealDishList = setmealDishService.list(lambdaQueryWrapper).stream().map((item)->{
            SetmealDishDTo setmealDishDTo = new SetmealDishDTo();
            BeanUtils.copyProperties(item,setmealDishDTo);
            String image = dishService.getById(item.getDishId()).getImage();
            setmealDishDTo.setImage(image);
            return setmealDishDTo;
        }).collect(Collectors.toList());
        setmealDTo.setSetmealDish0(setmealDishList);
        return R.success(setmealDTo);
    }

    @PutMapping
    public R<String> editSetmeal(@RequestBody SetmealDTo setmealDTo){
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTo,setmeal);
        setmealService.updateById(setmeal);
        Long setmealId = setmeal.getId();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);

        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDTo.getSetmealDishes().stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
        return R.success("ok");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(setmeal.getName()), Setmeal::getName, setmeal.getName());
        queryWrapper.eq(null != setmeal.getCategoryId(), Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(null != setmeal.getStatus(), Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        return R.success(setmealService.list(queryWrapper));
    }
}
