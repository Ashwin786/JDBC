package com.rk.jdbc;

/**
 * Created by user1 on 19/7/19.
 */

class ProductDto {
    String product_id,group_id,quantity,product_price;
    public ProductDto(String product_id, String group_id, String quantity, String product_price) {
        this.product_id = product_id;
        this.group_id = group_id;
        this.quantity = quantity;
        this.product_price = product_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getProduct_price() {
        return product_price;
    }
}
