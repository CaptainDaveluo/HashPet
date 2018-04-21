package com.debla.hashpet.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.SellerInfo;
import com.debla.hashpet.Model.User;
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
 * Created by Dave-PC on 2017/12/24.
 */

public class NewSellerActivity extends Activity{
    private User user;
    private EditText et_shopName;
    private EditText et_shopAdress;
    private EditText et_shopBrief;
    private EditText et_shopPhone;
    private Gson gson;
    private AppContext appContext;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(NewSellerActivity.this,"创建商铺成功，您已经成为商家用户!",Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beseller);
        AppContext context =(AppContext) getApplicationContext();
        user = context.getUser();
        et_shopName = (EditText) findViewById(R.id.et_shop_name);
        et_shopAdress = (EditText) findViewById(R.id.et_shopaddress);
        et_shopBrief = (EditText) findViewById(R.id.et_shopbrief);
        et_shopPhone = (EditText) findViewById(R.id.et_phone);
        context = (AppContext)getApplicationContext();
    }

    public void onCreateShop(View v){
        switch (check()){
            case 0:
                //验证通过
                break;
            case 1:
                Toast.makeText(NewSellerActivity.this,"请填写店铺名称",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(NewSellerActivity.this,"请填写店铺地址",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(NewSellerActivity.this,"请填写店铺简介",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(NewSellerActivity.this,"请填写联系方式",Toast.LENGTH_SHORT).show();
                break;
        }
        if(check()==0){
            String url=HttpUtil.getUrl(getApplicationContext());
            HttpUtil util = new HttpUtil();
            url=url+"registSeller";
            Map<String,String> params = new HashMap<String,String>();
            params.put("userId",String.valueOf(user.getUserid()));
            params.put("shopName",et_shopName.getText().toString());
            params.put("address",et_shopAdress.getText().toString());
            params.put("brief",et_shopBrief.getText().toString());
            params.put("phoneNum",et_shopPhone.getText().toString());
            util.postRequest(url, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e("debug","请求失败"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json=response.body().string();
                    Analize(json);
                    appContext = (AppContext)getApplicationContext();
                    User user=appContext.getUser();
                    user.setUsertype("1");
                    Message msg = new Message();
                    handler.sendMessage(msg);
                }
            });
        }
    }

    public int check(){
        if("".equals(et_shopName.getText().toString()))
            return 1;
        if("".equals(et_shopAdress.getText().toString()))
            return 2;
        if("".equals(et_shopBrief.getText().toString()))
            return 3;
        if("".equals(et_shopPhone.getText().toString()))
            return 4;
        return 0;
    }

    public void Analize(String JSON){
        gson = new Gson();
        try {
            Log.e("debug",JSON);
            Type jsonType = new TypeToken<BaseJsonObject<SellerInfo>>(){}.getType();
            BaseJsonObject<SellerInfo> jsonObject = null;
            jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
            SellerInfo seller = (SellerInfo) jsonObject.getResult();
            //放入全局变量
            Map map = new HashMap();
            map.put("seller",seller);
            appContext.setInnerMap(map);
        }catch (Exception e){
            Log.e("debug","解析json过程出现异常"+e.getMessage());
            e.printStackTrace();
        }
    }

}
