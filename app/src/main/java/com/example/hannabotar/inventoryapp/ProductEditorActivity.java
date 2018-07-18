package com.example.hannabotar.inventoryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hannabotar.inventoryapp.model.Product;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductEditorActivity extends AppCompatActivity {

    private int mProductUid;
    private Product mProduct;

    @BindView(R.id.edit_product_name)
    EditText productName;
    @BindView(R.id.edit_product_price)
    EditText productPrice;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_editor);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mProductUid = intent.getIntExtra("PRODUCT_UID", 0);

        if (mProductUid != 0) {
            setTitle(R.string.title_edit_product);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mProduct = App.get().getDB().productDao().findByUid(mProductUid);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productName.setText(mProduct.getName());
                            productPrice.setText(mProduct.getPrice().toString());
                        }
                    });
                }
            }).start();
        } else {
            setTitle(R.string.title_new_product);
            invalidateOptionsMenu();
        }

        productName.setOnTouchListener(mTouchListener);
        productPrice.setOnTouchListener(mTouchListener);
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
        if (mProductUid == 0) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(ProductEditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ProductEditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_product_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveProduct() {
        String nameString = productName.getText().toString().trim();
        String priceString = productPrice.getText().toString();

        // Check if this is supposed to be a new item
        // and check if all the fields in the editor are blank
        if (mProductUid == 0 &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString)) {
            return;
        }

        DecimalFormat df = new DecimalFormat();
        df.setParseBigDecimal(true);
        BigDecimal price = (BigDecimal) df.parse(priceString, new ParsePosition(0));

        if (mProductUid == 0) {
            // insert product
            final Product product = new Product();
            product.setName(nameString);
            product.setPrice(price);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    App.get().getDB().productDao().insert(product);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }).start();
        } else {
            // update product
            mProduct.setName(nameString);
            mProduct.setPrice(price);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    App.get().getDB().productDao().update(mProduct);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }).start();
        }
    }

    private void deleteProduct() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.get().getDB().productDao().delete(mProduct);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
    }
}
