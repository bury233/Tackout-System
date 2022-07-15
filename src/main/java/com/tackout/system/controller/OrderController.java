package com.tackout.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tackout.system.common.R;
import com.tackout.system.entity.OrderDetail;
import com.tackout.system.entity.Orders;
import com.tackout.system.entity.OrdersDto;
import com.tackout.system.service.OrderDetailService;
import com.tackout.system.service.OrderService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    public OrderService orderService;

    @Autowired
    public OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){
        orderService.submit(order);
        return R.success("ok");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize, HttpServletRequest request){
        Page<Orders> page1 = new Page<>(page,pageSize);
        Page<OrdersDto> page2 = new Page<>();
        Long userid = (Long)request.getSession().getAttribute("user");
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId,userid);
        lambdaQueryWrapper.orderByAsc(Orders::getOrderTime);
        orderService.page(page1,lambdaQueryWrapper);
        BeanUtils.copyProperties(page1,page2,"records");


        List<Orders> records = page1.getRecords();

        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto dishDto = new OrdersDto();

            BeanUtils.copyProperties(item,dishDto);

            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(OrderDetail::getOrderId,item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper1);

            dishDto.setOrderDetails(orderDetails);
            return dishDto;
        }).collect(Collectors.toList());

        page2.setRecords(list);

        return R.success(page2);
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        return R.success("ok");
    }

    @GetMapping("/page")
    public R<Page> getPage(int page, int pageSize, HttpServletRequest request, Long number, String beginTime,String endTime){
        Page<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Orders::getOrderTime);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(beginTime!=null && endTime!=null) {
            LocalDateTime b_time = LocalDateTime.parse(beginTime, df);
            LocalDateTime e_time = LocalDateTime.parse(beginTime, df);
            lambdaQueryWrapper.between(Orders::getOrderTime, beginTime, endTime);
        }
        lambdaQueryWrapper.eq(number!=null,Orders::getId,number);
        orderService.page(page1,lambdaQueryWrapper);
        return R.success(page1);
    }

    @PutMapping
    public R<String> changeStatus(@RequestBody Orders orders){
        orderService.updateById(orders);
        return R.success("ok");
    }
}
