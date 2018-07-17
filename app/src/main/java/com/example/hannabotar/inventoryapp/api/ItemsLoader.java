package com.example.hannabotar.inventoryapp.api;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.hannabotar.inventoryapp.model.InventoryItem;

import java.util.List;

/**
 * Created by hanna on 7/17/2018.
 */

public class ItemsLoader extends AsyncTaskLoader<List<InventoryItem>> {

    private String mUrl;

    public ItemsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<InventoryItem> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return ItemsUtil.fetchItems(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
