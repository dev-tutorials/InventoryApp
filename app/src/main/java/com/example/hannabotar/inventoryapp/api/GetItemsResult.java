package com.example.hannabotar.inventoryapp.api;

import com.example.hannabotar.inventoryapp.model.InventoryItem;

import java.util.List;

/**
 * Created by hanna on 7/18/2018.
 */

public class GetItemsResult {

    private List<InventoryItem> inventoryItems;

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}
