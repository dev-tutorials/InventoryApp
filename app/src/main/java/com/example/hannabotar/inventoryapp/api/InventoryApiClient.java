package com.example.hannabotar.inventoryapp.api;

import java.util.Calendar;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by hanna on 7/18/2018.
 */

public class InventoryApiClient {

    public static final String WS_ROOT = "http://185.109.255.43";

    private static InventoryService sInventoryService;

    public static InventoryService getInventoryService() {
        if (sInventoryService != null) {
            return sInventoryService;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WS_ROOT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        sInventoryService = restAdapter.create(InventoryService.class);
        return sInventoryService;
    }
}
