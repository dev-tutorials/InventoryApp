package com.example.hannabotar.inventoryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.hannabotar.inventoryapp.adapter.EditableAdapter;
import com.example.hannabotar.inventoryapp.model.Person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditableListActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;

    private EditableAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_list);

        ButterKnife.bind(this);

//        List<Person> list = getList();

        mAdapter = new EditableAdapter(this, new ArrayList<>());

        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mAdapter.notifyDataSetChanged();
        mAdapter.clear();
        mAdapter.addAll(getList());
    }

    private List<Person> getList() {
        List<Person> personList = new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        final String nameFilter = sharedPrefs.getString(
                getString(R.string.settings_name_key),
                getString(R.string.settings_name_default));

        personList.add(new Person("Name1", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name2", new Date(), "Low", false, BigDecimal.valueOf(765)));
        personList.add(new Person("Name3", new Date(), "High", true, BigDecimal.valueOf(3232.2)));
        personList.add(new Person("Name4", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Gigel", new Date(), "Low", true, BigDecimal.valueOf(32)));
        personList.add(new Person("Name6", new Date(), "Low", true, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name7", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name8", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name9", new Date(), "Low", true, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name10", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name1", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name2", new Date(), "Low", false, BigDecimal.valueOf(765)));
        personList.add(new Person("Name3", new Date(), "High", true, BigDecimal.valueOf(3232.2)));
        personList.add(new Person("Name4", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name5", new Date(), "Low", true, BigDecimal.valueOf(32)));
        personList.add(new Person("Name6", new Date(), "Low", true, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name7", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name8", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name9", new Date(), "Low", true, BigDecimal.valueOf(1222)));
        personList.add(new Person("Dorel", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name1", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name2", new Date(), "Low", false, BigDecimal.valueOf(765)));
        personList.add(new Person("Name3", new Date(), "High", true, BigDecimal.valueOf(3232.2)));
        personList.add(new Person("Name4", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name5", new Date(), "Low", true, BigDecimal.valueOf(32)));
        personList.add(new Person("Name6", new Date(), "Low", true, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name7", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name8", new Date(), "Low", false, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name9", new Date(), "Low", true, BigDecimal.valueOf(1222)));
        personList.add(new Person("Name10", new Date(), "Low", false, BigDecimal.valueOf(1222)));

        personList = personList.stream().filter(p -> p.getName().contains(nameFilter)).collect(Collectors.toList());

        return personList;
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
