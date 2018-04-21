package com.debla.hashpet.services.imps;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.Product;
import com.debla.hashpet.Utils.HttpUtil;
import com.debla.hashpet.services.IProService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2017/12/27.
 */

public class ProService implements IProService{
    private String url;
    private Gson gson;
    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void getProductByCondition(Map<String, String> params) {
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.postRequest(url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("debug","请求失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JSON = response.body().string();
                gson = new Gson();
                try {
                    Type jsonType = new TypeToken<BaseJsonObject<List<Product>>>() {
                    }.getType();
                    BaseJsonObject<List<Product>> jsonObject = null;
                    jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
                    List<Product> res = (List<Product>) jsonObject.getResult();
                    Message msg = new Message();
                    msg.what=1004;
                    msg.obj=res;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("debug","解析json出错"+e.getMessage());
                }
            }
        });
    }
}
