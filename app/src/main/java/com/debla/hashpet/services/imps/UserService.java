package com.debla.hashpet.services.imps;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.debla.hashpet.Model.BaseJsonObject;
import com.debla.hashpet.Model.User;
import com.debla.hashpet.services.IUserService;
import com.debla.hashpet.services.OkHttpHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2017/11/29.
 */

public class UserService implements IUserService,OkHttpHelper{
    private OkHttpClient client;
    private User         user;
    private static String url ;
    private Request.Builder builder;
    private Request request;
    private Call call;
    private String result;
    private Handler handler;
    private Gson gson;

    /**
     * 初始化操作
     */
    public void init(Context context){
        client = new OkHttpClient();

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
    /**
     * 登陆操作
     * @param user
     * @return
     */
    @Override
    public void doLogin(User user) {
        String str = url+"doUserLogin?phoneNum="+user.getPhonenum()+"&userPass="+user.getPassword();
        try {
            post(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 请求接口
     * @param apiurl
     * @return
     */
    @Override
    public void post(String apiurl) throws IOException{
        builder = new Request.Builder().url(apiurl);

        request = builder.build();
        call =client.newCall(request) ;
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("debug","请求接口出现异常"+e.getMessage());
                Message msg = new Message();
                msg.what=1002;
                msg.obj=user;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Analize(json);

            }
        };
        call.enqueue(callback);
    }

    @Override
    public void registUser(User user) {
        String str = url+"doUserRegist?phoneNum="+user.getPhonenum()+"&userPass="+user.getPassword()+"&userType="+user.getUsertype();
        try {
            Log.e("debug",str);
            post(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void Analize(String JSON) {
        gson = new Gson();
        try {
            Log.e("debug",JSON);
            Type jsonType = new TypeToken<BaseJsonObject<User>>(){}.getType();
            BaseJsonObject<User> jsonObject = null;
            jsonObject = (BaseJsonObject) gson.fromJson(JSON, jsonType);
            user = (User) jsonObject.getResult();
            if(user==null)
                throw new Exception();
            Message msg = new Message();
            msg.what=1001;
            msg.obj=user;
            handler.sendMessage(msg);
        }catch (Exception e){
            Log.e("debug","解析json过程出现异常"+e.getMessage());
            Message msg = new Message();
            msg.what=1002;
            msg.obj=null;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
