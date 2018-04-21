package com.debla.hashpet.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.debla.hashpet.Model.User;
import com.debla.hashpet.services.imps.UserService;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dave-PC on 2018/2/17.
 * 本地数据保存类，用于保存本地数据
 */

public class LocalDataHelper {
    private static SharedPreferences sp;
    private static Context mContext;

    public LocalDataHelper(Context context){
        mContext = context;
    }

    public static boolean isKeyExist(String name ,String key){
        sp = mContext.getSharedPreferences(name,Context.MODE_PRIVATE);
        if(sp.getString(key,null)==null||"null".equals(sp.getString(key,"null")))
            return false;
        else
            return true;
    }

    public static void saveString(String key,String value){
        sp = mContext.getSharedPreferences("string",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String readString(String key){
        sp = mContext.getSharedPreferences("string",Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }

    /**
     * 保存购物车信息到本地
     * @param cart
     */
    public static void saveProducts( Map<String,Integer> cart){
        sp = mContext.getSharedPreferences("shopCart",Context.MODE_PRIVATE);
         Iterator iterator= cart.entrySet().iterator();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        while(iterator.hasNext()){
            Map.Entry<String,Integer> entry = (Map.Entry<String, Integer>) iterator.next();
            String goodId = entry.getKey();
            Integer num = entry.getValue();
            editor.putInt(goodId,num);
            editor.commit();
        }
    }

    /**
     * 清空本地下的购物车信息
     */
    public static void clearShopCar(){
        sp = mContext.getSharedPreferences("shopCart",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 使用加密系统对密码加密后保存
     * @param username
     * @param password
     */
    public static void saveUser(String username,String password) throws Exception {
        sp = mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("username",username);
        String pwd = AESUtils.encrypt("pwd",password);
        editor.putString("password",pwd);
        editor.commit();
    }

    /**
     * 读取本地保存的用户信息，若存在则登陆
     */
    public static void readUserAndLogin() throws Exception{
        sp = mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        String username=sp.getString("username",null);
        if(username!=null) {
            String password = sp.getString("password", null);
            String pwd = AESUtils.decrypt("pwd", password);
            UserService userService = new UserService();
            User user = new User();
            user.setUsername(username);
            user.setPassword(pwd);
            userService.doLogin(user);
        }
    }

}
