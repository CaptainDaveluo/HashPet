package com.debla.hashpet.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.debla.hashpet.R;
import com.debla.hashpet.activities.ShowGoodsResultActivity;

import static com.debla.hashpet.R.id.iv_bigdog;

/**
 * Created by Dave-PC on 2017/10/25.
 */

public class MarketFragment extends Fragment implements View.OnClickListener{
    private View mRootView;
    private ImageView mBigDog;
    private ImageView mDogToys;
    private ImageView mDaily;
    private ImageView mClean;
    private ImageView mBeauty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.market_fragment, container, false);
        mBigDog = (ImageView) mRootView.findViewById(iv_bigdog);
        mDogToys = (ImageView) mRootView.findViewById(R.id.iv_dogtoys);
        mDaily = (ImageView) mRootView.findViewById(R.id.iv_outdaily);
        mClean = (ImageView) mRootView.findViewById(R.id.iv_clean);
        mBeauty = (ImageView) mRootView.findViewById(R.id.iv_beauty);
        mBigDog.setOnClickListener(this);
        mDogToys.setOnClickListener(this);
        mDaily.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mBeauty.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(),ShowGoodsResultActivity.class);
        Bundle bd = new Bundle();
        switch (v.getId()){
            case R.id.iv_bigdog:
                bd.putString("proTag","dog");
                break;
            case R.id.iv_dogtoys:
                bd.putString("proTag","toy");
                break;
            case R.id.iv_outdaily:
                bd.putString("proTag","daily");
                break;
            case R.id.iv_clean:
                bd.putString("proTag","clean");
                break;
            case R.id.iv_beauty:
                bd.putString("proTag","clothes");
                break;
        }
        intent.putExtra("params",bd);
        startActivity(intent);
    }
}
