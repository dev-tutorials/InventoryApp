package com.example.hannabotar.inventoryapp.api;

import com.example.hannabotar.inventoryapp.model.InventoryItem;

import java.util.List;

/**
 * Created by hanna on 7/18/2018.
 */

public class GetItemsResult {

    private List<InventoryItem> items;

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
}
