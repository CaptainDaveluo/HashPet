package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.Product;
import com.debla.hashpet.Model.SellerInfo;
import com.debla.hashpet.Model.URLImage;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.AppContext;
import com.debla.hashpet.Utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2017/12/22.
 */

public class EditProdActivity extends Activity {
    private URLImage urlImage;
    private String localImg;
    private String brief;
    private AppContext appContext;
    private EditText et_proName;
    private Spinner sp_proType;
    private EditText et_price;
    private EditText et_stock;
    private EditText et_intergral;
    private Button   btnSubmit;
    private ImageView imageView;
    private Gson gson;


    //商品类型，分别表示宠物日用，宠物清洁，宠物玩具，宠物服装
    private String [] proTypes = {"daily","clean","toy","clothes"};
    private String strProType;

    public static final String action="com.debla.hashpet.EditProdAction";

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.obj!=null){
                Product product = (Product)msg.obj;
                Toast.makeText(EditProdActivity.this,"商品"+product.getProName()+"上架成功",Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpro);
        appContext = (AppContext) getApplicationContext();
        getURLImg();
        init();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()==0){
                    //上传表单
                    HttpUtil httpUtil = new HttpUtil();
                    Map params = new HashMap();
                    params.put("proName",et_proName.getText().toString());
                    params.put("proTag",strProType);
                    params.put("proPrice",et_price.getText().toString());
                    params.put("proStock",et_stock.getText().toString());
                    params.put("proIntergral",et_intergral.getText().toString());
                    params.put("urlId",urlImage.getUrlId());
                    params.put("brief",brief);
                    SellerInfo seller = (SellerInfo)(appContext.getInnerMap().get("seller"));
                    Integer shopId = seller.getShopId();
                    params.put("shopId",String.valueOf(shopId));
                    String url = HttpUtil.getUrl(getApplicationContext())+"/newProduct";
                    httpUtil.postRequest(url, params, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("debug","新建商品过程出错");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            Log.e("debug",json);
                            gson =new Gson();
                            Type jsonType = new TypeToken<BaseJsonObject<Product>>(){}.getType();
                            BaseJsonObject<Product> jsonObject = null;
                            jsonObject = (BaseJsonObject) gson.fromJson(json, jsonType);
                            Product product = (Product) jsonObject.getResult();
                            Message msg = new Message();
                            msg.obj=product;
                            handler.sendMessage(msg);
                            //发送更新List的广播,将新增的商品数据更新
                            Intent intent = new Intent(EditPetActivity.action);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("proInfo",product);
                            intent.putExtra("data",bundle);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        }
                    });
                }
            }
        });
    }

    private void init(){
        et_proName = (EditText) findViewById(R.id.pro_nanme);
        sp_proType = (Spinner) findViewById(R.id.pro_type);
        et_price = (EditText) findViewById(R.id.edit_pro_price);
        et_stock = (EditText) findViewById(R.id.edit_pro_stock);
        et_intergral = (EditText) findViewById(R.id.edit_pet_intergral);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        imageView = (ImageView) findViewById(R.id.edit_piv_head);
        Bitmap img = BitmapFactory.decodeFile(localImg);
        imageView.setImageBitmap(img);
        sp_proType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProType = proTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strProType = proTypes[0];
            }
        });
    }

    /**
     * 获取之前的图片信息
     */
    public void getURLImg(){
        Intent intent =getIntent();
        Bundle bd = intent.getBundleExtra("data");
        urlImage = (URLImage) bd.get("img");
        localImg = bd.getString("imgurl");
        brief = bd.getString("brief");
    }

    public int check(){
        if("".equals(et_proName.getText().toString()))
            return 1;
        if("".equals(et_price.getText().toString()))
            return 2;
        if("".equals(et_stock.getText().toString()))
            return 3;
        return 0;
    }
}
