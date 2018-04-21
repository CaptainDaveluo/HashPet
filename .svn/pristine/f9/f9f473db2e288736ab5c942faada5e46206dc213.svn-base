package com.debla.hashpet.Utils;

import android.app.Application;
import android.os.Handler;

import com.debla.hashpet.Model.GoodsInfo;
import com.debla.hashpet.Model.StoreInfo;
import com.debla.hashpet.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dave-PC on 2017/12/14.
 */

public class AppContext extends Application {
    //共享变量
    private Handler handler;
    private User user;
    private Map innerMap = new HashMap();
    private Map<String,List<GoodsInfo>> shopCar = new HashMap<String, List<GoodsInfo>>();
    private List<StoreInfo> groups = new ArrayList<>();

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map getInnerMap() {
        return innerMap;
    }

    public void setInnerMap(Map innerMap) {
        this.innerMap = innerMap;
    }

    public Map<String,List<GoodsInfo>> getShopCar() {
        return shopCar;
    }

    public void setShopCar(Map<String,List<GoodsInfo>> shopCar) {
        this.shopCar = shopCar;
    }

    public List<StoreInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<StoreInfo> groups) {
        this.groups = groups;
    }
}
