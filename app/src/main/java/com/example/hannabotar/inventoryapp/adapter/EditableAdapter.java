package com.example.hannabotar.inventoryapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hannabotar.inventoryapp.R;
import com.example.hannabotar.inventoryapp.model.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditableAdapter extends ArrayAdapter<Person> {

    private List<Person> mPersonList;

    public EditableAdapter(@NonNull Context context, @NonNull List<Person> objects) {
        super(context, 0, objects);
        mPersonList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.editable_item, parent, false);
        }

        final Person currentPerson = getItem(position);

        EditText nameTv = (EditText) convertView.findViewById(R.id.name_tv);
        TextView dateTv = (TextView) convertView.findViewById(R.id.date_tv);
        TextView priorityTv = (TextView) convertView.findViewById(R.id.prior_tv);
        final CheckBox shippedTv = (CheckBox) convertView.findViewById(R.id.shipped_cb);
        TextView totalTv = (TextView) convertView.findViewById(R.id.total_tv);

        nameTv.setText(currentPerson.getName());
        dateTv.setText(formatDate(currentPerson.getDate()));
        priorityTv.setText(currentPerson.getPriority());
        shippedTv.setChecked(currentPerson.getShipped());
        totalTv.setText(currentPerson.getTotal().toString());

        shippedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPerson.setShipped(shippedTv.isChecked());
            }
        });

        nameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newName = s.toString();
                currentPerson.setName(newName);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private String formatDate(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/YYYY");
        return sdf.format(d);
    }
}
