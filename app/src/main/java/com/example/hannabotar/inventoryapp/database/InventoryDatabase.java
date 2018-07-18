package com.example.hannabotar.inventoryapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.hannabotar.inventoryapp.dao.ProductDao;
import com.example.hannabotar.inventoryapp.model.Product;

/**
 * Created by hanna on 7/18/2018.
 */
@Database(entities = {Product.class}, version = 1)
public abstract class InventoryDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
