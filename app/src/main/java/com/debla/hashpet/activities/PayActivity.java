package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.GoodsInfo;
import com.debla.hashpet.Model.User;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.AppContext;
import com.debla.hashpet.Utils.HttpUtil;
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
 * Created by Dave-PC on 2018/3/7.
 */

public class PayActivity extends Activity{
    @BindView(R.id.confirm_pay)
    Button btnPay;

    private String name;
    private String address;
    private String phone;

    private User user;

    private Map<String,List<GoodsInfo>> childs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }
    public void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        childs = (Map<String, List<GoodsInfo>>) bundle.getSerializable("childs");
        name = bundle.getString("name");
        address = bundle.getString("address");
        phone = bundle.getString("phone");
        AppContext appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String status = (String)msg.obj;
            if("OK".equals(status)){
                Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("status",status);
                setResult(3,intent);
                finish();
            }else{
                Toast.makeText(PayActivity.this,"支付出现异常",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
    });

    public void initEvent(){
        //支付（提交）订单
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(childs);
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("ordUserId",String.valueOf(user.getUserid()));
                params.put("ordAddr",address);
                params.put("ordPhone",phone);
                params.put("ordType",String.valueOf(1));
                params.put("details",json);
                String url = HttpUtil.getUrl(getApplicationContext());
                HttpUtil client = new HttpUtil();
                client.postRequest(url + "newOrder", params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("debug","请求订单时出现错误");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        String JSON = response.body().string();
                        Type jsonType = new TypeToken<BaseJsonObject<Object>>(){}.getType();
                        BaseJsonObject<Object> jsonObject = null;
                        jsonObject = (BaseJsonObject) gson.fromJson(JSON,jsonType);
                        String status = jsonObject.getStatus();
                        Message msg = new Message();
                        msg.obj=status;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }
}
