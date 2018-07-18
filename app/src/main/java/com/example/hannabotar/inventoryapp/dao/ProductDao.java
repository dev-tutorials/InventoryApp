package com.example.hannabotar.inventoryapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.hannabotar.inventoryapp.model.Product;

import java.util.List;

/**
 * Created by hanna on 7/18/2018.
 */
@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE name LIKE :name LIMIT 1")
    Product findByName(String name);

    @Query("SELECT * FROM product WHERE uid = :uid")
    Product findByUid(int uid);

    @Insert
    void insert(Product product);

    @Insert
    void insertAll(List<Product> products);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);
}
