package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.debla.hashpet.R;
import com.debla.hashpet.Utils.HttpUtil;
import com.debla.hashpet.Utils.ImageLoader;
import com.debla.hashpet.Utils.ZoomImageView;

/**
 * Created by Dave-PC on 2018/2/8.
 */

public class ShowImageActivity extends Activity{
    private TextView mTitle;
    private ZoomImageView mImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pic_activity);
        mImage = (ZoomImageView) findViewById(R.id.show_pic_img);
        mImage.placeholder(R.drawable.img_error);
        mTitle = (TextView) findViewById(R.id.showpic_text);
        mTitle.setBackgroundColor(Color.argb(127,0,0,0));
        mTitle.setTextColor(Color.argb(255, 255, 255, 255));
        showImage();
    }

    public void showImage(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String imageUrl = bundle.getString("image");
        String title = bundle.getString("title");
        mTitle.setText(title);
        ImageLoader loader = new ImageLoader(getApplicationContext());
        String url = HttpUtil.getUrl(getApplicationContext())+"/getImage?urlId="+imageUrl;
        loader.DisplayImage(url,mImage);
    }

    public void onImageClick(View v){
        finish();
    }
}
