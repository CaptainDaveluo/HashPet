package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.debla.hashpet.Model.Product;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.HttpUtil;
import com.debla.hashpet.Utils.ImageLoader;
import com.debla.hashpet.services.imps.ProService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private List<Product> dataList;
    private ProAdapter proAdapter;

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
        ProService proService = new ProService();
        proService.setUrl(url);
        proService.setHandler(listHandler);
        proService.getProductByCondition(params);

    }


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
                mListView.setAdapter(proAdapter);
                mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
