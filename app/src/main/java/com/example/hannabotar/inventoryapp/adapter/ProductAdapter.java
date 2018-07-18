package com.example.hannabotar.inventoryapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hannabotar.inventoryapp.R;
import com.example.hannabotar.inventoryapp.model.Product;

import java.util.List;

/**
 * Created by hanna on 7/18/2018.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(@NonNull Context context, @NonNull List<Product> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
        }

        final Product currentProduct = getItem(position);

        TextView productName = (TextView) convertView.findViewById(R.id.product_name);
        TextView productPrice = (TextView) convertView.findViewById(R.id.product_price);
        productName.setText(currentProduct.getName());
        productPrice.setText(currentProduct.getPrice().toString());

        return convertView;
    }
}
