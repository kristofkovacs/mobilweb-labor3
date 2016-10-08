package hu.bme.aut.shoppinglist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by kristofkovacs-egri on 2016. 10. 08..
 */

public class NewShoppingItemDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = "NewShoppingItemDialogFragment";

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText estimatedPriceEditText;
    private Spinner categorySpinner;
    private CheckBox alreadyPurchasedCheckBox;

    public interface INewShoppingItemDialogListener {
        void onShoppingItemCreated(ShoppingItem newItem);
    }

    private INewShoppingItemDialogListener listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();

        if (activity instanceof INewShoppingItemDialogListener) {
            listener = (INewShoppingItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the INewShoppingItemDialogListener interface!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext()).setTitle(R.string.new_shopping_item)
                .setView(getContentView()).setPositiveButton(R.string.ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isValid()) {
                                    listener.onShoppingItemCreated(getShoppingItem());
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel, null).create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_shopping_item, null);
        nameEditText = (EditText) contentView.findViewById(R.id.ShoppingItemNameEditText);
        descriptionEditText = (EditText) contentView.findViewById(R.id.ShoppingItemDescriptionEditText);
        estimatedPriceEditText = (EditText) contentView.findViewById(R.id.ShoppingItemEstimatedPriceEditText);

        categorySpinner = (Spinner) contentView.findViewById(R.id.ShoppingItemCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        alreadyPurchasedCheckBox = (CheckBox) contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox);

        return contentView;
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private ShoppingItem getShoppingItem() {
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.name = nameEditText.getText().toString();
        shoppingItem.description = descriptionEditText.getText().toString();

        try {
            shoppingItem.estimatedPrice = Integer.parseInt(estimatedPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            shoppingItem.estimatedPrice = 0;
        }

        shoppingItem.category = ShoppingItem.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
        shoppingItem.isBought = alreadyPurchasedCheckBox.isChecked();
        shoppingItem.save();

        return shoppingItem;
    }
}