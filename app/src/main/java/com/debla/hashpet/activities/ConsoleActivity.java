package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.debla.hashpet.R;

/**
 * Created by Dave-PC on 2017/12/17.
 */

public class ConsoleActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_console);
    }


    public void onSellerProList(View v){
        Intent intent = new Intent(getApplication(),SellerProListActivity.class);
        startActivity(intent);
    }

    public void onSellerStoreManage(View v){
        Intent intent = new Intent(getApplication(),StoreManageActivity.class);
        startActivity(intent);
    }

    public  void onSellerOrderManage(View v){
        Intent intent = new Intent(getApplication(),OrderManageActiity.class);
        startActivity(intent);
    }
}
