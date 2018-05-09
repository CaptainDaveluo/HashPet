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
import android.widget.ListView;
import android.widget.TextView;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.Facilitator;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2018/5/4.
 */

public class ServiceStoresActivity extends Activity{

    @BindView(R.id.lv_near_stores)
    ListView lvStores;

    private List<Facilitator> stores;
    private StoreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearstores);
        ButterKnife.bind(this);
        initData();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1){
                lvStores.setAdapter(adapter);
            }
            return false;
        }
    });


    private void initData(){
        String url = HttpUtil.getUrl(getApplicationContext());
        HttpUtil client = new HttpUtil();
        client.postRequest(url + "showAllStores", new HashMap<String, String>(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("debug","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                Type jsonType = new TypeToken<BaseJsonObject<List<Facilitator>>>(){}.getType();
                BaseJsonObject<List<Facilitator>> jsonObject = null;
                jsonObject =  gson.fromJson(json, jsonType);
                stores = jsonObject.getResult();
                adapter = new StoreAdapter();
                Message msg = new Message();
                msg.what=1;
                handler.sendMessage(msg);

            }
        });
    }

    public class StoreAdapter extends BaseAdapter {

        //public ImageLoader imageLoader; //用来下载图片的类
        private String url;

        public StoreAdapter(){
            //imageLoader = new ImageLoader(getApplicationContext());
            //url = imgUrl;
        }


        @Override
        public int getCount() {
            return stores.size();
        }

        @Override
        public Object getItem(int position) {
            if(position==-1)
                position=0;
            return stores.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(position==-1)
                position=0;
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v;
            if(convertView==null){
                v = View.inflate(ServiceStoresActivity.this,R.layout.item_store_info,null);
            }else{
                v=convertView;
            }
            TextView tvStoreName = (TextView) v.findViewById(R.id.store_name);
            TextView tvStoreAddress = (TextView) v.findViewById(R.id.store_address);
            TextView tvStoreBrief = (TextView) v.findViewById(R.id.store_brief);
            tvStoreName.setText(stores.get(position).getShopName());
            tvStoreAddress.setText(stores.get(position).getAddress());
            tvStoreBrief.setText(stores.get(position).getShopBrief());
            return v;
        }

    }
}
