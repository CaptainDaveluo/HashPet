package com.debla.hashpet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.debla.hashpet.R;
import com.debla.hashpet.activities.ServiceStoresActivity;

/**
 * Created by Dave-PC on 2017/10/27.
 */

public class ServiceFragment extends Fragment {
    private View mRootView;

    private LinearLayout mServiceClean;
    private LinearLayout mServiceBeauty;
    private LinearLayout mServiceLive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mRootView != null)
            return mRootView;
        mRootView = inflater.inflate(R.layout.service_fragment,container,false);
        mServiceClean = (LinearLayout) mRootView.findViewById(R.id.service_layout_clean);
        mServiceBeauty = (LinearLayout) mRootView.findViewById(R.id.service_layout_beauty);
        mServiceLive = (LinearLayout) mRootView.findViewById(R.id.service_layout_live);
        mServiceClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ServiceStoresActivity.class);
                startActivity(intent);
            }
        });
        mServiceBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ServiceStoresActivity.class);
                startActivity(intent);
            }
        });
        mServiceLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ServiceStoresActivity.class);
                startActivity(intent);
            }
        });
        return mRootView;
    }

}
