package com.tackout.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class SetmealDTo extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private List<SetmealDishDTo> setmealDish0;
    private String CategoryName;
}
