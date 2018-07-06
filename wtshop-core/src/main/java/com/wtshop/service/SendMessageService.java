package com.wtshop.service;


import com.wtshop.model.Order;
import com.wtshop.model.Goods;
import com.wtshop.util.ApiResult;

/**
 * Created by sq on 2017/8/23.
 */
public interface SendMessageService {

    void systemMessage();

    void paySuccessMessage(Order order);

    void noPayMessage(Order order);

    void sendPeoductMessage(Order order);

    void myFavoriteMessage(Goods goods);

    void staffMessage(Goods goods);

}
