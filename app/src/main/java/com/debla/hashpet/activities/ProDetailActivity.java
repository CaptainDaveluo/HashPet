package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.debla.hashpet.Model.ProductSellDetail;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.HttpUtil;
import com.debla.hashpet.Utils.ImageLoader;
import com.debla.hashpet.Utils.ShopCarUtil;

/**
 * Created by Dave-PC on 2018/2/3.
 */

public class ProDetailActivity extends Activity{

    private ImageView mView;
    private ProductSellDetail mDetail;

    private TextView tv_name;
    private TextView tv_brief;
    private TextView tv_price;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_detail);
        init();
    }

    public void init(){
        mView = (ImageView) findViewById(R.id.detail_img);
        tv_name = (TextView) findViewById(R.id.detail_title);
        tv_brief = (TextView) findViewById(R.id.detail_brief);
        tv_price = (TextView) findViewById(R.id.detail_price);
        Intent intent = getIntent();
        Bundle bd = intent.getBundleExtra("data");
        ProductSellDetail detail = (ProductSellDetail) bd.getSerializable("product");
        mDetail = detail;
        tv_name.setText(detail.getProName());
        tv_price.setText("￥"+detail.getProPrice());
        tv_brief.setText(detail.getProBrief());
        ImageLoader loader = new ImageLoader(getApplicationContext());
        String url = HttpUtil.getUrl(getApplicationContext())+"/getImage?urlId="+detail.getProSrc();
        loader.DisplayImage(url,mView);
    }


    public void addToShopCar(View v){
        //点击添加到购物车
        //先获取当前商品信息
        if(mDetail==null){
            Toast.makeText(ProDetailActivity.this,"获取商品信息失败",Toast.LENGTH_SHORT).show();
            return;
        }
        String goodsId = String.valueOf(mDetail.getProId());
        ShopCarUtil.addToShopCar(getApplicationContext(),this.mDetail,1);
        Toast.makeText(ProDetailActivity.this,"商品添加成功!",Toast.LENGTH_SHORT).show();
    }

    public void showPic(View v){
        Intent intent = new Intent(this,ShowImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("image",mDetail.getProSrc());
        bundle.putString("title",mDetail.getProName());
        intent.putExtra("data",bundle);
        startActivity(intent);
    }
}
