package com.example.hannabotar.inventoryapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.hannabotar.inventoryapp.api.GetItemsResult;
import com.example.hannabotar.inventoryapp.api.InventoryApiClient;
import com.example.hannabotar.inventoryapp.data.InventoryDbHelper;
import com.example.hannabotar.inventoryapp.data.ItemContract;
import com.example.hannabotar.inventoryapp.model.InventoryItem;


/**
 * Created by hanna on 7/18/2018.
 */
public class GetItemsService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GetItemsService() {
        super("GetItemsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean success = false;
        try {
            GetItemsResult result = InventoryApiClient.getInventoryService().getItems();
            if (result != null && result.getItems() != null) {

                InventoryDbHelper dbHelper = new InventoryDbHelper(getApplicationContext());
                final SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    db.delete(ItemContract.ItemEntry.TABLE_NAME, null, null);
                    // reset AUTOINCREMENT
//                    db.execSQL("delete from sqlite_sequence where name='" + ItemContract.ItemEntry.TABLE_NAME + "'");
                    for (InventoryItem item: result.getItems()) {
                        ContentValues values = new ContentValues();
                        values.put(ItemContract.ItemEntry.COLUMN_NAME, item.getName());
                        values.put(ItemContract.ItemEntry.COLUMN_SERIAL_NO, item.getSerialNo());
                        values.put(ItemContract.ItemEntry.COLUMN_CONDITION, item.getCondition());
                        values.put(ItemContract.ItemEntry.COLUMN_DESCRIPTION, item.getDescription());
                        db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
                    }
                    db.setTransactionSuccessful();
                    success = true;
                } catch (Exception e) {
                    success = false;
                } finally {
                    db.endTransaction();
                }
            }
            success = true;
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        if (!success)
            return;

        // notify content changed
        getApplicationContext().getContentResolver().notifyChange(ItemContract.ItemEntry.CONTENT_URI, null, false);
    }
}
