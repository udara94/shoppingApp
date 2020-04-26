package com.shopping.item.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.item.R;
import com.shopping.item.common.CommonUtils;
import com.shopping.item.common.constants.ApplicationConstants;
import com.shopping.item.model.dto.Item;
import com.shopping.item.ui.activities.MainActivity;
import com.shopping.item.ui.adapters.CartListAdapter;
import com.shopping.item.ui.adapters.ItemListAdapter;
import com.shopping.item.utils.BaseBackPressedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = CartFragment.this.getClass().getSimpleName();

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    public static String getTAG() {
        return "CartFragment";
    }

    public static CartFragment cartFragment;

    private CartListAdapter cartListAdapter;
    private List<Item> mItemList = new ArrayList<>();

    RecyclerView mRecyclerView;


    @BindView(R.id.txt_total) TextView txtTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_cart, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            cartFragment = this;
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            ((MainActivity) getActivity()).setOnBackPressedListener(this);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: " + e.toString());
        }
        return rootView;
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    protected void setUpUI() {

        mItemList = ((MainActivity) getActivity()).getCartList();
        if(mItemList != null && mItemList.size() > 0){
            initRecyclerViwe();
            System.out.println("==============>>"+ mItemList);
            cartListAdapter.updateData(mItemList);

        }
        txtTotal.setText("Total $"+getTotalPrice());
    }


    private String getTotalPrice(){

        Double total  = 0.0;
        for (Item item: mItemList) {
        String price = item.getItemPrice();
        total = total + Double.parseDouble(price);
        }
        return total.toString();
    }

    private  void initRecyclerViwe(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        cartListAdapter = new CartListAdapter(getActivity(), new ArrayList<Item>());
        mRecyclerView.setAdapter(cartListAdapter);
    }
    @Override
    protected void setUpToolBar() {
        View mCustomView = getLayoutInflater().inflate(R.layout.custom_action_bar_with_back, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        mToolBar.addView(mCustomView);
        mCustomView.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        title.setTypeface(CommonUtils.getInstance().getFont(getActivity(), ApplicationConstants.FONT_ROBOTO_BOLD));
        title.setText("Cart");
        Toolbar parent =(Toolbar) mCustomView.getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
    }

    @Override
    public void doBack() {

    }
}
