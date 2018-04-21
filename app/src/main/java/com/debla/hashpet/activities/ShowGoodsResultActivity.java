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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.debla.hashpet.Model.BaseJsonObject;
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
 * Created by Dave-PC on 2018/4/15.
 */

public class ShowGoodsResultActivity extends Activity{

    @BindView(R.id.lv_showgoods_list)
    ListView mListView;

    private String url;
    private String proName;
    private String proTag;
    private String proShopId;
    private List<ProductSellDetail> dataList;
    private ProAdapter proAdapter;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_result);
        ButterKnife.bind(this);
        initParams();
        init();
    }

    public void initParams(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("params");
        proName = bundle.getString("proName");
        proTag = bundle.getString("proTag");
        proShopId = bundle.getString("proShopId");
    }

    public void init(){
        url = HttpUtil.getUrl(getApplication())+"queryProduct";
        Map<String,String> params = new HashMap<>();
        if(proName!=null &&!"".equals(proName))
            params.put("proName",proName);
        if(proTag!=null && !"".equals(proTag))
            params.put("proTag",proTag);
        if(proShopId!=null && !"".equals(proShopId))
            params.put("proShopId",proShopId);
        /*ProService proService = new ProService();
        proService.setUrl(url);
        proService.setHandler(listHandler);
        proService.getProductByCondition(params);*/
        initDataList(params);
    }


    //从服务器获取宝贝资源
    public void initDataList(Map<String,String> params) {
        gson = new Gson();
        HttpUtil httpUtil = new HttpUtil();
        String url = HttpUtil.getUrl(getApplicationContext()) + "/queryProduct";
        httpUtil.postRequest(url, params, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("debug", "从服务器读取首页信息失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JSON = response.body().string();
                try {
                    Log.e("debug", JSON);
                    Type jsonType = new TypeToken<BaseJsonObject<List<ProductSellDetail>>>() {
                    }.getType();
                    BaseJsonObject<SellerInfo> jsonObject = null;
                    jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
                    dataList = (List<ProductSellDetail>) jsonObject.getResult();
                    Message msg = new Message();
                    msg.what=1004;
                    listHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 处理ListView获取数据
     */
    private Handler listHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1004){
                String imgUrl=HttpUtil.getUrl(getApplicationContext())+"getImage";
                proAdapter = new ProAdapter(imgUrl);
                mListView.setAdapter(proAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(),ProDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("product",dataList.get(position));
                        intent.putExtra("data",bundle);
                        startActivity(intent);
                    }
                });
            }
            return false;
        }
    });



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
                v=View.inflate(ShowGoodsResultActivity.this, R.layout.lv_product, null);
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
