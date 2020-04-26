package com.shopping.item.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.shopping.item.BaseApplication;
import com.shopping.item.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shopping.item.model.dto.Item;
import com.shopping.item.ui.activities.MainActivity;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemPickBottomSheet extends BottomSheetDialogFragment {


    final String TAG = ItemPickBottomSheet.this.getClass().getSimpleName();
    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";

    public static String getTAG() {
        return "ItemPickBottomSheet";
    }


    public ItemPickBottomSheet() { }

    public static ItemPickBottomSheet itemPickBottomSheet = null;

    private  Item mItem;
    private String[] mTypes = {"Black", "White", "Gold"};

    public static ItemPickBottomSheet newInstance(Item item) {
        ItemPickBottomSheet fragment = new ItemPickBottomSheet();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.btn_select) Button btnSelect;
    @BindView(R.id.chip_group) ChipGroup chipGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if (getArguments() != null) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet_item_pick, container, false);
        ButterKnife.bind(this, rootView);
        itemPickBottomSheet = this;
        setUpUI();
        return rootView;
    }

    private void populateItemTypes() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (String text : mTypes) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
            chip.setText(text);
//            if (isSubcribeJob(text.getJobType(), subsJobs)) {
//                //chip.setBackgroundColor(getResources().getColor(R.color.colorOrange));
//                chip.setSelected(true);
//                chip.setChecked(true);
//            }

            chipGroup.addView(chip);

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                    btnSave.setVisibility(View.VISIBLE);
//                    if(isChecked){
//                        chip.setSelected(true);
//                        chip.setChecked(true);
//                    }else {
//                        chip.setSelected(false);
//                        chip.setChecked(false);
//                    }
                }
            });

        }
    }

    private void setUpUI() {
        if(mItem != null){
            populateItemTypes();

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).addToCart(mItem);
                    dismiss();
                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_LONG);

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        itemPickBottomSheet = null;
        super.onDestroyView();
        BaseApplication.getBaseApplication().setLoadBottomSheet(false);
    }

}
