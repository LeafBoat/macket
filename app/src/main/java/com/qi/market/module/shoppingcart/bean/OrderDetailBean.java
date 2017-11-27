package com.qi.market.module.shoppingcart.bean;

/**
 * Created by Qi on 2017/11/24.
 */

public class OrderDetailBean {
    private Long userid;//用户id
    private Long productid;//产品id
    private Long num;//数量
    private String producttile;//产品名称
    private double productprice;//产品价格
    public boolean isChecked;
    public boolean isInvalid;//商品是否失效

    public void setProducttile(String producttile) {
        this.producttile = producttile;
    }

    public void setProductprice(double productprice) {
        this.productprice = productprice;
    }


    public String getProducttile() {
        return producttile;
    }

    public double getProductprice() {
        return productprice;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Long getProductid() {
        return productid;
    }

    public Long getNum() {
        return num;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUserid() {
        return userid;
    }
}
