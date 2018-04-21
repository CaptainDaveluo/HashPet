package com.debla.hashpet.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.debla.hashpet.Model.UserImgs;
import com.debla.hashpet.Model.UserInfo;
import com.debla.hashpet.R;
import com.debla.hashpet.adapters.PetCircleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dave-PC on 2018/3/26.
 */

public class PetCircleActivity extends Activity{
    private ListView mListView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_circle);
        mListView = (ListView) findViewById(R.id.lv_pet_circle);
        mListView.addHeaderView(getheadView());
        setData();
    }

    private void setData() {
        List<UserInfo> mList = new ArrayList<UserInfo>();
        UserInfo mUserInfo = new UserInfo();
        UserImgs m = new UserImgs();
        m.setUrls("http://localhost:8080/getImage?urlId=1517205400845");
        mUserInfo.getUi().add(m);
        mList.add(mUserInfo);
        //---------------------------------------------
        UserInfo mUserInfo2 = new UserInfo();
        UserImgs m2 = new UserImgs();
        m2.setUrls("http://localhost:8080/getImage?urlId=1521949847950");
        mUserInfo2.getUi().add(m2);
        UserImgs m21 = new UserImgs();
        m21.setUrls("http://m1.img.srcdd.com/farm2/d/2011/0817/01/5A461954F44D8DC67A17838AA356FE4B_S64_64_64.JPEG");
        mUserInfo2.getUi().add(m21);
        mList.add(mUserInfo2);

        PetCircleAdapter mWeChatAdapter = new PetCircleAdapter(this);
        mWeChatAdapter.setData(mList);
        mListView.setAdapter(mWeChatAdapter);
    }

    private View getheadView() {
        View view = LayoutInflater.from(PetCircleActivity.this).inflate(
                R.layout.pet_circle_head, null);
        return view;
    }
}
