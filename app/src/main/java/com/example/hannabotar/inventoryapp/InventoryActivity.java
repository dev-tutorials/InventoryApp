package com.example.hannabotar.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InventoryActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mName;
    private TextView mSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mImage = (ImageView) findViewById(R.id.item_image);
        mName = (TextView) findViewById(R.id.item_name);
        mSerial = (TextView) findViewById(R.id.item_serial);

        mImage.setImageResource(R.drawable.laptop);
        mName.setText("Lenovo E540");
        mSerial.setText("VGSHA45678XXXXXXX");
    }
}
