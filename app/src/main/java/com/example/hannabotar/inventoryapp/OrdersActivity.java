package com.example.hannabotar.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

//    private OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // TODO
//        listView.setAdapter(mAdapter);
    }
}
