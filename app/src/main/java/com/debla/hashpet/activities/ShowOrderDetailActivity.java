package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.OrderItem;
import com.debla.hashpet.Model.ProductSellDetail;
import com.debla.hashpet.Model.SellerInfo;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.HttpUtil;
import com.debla.hashpet.Utils.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2018/5/2.
 */

public class ShowOrderDetailActivity extends Activity{
    @BindView(R.id.lv_order_item)
    ListView mOrderItemView;

    @BindView(R.id.order_detail_info)
    TextView mDetailInfo;

    private List<OrderItem> items;
    private List<ProductSellDetail> goods;
    private String address;
    private String ordNum;
    private ProAdapter proAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        init();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String imgUrl=HttpUtil.getUrl(getApplicationContext())+"getImage";
            proAdapter = new ProAdapter(imgUrl);
            mOrderItemView.setAdapter(proAdapter);
            return false;
        }
    });

    public void init(){
        Intent intent = getIntent();
        try {
            items = (List<OrderItem>) intent.getSerializableExtra("items");
            address = intent.getStringExtra("address");
            ordNum = intent.getStringExtra("ordNum");
            mDetailInfo.setText(address);
            Map params = new HashMap();
            params.put("orderNum",ordNum);
            String url=HttpUtil.getUrl(getApplicationContext());
            HttpUtil client = new HttpUtil();
            client.postRequest(url + "getOrderGoods", params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("debug","请求失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String JSON = response.body().string();
                    try {
                        Log.e("debug",JSON);
                        Gson gson =new Gson();
                        Type jsonType = new TypeToken<BaseJsonObject<List<ProductSellDetail>>>() {
                        }.getType();
                        BaseJsonObject<SellerInfo> jsonObject = null;
                        jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
                        goods=(List<ProductSellDetail>) jsonObject.getResult();
                        Message msg = new Message();
                        handler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * ListView适配器
     */
    public class ProAdapter extends BaseAdapter {
        public ImageLoader imageLoader; //用来下载图片的类
        private String url;

        public ProAdapter(String imgUrl){
            imageLoader = new ImageLoader(getApplicationContext());
            url = imgUrl;
        }

        @Override
        public int getCount() {
            return goods.size();
        }

        @Override
        public Object getItem(int position) {
            if(position==-1)
                position=0;
            return goods.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(position==-1)
                position=0;
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if(convertView==null){
                v=View.inflate(ShowOrderDetailActivity.this, R.layout.lv_product, null);
            }else{
                v=convertView;
            }
            TextView tvPrdName = (TextView) v.findViewById(R.id.lv_prd_title);
            tvPrdName.setText(goods.get(position).getProName());
            TextView tvPrice = (TextView) v.findViewById(R.id.lv_prd_price);
            tvPrice.setText("￥"+goods.get(position).getProPrice());
            TextView tvBrief = (TextView) v.findViewById(R.id.lv_prd_brief);
            tvBrief.setText(goods.get(position).getProBrief());
            ImageView prodImage = (ImageView) v.findViewById(R.id.lv_prd_img);
            imageLoader.DisplayImage(url+"?urlId="+goods.get(position).getProSrc(),prodImage);
            return v;
        }
    }
}
