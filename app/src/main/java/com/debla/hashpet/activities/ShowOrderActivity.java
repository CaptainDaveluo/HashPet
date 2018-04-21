package com.debla.hashpet.activities;

import android.app.Activity;
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
import android.widget.Toast;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.Order;
import com.debla.hashpet.Model.User;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.AppContext;
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
 * Created by Dave-PC on 2018/3/15.
 */

public class ShowOrderActivity extends Activity{

    @BindView(R.id.show_order_list)
    ListView orderListView;

    private AppContext appContext;
    private User user;
    private String url;

    private OrderAdapter adapter;
    public ImageLoader imageLoader; //用来下载图片的类
    private List<Order> orders;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            url = HttpUtil.getUrl(getApplicationContext())+"getGoodsImage";
            orders = (List<Order>) msg.obj;
            if(orders==null||orders.size()==0) {
                Toast.makeText(ShowOrderActivity.this, "您目前没有任何订单，快去下单吧", Toast.LENGTH_SHORT).show();
                return false;
            }
            adapter = new OrderAdapter(url);
            orderListView.setAdapter(adapter);
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 获取订单数据
     */
    public void initData(){
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();
        HttpUtil client = new HttpUtil();
        String url = HttpUtil.getUrl(getApplicationContext());
        Map<String,String> params = new HashMap<>();
        params.put("userId",String.valueOf(user.getUserid()));
        imageLoader = new ImageLoader(getApplicationContext());
        client.postRequest(url + "/showUserOrder", params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("debug","获取订单信息失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                Type jsonType = new TypeToken<BaseJsonObject<List<Order>>>(){}.getType();
                BaseJsonObject<List<Order>> jsonObject = null;
                jsonObject =  gson.fromJson(json, jsonType);
                List<Order> orders = jsonObject.getResult();
                Message msg = new Message();
                msg.obj=orders;
                handler.sendMessage(msg);
            }
        });
    }



    public class OrderAdapter extends BaseAdapter{

        public ImageLoader imageLoader; //用来下载图片的类
        private String url;

        public OrderAdapter(String imgUrl){
            imageLoader = new ImageLoader(getApplicationContext());
            url = imgUrl;
        }


        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            if(position==-1)
                position=0;
            return orders.get(position);
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
                v = View.inflate(ShowOrderActivity.this,R.layout.order_list_item,null);
            }else{
                v=convertView;
            }
            TextView tv_name = (TextView) v.findViewById(R.id.order_item_name);
            TextView tv_num = (TextView) v.findViewById(R.id.order_item_num);
            TextView tv_addr = (TextView) v.findViewById(R.id.order_item_address);
            TextView tv_time = (TextView) v.findViewById(R.id.order_item_time);
            ImageView srcImage = (ImageView) v.findViewById(R.id.show_order_img);
            Integer proId = orders.get(position).getItems().get(0).getOrdProId();
            imageLoader.DisplayImage(url+"?goodsId="+proId,srcImage);
            tv_name.setText(String.valueOf(orders.get(position).getOrdGoodsName()));
            tv_num.setText("单号："+orders.get(position).getOrdNum());
            tv_addr.setText("地址："+orders.get(position).getOrdAddr());
            tv_time.setText("日期："+orders.get(position).getOrdTime());
            return v;
        }
    }
}
