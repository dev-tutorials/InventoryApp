package com.example.hannabotar.inventoryapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.hannabotar.inventoryapp.util.AppUtil;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private TextView mInventoryLabel;
    private TextView mOrderHistoryLabel;
    private TextView mSupplierProductsLabel;
    private TextView mEditableListLabel;

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInventoryLabel = (TextView) findViewById(R.id.inventory_label);
        mOrderHistoryLabel = (TextView) findViewById(R.id.order_history_label);
        mSupplierProductsLabel = (TextView) findViewById(R.id.supplier_products_label);
        mEditableListLabel = (TextView) findViewById(R.id.editable_list);




        mInventoryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                startActivity(intent);
            }
        });

        mOrderHistoryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });

        mSupplierProductsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(intent);
            }
        });

        mEditableListLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);
                mediaPlayer.start();

                /*Intent intent = new Intent(MainActivity.this, EditableListActivity.class);
                startActivity(intent);*/
            }
        });

        AppUtil.scheduleJob(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}
