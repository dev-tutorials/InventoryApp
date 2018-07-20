package com.example.hannabotar.inventoryapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.hannabotar.inventoryapp.model.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM [order]")
    List<Order> getAllOrders();

    @Query("SELECT * FROM [order] where uid = :uid")
    Order getByUid(int uid);

    @Insert
    Long insert(Order order);

    @Update
    int update(Order order);

    @Delete
    int delete(Order order);

}
