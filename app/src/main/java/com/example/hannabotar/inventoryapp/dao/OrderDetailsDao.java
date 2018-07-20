package com.example.hannabotar.inventoryapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.hannabotar.inventoryapp.model.OrderDetails;

import java.util.List;

@Dao
public interface OrderDetailsDao {

    @Query("SELECT * FROM [order_details] where order_id = :orderId")
    List<OrderDetails> getAllOrderDetailsByOrderUid(int orderId);

    @Query("SELECT * FROM [order_details] where uid = :uid")
    OrderDetails getByUid(int uid);

    @Insert
    Long insert(OrderDetails orderDetails);

    @Update
    int update(OrderDetails orderDetails);

    @Delete
    int delete(OrderDetails orderDetails);

}
