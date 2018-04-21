package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.debla.hashpet.Model.User;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.AppContext;
import com.debla.hashpet.services.imps.UserService;

/**
 * Created by Dave-PC on 2017/10/29.
 */

public class LoginActivity extends Activity{
    private EditText etPhoneNum;
    private EditText etPassword;
    private User user;
    public static final String action="com.debla.hashpet.LoginAction";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        etPhoneNum = (EditText) findViewById(R.id.register_phone);
        etPassword = (EditText) findViewById(R.id.register_pass);
    }

    public void toRegist(View v){
        Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
        startActivity(intent);
    }

    public void onLogin(View v){
        UserService userService = new UserService();
        userService.init(getApplicationContext());
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==1001){
                    user = (User)msg.obj;
                    if(user!=null) {
                        //登陆成功,需要写入cookie，并通知个人中心界面刷新昵称
                        Toast.makeText(getApplicationContext(), "用户" + user.getPhonenum() + "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(action);
                        Bundle bd = new Bundle();
                        bd.putSerializable("user",user);
                        intent.putExtra("data",bd);
                        //getApplicationContext().sendBroadcast(intent);
                        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);
                        //设置全局变量user
                        AppContext context = (AppContext) getApplication();
                        context.setUser(user);
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"用户登陆失败,请检查网络",Toast.LENGTH_SHORT).show();

                    return true;
                }else{
                    Toast.makeText(getApplicationContext(),"用户登陆失败",Toast.LENGTH_SHORT).show();
                    finish();
                    return false;
                }
            }
        });
        userService.setHandler(handler);
        String phoneNum = etPhoneNum.getText().toString().trim();
        String passWord = etPassword.getText().toString().trim();
        User user  = new User();
        user.setPhonenum(phoneNum);
        user.setPassword(passWord);
        userService.doLogin(user);
    }

}
