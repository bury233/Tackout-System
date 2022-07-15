package com.tackout.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tackout.system.common.R;
import com.tackout.system.entity.Employee;
import com.tackout.system.entity.User;
import com.tackout.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    public UserService userService;

    @PostMapping("registry")
    public R<String> registry(@RequestBody User user) {
        user.setPsword(DigestUtils.md5DigestAsHex(user.getPsword().getBytes()));
        userService.save(user);
        return R.success("ok");
    }

    @PostMapping("login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        String phone = user.getPhone();
        String psword = user.getPsword();
        psword = DigestUtils.md5DigestAsHex(psword.getBytes());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.eq(User::getPhone, phone);

        /*
        Employee::getUsername
        先实例化一个Employee，再调用getUsername
        eq相当于赋值
        */

        User resultE = userService.getOne(lambdaQueryWrapper);

        if (resultE == null) {
            return R.error("登陆失败");
        }

        if (!resultE.getPsword().equals(psword)) {
            return R.error("登陆失败");
        }

        if (resultE.getStatus() == 0) {
            return R.error("用户状态禁止");
        }

        request.getSession().setAttribute("user", resultE.getId());
        return R.success(resultE);
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("ok");
    }
}