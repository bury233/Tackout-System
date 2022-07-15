package com.tackout.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tackout.system.common.R;
import com.tackout.system.entity.OrderDetail;
import com.tackout.system.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单明细
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/{id}")
    public R<List<OrderDetail>> getOrderDetails(@PathVariable Long id){
        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id!=null,OrderDetail::getOrderId,id);
        List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper);
        return R.success(orderDetails);
    }

}