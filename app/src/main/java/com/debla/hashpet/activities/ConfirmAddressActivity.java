package com.debla.hashpet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.debla.hashpet.Model.GoodsInfo;
import com.debla.hashpet.Model.User;
import com.debla.hashpet.R;
import com.debla.hashpet.Utils.AppContext;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dave-PC on 2018/3/6.
 * 确认地址Acticity
 */

public class ConfirmAddressActivity extends Activity{
    //联系人
    private String name="谢晓鹏";
    @BindView(R.id.confirm_name)
    TextView tvName;
    //联系方式
    private String phone="18258458665";
    @BindView(R.id.confirm_phone)
    TextView tvPhone;
    //地址信息
    private String address="浙江省 杭州市 余杭区鸬鸟镇太平山村太公堂22组13号";
    @BindView(R.id.confirm_address_detail)
    TextView tvAddress;

    private User user;

    private Map<String,List<GoodsInfo>> childs;
    @BindView(R.id.btn_confirm_address)
    Button btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }

    public void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        childs= (Map<String, List<GoodsInfo>>) bundle.getSerializable("childs");
        user = ((AppContext)getApplicationContext()).getUser();
        phone=user.getPhonenum();
        name = user.getNickname();
    }

    public void initEvent(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmAddressActivity.this,PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("address",address);
                bundle.putString("phone",phone);
                bundle.putSerializable("childs", (Serializable) childs);
                intent.putExtra("data",bundle);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==3){
            String status=data.getStringExtra("status");
            if("OK".equals(status)){
                setResult(2,data);
                this.finish();
            }
        }
    }
}
