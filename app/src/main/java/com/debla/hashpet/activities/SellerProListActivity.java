package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.Product;
import com.debla.hashpet.Model.SellerInfo;
import com.debla.hashpet.Model.User;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.AppContext;
import com.debla.hashpet.Utils.HttpUtil;
import com.debla.hashpet.Utils.ImageLoader;
import com.debla.hashpet.services.imps.ProService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2017/12/18.
 */

public class SellerProListActivity extends Activity{
    private TextView shopName;
    private AppContext appContext;
    private String url;
    private User user;
    private Gson gson;
    private SellerInfo seller;
    private ListView proList;
    private List<Product> dataList;
    private ProAdapter proAdapter;
    private UpdateListReciver updateListReciver;//更新列表广播接收


    /**
     * 处理UI更新商铺名称
     */
    private Handler  handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            shopName.setText(seller.getShopName());
            initList();
            return false;
        }
    });

    /**
     * 处理ListView获取数据
     */
    private Handler listHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1004){
                dataList = (List<Product>) msg.obj;
                String imgUrl=HttpUtil.getUrl(getApplicationContext())+"getImage";
                proAdapter = new ProAdapter(imgUrl);
                proList.setAdapter(proAdapter);
                proList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_manage);
        shopName = (TextView) findViewById(R.id.tv_pro_shopname);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();
        proList = (ListView) findViewById(R.id.lv_pro_list);
        init();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EditPetActivity.action);
        intentFilter.addAction(EditProdActivity.action);
        if(updateListReciver==null)
            updateListReciver = new UpdateListReciver();
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(updateListReciver,intentFilter);
    }


    public void init(){
        url= HttpUtil.getUrl(getApplication())+"getSellerInfo";
        seller = (SellerInfo)appContext.getInnerMap().get("seller");
        if(seller==null){
            HttpUtil httpUtil = new HttpUtil();
            Map<String,String> params = new HashMap<>();
            params.put("userId",String.valueOf(user.getUserid()));
            httpUtil.postRequest(url, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("debug","请求接口出现异常"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String JSON = response.body().string();
                    Analize(JSON);
                }
            });
        }else{
            shopName.setText(seller.getShopName());
            initList();
        }
    }

    public void Analize(String JSON) {
        gson = new Gson();
        try {
            Log.e("debug",JSON);
            Type jsonType = new TypeToken<BaseJsonObject<SellerInfo>>(){}.getType();
            BaseJsonObject<SellerInfo> jsonObject = null;
            jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
            seller = (SellerInfo) jsonObject.getResult();
            appContext.getInnerMap().put("seller",seller);
            Message msg = new Message();
            msg.obj=seller;
            handler.sendMessage(msg);
        }catch (Exception e){
            Log.e("debug","解析json过程出现异常"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void initList(){
        url=HttpUtil.getUrl(getApplication())+"queryProduct";
        Map params = new HashMap();
        params.put("proShopId",String.valueOf(seller.getShopId()));
        ProService proService = new ProService();
        proService.setUrl(url);
        proService.setHandler(listHandler);
        proService.getProductByCondition(params);
    }

    /**
     * 新建商品或新宠上架
     * @param v
     */
    public void onNewPro(View v){
        Intent intent = new Intent(getApplicationContext(),UploadNewActivity.class);
        startActivity(intent);
    }

    /**
     * 处理产品更新的广播
     */
    class UpdateListReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getBundle("data")!=null){
                Product product = (Product) intent.getExtras().getBundle("data").getSerializable("proInfo");
                dataList.add(product);
                //更新ListView
                proAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateListReciver);
    }

    /**
     * ListView适配器
     */
    public class ProAdapter extends BaseAdapter{
        public ImageLoader imageLoader; //用来下载图片的类
        private String url;

        public ProAdapter(String imgUrl){
            imageLoader = new ImageLoader(getApplicationContext());
            url = imgUrl;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            if(position==-1)
                position=0;
            return dataList.get(position);
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
                v=View.inflate(SellerProListActivity.this, R.layout.lv_product, null);
            }else{
                v=convertView;
            }
            TextView tvPrdName = (TextView) v.findViewById(R.id.lv_prd_title);
            tvPrdName.setText(dataList.get(position).getProName());
            TextView tvPrice = (TextView) v.findViewById(R.id.lv_prd_price);
            tvPrice.setText("￥"+dataList.get(position).getProPrice());
            TextView tvBrief = (TextView) v.findViewById(R.id.lv_prd_brief);
            tvBrief.setText(dataList.get(position).getProBrief());
            ImageView prodImage = (ImageView) v.findViewById(R.id.lv_prd_img);
            imageLoader.DisplayImage(url+"?urlId="+dataList.get(position).getProSrc(),prodImage);
            return v;
        }
    }
}
