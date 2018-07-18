package com.example.hannabotar.inventoryapp;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hannabotar.inventoryapp.adapter.ProductAdapter;
import com.example.hannabotar.inventoryapp.database.InventoryDatabase;
import com.example.hannabotar.inventoryapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.add_product)
    FloatingActionButton addButton;

    ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ButterKnife.bind(this);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                List<Product> productList = App.get().getDB().productDao().getAll();
                if (!productList.isEmpty()) {
                    populateProducts(productList);
                }
            }
        }).start();*/

        mAdapter = new ProductAdapter(this, new ArrayList<Product>());

        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = mAdapter.getItem(position);
                Intent editProduct = new Intent(ProductsActivity.this, ProductEditorActivity.class);
                editProduct.putExtra("PRODUCT_UID", product.getUid());
                startActivity(editProduct);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, ProductEditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Product> productList = App.get().getDB().productDao().getAll();
                if (!productList.isEmpty()) {
                    populateProducts(productList);
                }
            }
        }).start();
    }

    private void populateProducts(final List<Product> products) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                mAdapter.addAll(products);
            }
        });
    }
}
