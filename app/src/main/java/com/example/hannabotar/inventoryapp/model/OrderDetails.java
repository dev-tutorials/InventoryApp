package com.example.hannabotar.inventoryapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "order_details",
        foreignKeys = { @ForeignKey(entity = Order.class, parentColumns = "uid", childColumns = "order_id" )})
public class OrderDetails {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "quantity")
    private int quantity;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
