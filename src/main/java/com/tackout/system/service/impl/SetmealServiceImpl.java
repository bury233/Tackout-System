package com.tackout.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tackout.system.entity.Category;
import com.tackout.system.entity.Setmeal;
import com.tackout.system.mapper.CategoryMapper;
import com.tackout.system.mapper.SetmealMapper;
import com.tackout.system.service.CategoryService;
import com.tackout.system.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
