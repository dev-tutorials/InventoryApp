package com.example.hannabotar.inventoryapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.hannabotar.inventoryapp.dao.OrderDao;
import com.example.hannabotar.inventoryapp.dao.OrderDetailsDao;
import com.example.hannabotar.inventoryapp.model.Order;
import com.example.hannabotar.inventoryapp.model.OrderDetails;

@Database(entities = {Order.class, OrderDetails.class}, version = 1)
public abstract class OrderDatabase extends RoomDatabase{
    public abstract OrderDao getOrderDao();
    public abstract OrderDetailsDao getOrderDetailsDao();
}
