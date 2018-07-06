package com.wtshop.controller.admin;

import com.wtshop.model.Order;
import com.wtshop.service.OrderService;


import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/4.
 */
public class DoTimeController extends BaseController {

    private OrderService orderService = enhance(OrderService.class);



//    public void run() {
//        List<Order> expriceOrder = orderService.findExpriceOrder();
//        for(Order order :expriceOrder){
//            order.setStatus(Order.Status.denied.ordinal());
//            order.setExpire(null);
//            orderService.update(order);
//        }
//    }
}
