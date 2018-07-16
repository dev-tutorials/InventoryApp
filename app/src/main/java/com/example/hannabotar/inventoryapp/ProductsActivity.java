package com.example.hannabotar.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductsActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mName;
    private TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mImage = (ImageView) findViewById(R.id.product_image);
        mName = (TextView) findViewById(R.id.product_name);
        mPrice = (TextView) findViewById(R.id.product_price);

        mImage.setImageResource(R.drawable.phone);
        mName.setText("Samsung A3");
        mPrice.setText("200$");
    }
}
