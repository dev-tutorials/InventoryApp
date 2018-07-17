package com.example.hannabotar.inventoryapp.api;

import retrofit.http.GET;

/**
 * Created by hanna on 7/17/2018.
 */

public interface InventoryService {

    @GET("/index.php")
    GetItemsResult getItems();

}
