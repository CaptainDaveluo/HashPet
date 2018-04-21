package com.debla.hashpet.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.debla.hashpet.Model.User;
import com.debla.hashpet.R;
import com.debla.hashpet.services.imps.UserService;

/**
 * Created by Dave-PC on 2017/11/22.
 */

public class RegistActivity extends Activity {
    private EditText etPhoneNum;
    private EditText etPassword;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        etPhoneNum = (EditText) findViewById(R.id.register_phone);
        etPassword = (EditText) findViewById(R.id.register_pass);
    }

    public void onRegist(View v){
        UserService userService = new UserService();
        userService.init(getApplicationContext());
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==1001){
                    user = (User)msg.obj;
                    Toast.makeText(getApplicationContext(),"用户"+user.getPhonenum()+"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }else{
                    Toast.makeText(getApplicationContext(),"用户注册失败",Toast.LENGTH_SHORT).show();
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
        user.setUsertype("0");
        userService.registUser(user);
    }

}
