package com.tackout.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tackout.system.common.BaseContext;
import com.tackout.system.common.R;
import com.tackout.system.entity.Employee;
import com.tackout.system.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    public EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String username = employee.getUsername();
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<Employee>();
        lambdaQueryWrapper.eq(Employee::getUsername,username);

        /*
        Employee::getUsername
        先实例化一个Employee，再调用getUsername
        eq相当于赋值
        */

        Employee resultE = employeeService.getOne(lambdaQueryWrapper);

        if (resultE==null){
            return R.error("登陆失败");
        }

        if (!resultE.getPassword().equals(password)){
            return R.error("登陆失败");
        }

        if (resultE.getStatus()==0){
            return R.error("用户状态禁止");
        }

        request.getSession().setAttribute("employee",resultE.getId());
        return R.success(resultE);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("登出");
    }

    @PostMapping
    public R<String> insertEmployee(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("插入成功");
    }

    @GetMapping("/page")
    public R<Page> getEmployeeInfo(int page, int pageSize, String name){//name为员工姓名
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<Employee>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);//依据name过滤
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> enableOrDisableEmployee(HttpServletRequest request,@RequestBody Employee employee){
        Long id = employee.getId();
        Long ManagerId = (Long)request.getSession().getAttribute("employee");

        employee.setUpdateUser(ManagerId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("更改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> queryEmployeeById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("未知错误");
    }

    @PutMapping("edit")
    public R<String> editEmployee(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("更新成功");
    }
}
