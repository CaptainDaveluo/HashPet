package com.debla.hashpet.Utils;

import android.content.Context;
import android.util.Log;

import com.debla.hashpet.Model.GoodsInfo;
import com.debla.hashpet.Model.ProductSellDetail;
import com.debla.hashpet.Model.StoreInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Dave-PC on 2018/2/19.
 * 购物车工具类
 */

public class ShopCarUtil {


    /**
     * 添加商品到购物车,手动更新数量也可以采用这个方法
     * @param context
     * @param goods
     * @param num
     */
    public static void addToShopCar(Context context,ProductSellDetail goods, Integer num){
        AppContext appContext = (AppContext)context;
        Map<String,List<GoodsInfo>>  shopCar = new HashMap();
        shopCar = appContext.getShopCar();
        String goodsId = String.valueOf(goods.getProId());
        GoodsInfo goodsInfo = new GoodsInfo(goodsId,goods.getProName(),goods.getProSrc(),goods.getProPrice(),num);
        List<GoodsInfo> childs = shopCar.get(String.valueOf(goods.getProShopId()));
        if(childs==null){
            childs = new ArrayList<>();
            childs.add(goodsInfo);
        }else {
            //检查是否有相同产品
            int i=0;
            for(;i<childs.size();i++){
                if(goodsId.equals(childs.get(i).getId())){
                    childs.get(i).CountPlus();
                    break;
                }
            }
            if(i>=childs.size())
                childs.add(goodsInfo);
        }
        String storeId = String.valueOf(goods.getProShopId());
        shopCar.put(storeId,childs);
        //同时更新店铺组
        List<StoreInfo> groups = appContext.getGroups();
        int i=0;
        for(;i<groups.size();i++){
            if(storeId.equals(groups.get(i).getId()))
                break;
        }
        if(i>=groups.size())
            addStoreInfo(context, String.valueOf(goods.getProShopId()));
        appContext.setShopCar(shopCar);
    }

    /**
     * 查询商品名
     * @param context
     * @param storeId
     * @return
     */
    public static void addStoreInfo(final Context context, final String storeId){
        HttpUtil httpUtil = new HttpUtil();
        String url = HttpUtil.getUrl(context);
        HashMap<String,String> params = new HashMap<>();
        params.put("storeId",storeId);
        httpUtil.postRequest(url + "queryStoreName", params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UtilsLog.d("获取商店名失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JSON = response.body().string();
                Log.e("debug",JSON);
                Gson gson = new Gson();
                Map<String, String> result= gson.fromJson(JSON, new TypeToken<Map<String, String>>(){}.getType());
                AppContext appContext = (AppContext)context;
                List<StoreInfo> groups = appContext.getGroups();
                StoreInfo storeInfo = new StoreInfo(storeId,result.get("result"));
                groups.add(storeInfo);
                appContext.setGroups(groups);
            }
        });
    }


    /**
     * 清空购物车
     */
    public static void clearShopCar(Context context){
        AppContext appContext = (AppContext)context;
        Map<String,List<GoodsInfo>>  shopCar = appContext.getShopCar();
        List<StoreInfo>  groups = appContext.getGroups();
        groups.clear();
        shopCar.clear();
        appContext.setShopCar(shopCar);
    }
/*
    public static boolean isGoodsExist(Context context,String storeId,String goodsId){
        AppContext appContext = (AppContext)context;
        Map<String,List<GoodsInfo>>  shopCar = appContext.getShopCar();
        if(shopCar.get(goodsId)!=null) {
            return true;
        }
        else
            return false;
    }*/


}
