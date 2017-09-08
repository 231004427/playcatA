package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.Order;
import com.sunlin.playcat.domain.OrderList;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/8/16.
 */

public class OrderRESTful extends ObjRESTful {
    public OrderRESTful(User _user) {
        super(_user);
    }

    public void Add(Order order, RestTask.ResponseCallback responseCallback) {
        String dataStr = gson.toJson(order);
        super.requestPost(ActionType.ORDER_ADD, dataStr, responseCallback);
    }

    public void search(OrderList orderList, RestTask.ResponseCallback responseCallback) {
        String dataStr = gson.toJson(orderList);
        super.requestPost(ActionType.ORDER_SEARCH, dataStr, responseCallback);
    }
}
