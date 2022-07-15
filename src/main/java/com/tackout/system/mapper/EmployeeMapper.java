package com.tackout.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tackout.system.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
