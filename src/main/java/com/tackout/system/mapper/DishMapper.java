package com.tackout.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tackout.system.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
