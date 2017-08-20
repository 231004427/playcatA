package com.sunlin.playcat.domain;

/**
 * Created by sunlin on 2017/8/16.
 */

public class OrderMore {
    private Order order;
    private Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public Order getOrder() {
        return order;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}