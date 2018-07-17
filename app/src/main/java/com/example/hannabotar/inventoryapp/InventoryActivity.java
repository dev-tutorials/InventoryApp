package com.example.hannabotar.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hannabotar.inventoryapp.adapter.ItemCursorAdapter;
import com.example.hannabotar.inventoryapp.api.ItemsLoader;
import com.example.hannabotar.inventoryapp.api.ItemsUtil;
import com.example.hannabotar.inventoryapp.data.ItemContract;
import com.example.hannabotar.inventoryapp.model.InventoryItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DB_ITEM_LOADER_ID = 1;
    private static final int API_ITEM_LOADER_ID = 2;

    private static final String[] PROJECTION = {
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.COLUMN_NAME,
            ItemContract.ItemEntry.COLUMN_SERIAL_NO
    };

    private static final String[] PROJECTION_ALL = {
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.COLUMN_NAME,
            ItemContract.ItemEntry.COLUMN_SERIAL_NO,
            ItemContract.ItemEntry.COLUMN_CONDITION,
            ItemContract.ItemEntry.COLUMN_DESCRIPTION
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

    /*API methods*/
    private LoaderManager.LoaderCallbacks<List<InventoryItem>> mApiLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<InventoryItem>>() {
        @Override
        public Loader<List<InventoryItem>> onCreateLoader(int id, Bundle args) {
            return new ItemsLoader(InventoryActivity.this, "http://185.109.255.43/index.php");
        }

        @Override
        public void onLoadFinished(Loader<List<InventoryItem>> loader, List<InventoryItem> data) {
            // stop loader
            getLoaderManager().destroyLoader(API_ITEM_LOADER_ID);
            // save to DB and reload
            deleteAndSaveAll(data);
            reloadDataFromDB();
        }

        @Override
        public void onLoaderReset(Loader<List<InventoryItem>> arg0) {
            reloadDataFromDB();
        }
    };
    /*End API methods*/


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
                getLoaderManager().initLoader(API_ITEM_LOADER_ID, null, mApiLoaderCallbacks);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(ItemContract.ItemEntry.CONTENT_URI, PROJECTION_ALL, null, null, null);
                List<InventoryItem> list = new ArrayList<InventoryItem>();
                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    // The Cursor is now set to the right position
                    list.add(new InventoryItem(
                            cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_SERIAL_NO)),
                            cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CONDITION)),
                            cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_DESCRIPTION))
                            ));
                }
                new AsyncHttpPost().execute(list);
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

    private void reloadDataFromDB() {
        getLoaderManager().restartLoader(DB_ITEM_LOADER_ID, null, this);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void deleteAndSaveAll(List<InventoryItem> items) {
        getContentResolver().delete(ItemContract.ItemEntry.CONTENT_URI, null, null);
        for (InventoryItem item : items) {
            ContentValues cv = new ContentValues();
            cv.put(ItemContract.ItemEntry.COLUMN_NAME, item.getName());
            cv.put(ItemContract.ItemEntry.COLUMN_SERIAL_NO, item.getSerialNo());
            cv.put(ItemContract.ItemEntry.COLUMN_CONDITION, item.getCondition());
            cv.put(ItemContract.ItemEntry.COLUMN_DESCRIPTION, item.getDescription());
            getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, cv);
        }
    }

    public class AsyncHttpPost extends AsyncTask<List<InventoryItem>, Void, String> {
        @Override
        protected String doInBackground(List<InventoryItem>[] lists) {
            String result = ItemsUtil.postItems("http://185.109.255.43/post.php", lists[0]);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Activity 1 GUI stuff
            Toast.makeText(InventoryActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
