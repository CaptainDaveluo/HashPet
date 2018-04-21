package com.debla.hashpet.services.imps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.Product;
import com.debla.hashpet.activities.EditPetActivity;
import com.debla.hashpet.services.IPetService;
import com.debla.hashpet.services.OkHttpHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2017/12/23.
 */

public class PetService implements IPetService,OkHttpHelper{
    private OkHttpClient client;
    private static String url;
    private Request request;
    private Call call;
    private RequestBody body;
    private Handler handler;
    private Gson gson;      //解析json
    private Context context;


    @Override
    public void createNewPet(Map params) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        formBuilder.add("urlId",(String)params.get("urlId"));
        formBuilder.add("gender", (String) params.get("gender"));
        formBuilder.add("petName", (String) params.get("petName"));
        formBuilder.add("petType", (String) params.get("petType"));
        formBuilder.add("petAge", (String) params.get("petAge"));
        formBuilder.add("price", (String) params.get("price"));
        formBuilder.add("brief", (String) params.get("brief"));
        formBuilder.add("shopId", (String) params.get("shopId"));
        formBuilder.add("proStock",(String)params.get("proStock"));
        String urlapi = url+"createNewPet";
        body = formBuilder.build();
        try {
            post(urlapi);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init(Context context){
        client = new OkHttpClient();
        this.context = context;
        Properties props = new Properties();
        try {
            InputStream in = context.getAssets().open("appConfig.properties");
            props.load(in);
        }catch (Exception e){
            Log.e("debug","读取配置文件出错");
            e.printStackTrace();
        }
        url = props.getProperty("hosturl");
        Log.e("debug",url);
    }

    @Override
    public void post(String apiurl) throws IOException {
        request = new Request.Builder().url(apiurl).post(body).build();
        call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("debug","请求失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JSON = response.body().string();
                Analize(JSON);
            }
        });
    }

    @Override
    public void Analize(String JSON) {
        gson = new Gson();
        try {
            Log.e("debug",JSON);
            Type jsonType = new TypeToken<BaseJsonObject<Product>>(){}.getType();
            BaseJsonObject<Product> jsonObject = null;
            jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
            Product product = (Product) jsonObject.getResult();
            Message msg = new Message();
            msg.obj=product;
            handler.sendMessage(msg);
            //发送更新List的广播,将新增的商品数据更新
            Intent intent = new Intent(EditPetActivity.action);
            Bundle bundle = new Bundle();
            bundle.putSerializable("proInfo",product);
            intent.putExtra("data",bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }catch (Exception e){
            Log.e("debug","解析json过程出现异常"+e.getMessage());
            e.printStackTrace();
            handler.sendEmptyMessage(0);
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
