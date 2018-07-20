package com.example.hannabotar.inventoryapp;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

import com.example.hannabotar.inventoryapp.database.InventoryDatabase;
import com.example.hannabotar.inventoryapp.database.OrderDatabase;
import com.example.hannabotar.inventoryapp.model.OrderDetails;

/**
 * Created by gonzalo on 7/14/17
 */

public class App extends Application {

    public static App INSTANCE;
    private static final String DATABASE_NAME = "InventoryDatabase";
    private static final String PREFERENCES = "RoomDemo.preferences";
    private static final String KEY_FORCE_UPDATE = "force_update";

    private static final String ORDER_DATABASE_NAME = "OrderDatabase";


    private InventoryDatabase database;

    private OrderDatabase orderDatabase;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        database = Room.databaseBuilder(getApplicationContext(), InventoryDatabase.class, DATABASE_NAME)
//                .addMigrations(InventoryDatabase.MIGRATION_1_2)
                .build();

        orderDatabase = Room.databaseBuilder(getApplicationContext(), OrderDatabase.class, ORDER_DATABASE_NAME).build();

        INSTANCE = this;
    }

    public InventoryDatabase getDB() {
        return database;
    }

    public OrderDatabase getOrderDB() {
        return orderDatabase;
    }

    public boolean isForceUpdate() {
        return getSP().getBoolean(KEY_FORCE_UPDATE, true);
    }

    public void setForceUpdate(boolean force) {
        SharedPreferences.Editor edit = getSP().edit();
        edit.putBoolean(KEY_FORCE_UPDATE, force);
        edit.apply();
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }
}
