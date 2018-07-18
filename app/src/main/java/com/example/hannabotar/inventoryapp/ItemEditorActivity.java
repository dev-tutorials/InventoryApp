package com.example.hannabotar.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hannabotar.inventoryapp.data.ItemContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.edit_item_name)
    EditText itemName;
    @BindView(R.id.edit_serial_no)
    EditText serialNo;
    @BindView(R.id.spinner_condition)
    Spinner conditionSpinner;
    @BindView(R.id.edit_description)
    EditText description;

    private static final int DB_ITEM_LOADER_ID = 1;

    /**
     * Condition of the item. The possible values are:
     * 0 for unknown, 1 for new, 2 for used, 3 for defect.
     */
    private int mCondition = 0;

    /**
     * Set for edit screen, null for add screen
     */
    private Uri mCurrentItemUri;

    private boolean mItemHasChanged = false;

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mItemHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    private static final String[] PROJECTION = {
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.COLUMN_NAME,
            ItemContract.ItemEntry.COLUMN_SERIAL_NO,
            ItemContract.ItemEntry.COLUMN_CONDITION,
            ItemContract.ItemEntry.COLUMN_DESCRIPTION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        ButterKnife.bind(this);
        
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();
        if (mCurrentItemUri != null) {
            setTitle(R.string.title_edit_inventory_item);
            getLoaderManager().initLoader(DB_ITEM_LOADER_ID, null, this);
        } else {
            setTitle(R.string.title_new_inventory_item);

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        }
        
        setupConditionSpinner();

        itemName.setOnTouchListener(mTouchListener);
        serialNo.setOnTouchListener(mTouchListener);
        conditionSpinner.setOnTouchListener(mTouchListener);
        description.setOnTouchListener(mTouchListener);
    }

    private void setupConditionSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter conditionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_condition_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        conditionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        conditionSpinner.setAdapter(conditionSpinnerAdapter);

        // Set the integer mSelected to the constant values
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.condition_unknown))) {
                        mCondition = ItemContract.ItemEntry.CONDITION_UNKNOWN; // Unknown
                    } else if (selection.equals(getString(R.string.condition_new))) {
                        mCondition = ItemContract.ItemEntry.CONDITION_NEW; // New
                    } else if (selection.equals(getString(R.string.condition_used))) {
                        mCondition = ItemContract.ItemEntry.CONDITION_USED; // Used
                    } else {
                        mCondition = ItemContract.ItemEntry.CONDITION_DEFECT; // Defect
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCondition = 0; // Unknown
            }
        });
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_item_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_item_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save item to DB
                saveItem();
                // close the editor activity and go back to the initial one
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Show delete confirmation
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the item hasn't changed, continue with navigating up to parent activity
                // which is the {@link InventoryActivity}.
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(ItemEditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ItemEditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        String nameString = itemName.getText().toString().trim();
        String serialNoString = serialNo.getText().toString().trim();
        String descriptionString = description.getText().toString().trim();

        // Check if this is supposed to be a new item
        // and check if all the fields in the editor are blank
        if (mCurrentItemUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(serialNoString) &&
                TextUtils.isEmpty(descriptionString) && mCondition == ItemContract.ItemEntry.CONDITION_UNKNOWN) {
            // Since no fields were modified, we can return early without creating a new item.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        ContentValues item = new ContentValues();
        item.put(ItemContract.ItemEntry.COLUMN_NAME, nameString);
        item.put(ItemContract.ItemEntry.COLUMN_SERIAL_NO, serialNoString);
        item.put(ItemContract.ItemEntry.COLUMN_CONDITION, mCondition);
        item.put(ItemContract.ItemEntry.COLUMN_DESCRIPTION, descriptionString);

        if (mCurrentItemUri == null) {
            // insert item
            Uri uri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, item);
            if (uri == null) {
                Toast.makeText(this, getString(R.string.errorSave), Toast.LENGTH_SHORT).show();
            } else {
                long savedId = ContentUris.parseId(uri);
                Toast.makeText(this, getString(R.string.successSave, savedId), Toast.LENGTH_SHORT).show();
            }
        } else {
            // update item
            long updatedRows = getContentResolver().update(mCurrentItemUri, item, null, null);
            if (updatedRows == 0) {
                Toast.makeText(this, getString(R.string.errorUpdate), Toast.LENGTH_SHORT).show();
            } else {
                long updatedId = ContentUris.parseId(mCurrentItemUri);
                Toast.makeText(this, getString(R.string.successUpdate), Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteItem() {
        if (mCurrentItemUri == null) {
            return;
        } else {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.errorDelete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successDelete), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mCurrentItemUri != null) {
            return new CursorLoader(this, mCurrentItemUri, PROJECTION, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME));
            itemName.setText(name);

            String serial = data.getString(data.getColumnIndex(ItemContract.ItemEntry.COLUMN_SERIAL_NO));
            serialNo.setText(serial);

            int condition = data.getInt(data.getColumnIndex(ItemContract.ItemEntry.COLUMN_CONDITION));
            mCondition = condition;
            switch (condition) {
                case ItemContract.ItemEntry.CONDITION_NEW:
                    conditionSpinner.setSelection(1);
                    break;
                case ItemContract.ItemEntry.CONDITION_USED:
                    conditionSpinner.setSelection(2);
                    break;
                case ItemContract.ItemEntry.CONDITION_DEFECT:
                    conditionSpinner.setSelection(3);
                    break;
                default:
                    conditionSpinner.setSelection(0);
                    break;
            }

            String desc = data.getString(data.getColumnIndex(ItemContract.ItemEntry.COLUMN_DESCRIPTION));
            description.setText(desc);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        itemName.setText("");
        serialNo.setText("");
        conditionSpinner.setSelection(0); // Select "Unknown" condtion
        description.setText("");
    }

}
