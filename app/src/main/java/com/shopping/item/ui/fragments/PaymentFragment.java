package com.shopping.item.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shopping.item.BaseApplication;
import com.shopping.item.R;
import com.shopping.item.common.CommonUtils;
import com.shopping.item.common.constants.ApplicationConstants;
import com.shopping.item.ui.activities.MainActivity;
import com.shopping.item.utils.BaseBackPressedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentFragment extends BaseFragment implements BaseBackPressedListener.OnBackPressedListener {

    final String TAG = PaymentFragment.this.getClass().getSimpleName();

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        return fragment;
    }

    public static String getTAG() {
        return "PaymentFragment";
    }

    public static PaymentFragment paymentFragment;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;


    @BindView(R.id.btn_next) Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_payment, container, false);
            Log.d(TAG, "onCreateView");
            ButterKnife.bind(this, rootView);
            paymentFragment = this;
            viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
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
        layouts = new int[]{
                R.layout.fragment_shipping_details,
                R.layout.fragment_shipping_address,
                R.layout.fragment_pay,
                R.layout.fragment_payment_success};

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }else {
                    getFragmentManager().popBackStack();
                    getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).clearCart();
                }

            }
        });

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Done");
            }
            else if (position == layouts.length -2){
               btnNext.setText("Pay Now");
            }
            else {
                // still pages are left
                btnNext.setText("Next");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

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
        title.setText("Shipping Details");
        Toolbar parent =(Toolbar) mCustomView.getParent();
        parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
    }

    @Override
    public void doBack() {

    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        paymentFragment = null;
        BaseApplication.getBaseApplication().setLoadPaymentScreen(false);
    }
}
