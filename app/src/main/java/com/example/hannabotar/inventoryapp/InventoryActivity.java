package com.example.hannabotar.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hannabotar.inventoryapp.adapter.ItemCursorAdapter;
import com.example.hannabotar.inventoryapp.data.ItemContract;
import com.example.hannabotar.inventoryapp.services.GetItemsService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_ITEM_LOADER_ID = 1;

    private static final String[] PROJECTION = {
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.COLUMN_NAME,
            ItemContract.ItemEntry.COLUMN_SERIAL_NO
    };

    private ItemCursorAdapter mAdapter;

    @BindView(R.id.load_from_api)
    Button loadButton;
    @BindView(R.id.post_to_api)
    Button postButton;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        ButterKnife.bind(this);

        // Setup FAB to open EditorActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, ItemEditorActivity.class);
                startActivity(intent);
            }
        });

        // Setup cursor adapter
        mAdapter = new ItemCursorAdapter(this, null);
        // Attach cursor adapter to the ListView
        listView.setAdapter(mAdapter);

        // Set empty view on the ListView, so that it only shows when the list has 0 items.
        listView.setEmptyView(emptyView);

        // Setup item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mAdapter.getItem(position);
                Intent edit = new Intent(InventoryActivity.this, ItemEditorActivity.class);
                edit.setData(ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, id));
                startActivity(edit);
            }
        });

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(DB_ITEM_LOADER_ID, null, this);

        setupButtons();
    }

    private void setupButtons() {
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(InventoryActivity.this, GetItemsService.class));
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Called when a new Loader needs to be created
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        progressBar.setVisibility(View.VISIBLE);
        return new CursorLoader(this, ItemContract.ItemEntry.CONTENT_URI, PROJECTION, null, null, null);
    }

    // Called when a previously created loader has finished loading
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
        progressBar.setVisibility(View.GONE);
        if (data.getCount() == 0) {
            emptyView.setText("No items in your local inventory.");
        }
    }

    // Called when a previously created loader is reset, making the data unavailable
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}
