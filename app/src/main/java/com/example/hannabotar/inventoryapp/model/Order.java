package com.example.hannabotar.inventoryapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.Date;
import java.util.List;

@Entity(tableName = "order")
public class Order {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "order_code")
    private String orderCode;

    /*@ColumnInfo(name = "order_date")
    private Date orderDate;*/

//    @Relation(parentColumn = "uid", entityColumn = "order_id", entity = OrderDetails.class)
//    private List<OrderDetails> orderDetails;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

   /* public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }*/

   /* public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }*/
}
