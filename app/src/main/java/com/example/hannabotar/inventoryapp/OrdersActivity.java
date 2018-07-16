package com.example.hannabotar.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrdersActivity extends AppCompatActivity {

    private TextView mQuantityName;
    private TextView mDate;
    private TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mQuantityName = (TextView) findViewById(R.id.quantity_name_text);
        mDate = (TextView) findViewById(R.id.date_text);
        mPrice = (TextView) findViewById(R.id.price_text);

        mQuantityName.setText("2 x Lenovo E540");
        mDate.setText("01/05/2018");
        mPrice.setText("2000$");
    }
}
