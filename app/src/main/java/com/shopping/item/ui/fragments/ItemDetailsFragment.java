package com.shopping.item.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shopping.item.BaseApplication;
import com.shopping.item.R;
import com.shopping.item.model.dto.Item;
import com.shopping.item.ui.activities.MainActivity;
import com.shopping.item.utils.BaseBackPressedListener;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailsFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = ItemDetailsFragment.this.getClass().getSimpleName();

    private static String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private static String POSITION = "POSITION";

    public static String getTAG() {
        return "ItemDetailsFragment";
    }


    public static ItemDetailsFragment newInstance(Item item) {
        ItemDetailsFragment fragment = new ItemDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    public static  ItemDetailsFragment itemDetailsFragment;
    private  Item mItem;
    private ImageLoader imageLoader;

    @BindView(R.id.txt_item_name) TextView txtItemName;
    @BindView(R.id.txt_item_code) TextView txtItemCode;
    @BindView(R.id.txt_item_price) TextView txtItemPrice;
    @BindView(R.id.txt_description) TextView txtItemDescription;

    @BindView(R.id.header_image) ImageView mImageView;
    @BindView(R.id.shopping_cart) ImageView mbtnCart;
    @BindView(R.id.image_progress) ProgressBar imageProgress;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.btn_add_to_cart) Button btnAddToCart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItem = Parcels.unwrap(getArguments().getParcelable(BUNDLE_EXTRA));
            this.imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_item_details, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            itemDetailsFragment = this;
            collapsingToolbarLayout.setTitle(" ");
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
        if (mItem != null) {

            mImageView.setImageResource(R.drawable.icon);
            if (mItem.getItemImg() != null && !mItem.getItemImg().isEmpty()) {
                ImageLoader.getInstance().displayImage(mItem.getItemImg(), mImageView, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        if (imageProgress != null)
                            imageProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (imageProgress != null)
                            imageProgress.setVisibility(View.GONE);
                        if (mItem.getItemImg().isEmpty())
                            mImageView.setImageResource(R.drawable.icon); // clear imageview for no images
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (imageProgress != null)
                            imageProgress.setVisibility(View.GONE);
                    }
                });
            } else {
                if (imageProgress != null)
                    imageProgress.setVisibility(View.GONE);
                if (mImageView != null && mItem.getItemImg() != null && mItem.getItemImg().isEmpty())
                    mImageView.setImageResource(R.drawable.icon); // clear imageview for no images
            }

            if (mItem.getItemName() != null && !mItem.getItemName().isEmpty()) {
                txtItemName.setText(mItem.getItemName());
            }

            if (mItem.getItemCode() != null && !mItem.getItemCode().isEmpty())
                txtItemCode.setText(mItem.getItemCode());

            if (mItem.getItemDescription() != null && !mItem.getItemDescription().isEmpty()) {
                txtItemDescription.setText(mItem.getItemDescription());
            }
            if (mItem.getItemPrice() != null && !mItem.getItemPrice().isEmpty()) {
                txtItemPrice.setText(mItem.getItemPrice());
            }

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        ((MainActivity) getActivity()).addToCart(mItem);
                }
            });

            mbtnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    gotoCartFragment();
                }
            });
        }
    }

    private void gotoCartFragment(){
        ((MainActivity) getActivity()).addFragment(new CartFragment().newInstance(), CartFragment.getTAG());
    }



    @Override
    protected void setUpToolBar() {
        mToolBar.setNavigationIcon(R.drawable.back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void doBack() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        itemDetailsFragment = null;
        BaseApplication.getBaseApplication().setLoadItemDetailsScreen(false);
    }
}
