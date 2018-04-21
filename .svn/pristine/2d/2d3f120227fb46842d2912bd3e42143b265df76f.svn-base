package com.debla.hashpet.Utils;

import android.content.Context;
import android.util.Log;

import com.lzy.imagepicker.bean.ImageItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private PostFormBuilder mPost;
    private GetBuilder mGet;
    private OkHttpClient client;
    private Request.Builder builder;
    private Request request;

    public HttpUtil() {
        OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .build();

        mPost = OkHttpUtils.post();
        mGet = OkHttpUtils.get();
    }

    //封装请求
    public void postRequest(String url, Map<String, String> params, NewSellerCallBack callback) {
        mPost.url(url)
                .params(params)
                .build()
                .execute(callback);
    }

    //上传文件
    public void postFileRequest(String url, Map<String, String> params, ImageItem pathList, NewSellerCallBack callback) {

        String newPath = BitmapUtils.compressImageUpload(pathList.path);
        File file = new File(newPath);
        //files.put(pathList.name,new File(newPath));

        RequestBody request = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        mPost.url(url)
                .addFile("file",pathList.name,file)
                .build()
                .execute(callback);
    }

    public static String getUrl(Context context){
        Properties props = new Properties();
        try {
            InputStream in = context.getAssets().open("appConfig.properties");
            props.load(in);
        }catch (Exception e){
            Log.e("debug","读取配置文件出错");
            e.printStackTrace();
        }
        return props.getProperty("hosturl");
    }


    public void postRequest(String url, Map<String, String> params, Callback callback){
        client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder();
        Iterator iter = params.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            formBuilder.add(key,value);
        }
        RequestBody body;
        body = formBuilder.build();
        builder = new Request.Builder().url(url).post(body);
        request = builder.build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
