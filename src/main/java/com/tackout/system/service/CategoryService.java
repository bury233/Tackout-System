package com.tackout.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tackout.system.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
