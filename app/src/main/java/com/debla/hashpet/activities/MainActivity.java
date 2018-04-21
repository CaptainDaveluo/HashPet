package com.debla.hashpet.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.debla.hashpet.R;
import com.debla.hashpet.fragments.IndexFragment;
import com.debla.hashpet.fragments.MarketFragment;
import com.debla.hashpet.fragments.MineFragment;
import com.debla.hashpet.fragments.ServiceFragment;

import java.util.ArrayList;
import java.util.List;

import static com.debla.hashpet.fragments.MineFragment.broadcastReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragments;

    private Fragment mIndexView;            //首页视图
    private Fragment mMarketView;           //商场视图
    private Fragment mServiceView;          //服务视图
    private Fragment mMineView;             //我的视图

    private ImageView mImgIndex;
    private ImageView mImgMarket;
    private ImageView mImgService;
    private ImageView mImgMine;


    private LinearLayout tabIndex;      //首页Tab
    private LinearLayout tabMarket;     //商场Tab
    private LinearLayout tabService;    //服务Tab
    private LinearLayout tabMine;       //我的Tab


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initViewPager();
        initEvent();

    }
    /**
     * 初始化视图
     */
    public void initView(){
        mViewPager = (ViewPager) findViewById(R.id.index_viewpage);

        mImgIndex = (ImageView) findViewById(R.id.img_home);
        mImgMarket = (ImageView) findViewById(R.id.img_buy);
        mImgService = (ImageView) findViewById(R.id.img_service);
        mImgMine = (ImageView) findViewById(R.id.img_mine);

        mImgIndex.setBackgroundResource(R.drawable.ic_show_press);
        mFragments = new ArrayList<Fragment>();

        tabIndex = (LinearLayout) findViewById(R.id.tab_index);
        tabMarket = (LinearLayout) findViewById(R.id.tab_market);
        tabService = (LinearLayout) findViewById(R.id.tab_service);
        tabMine = (LinearLayout) findViewById(R.id.tab_mine);
    }

    /**
     * 初始化ViewPager
     */
    public void initViewPager(){
        LayoutInflater mLi = LayoutInflater.from(this);
        //mIndexView = mLi.inflate(R.layout.index_fragment,null);
        //mMarketView = mLi.inflate(R.layout.market_fragment,null);
        mIndexView = new IndexFragment();
        mMarketView = new MarketFragment();
        mServiceView = new ServiceFragment();
        mMineView = new MineFragment();
        mFragments.add(mIndexView);
        mFragments.add(mMarketView);
        mFragments.add(mServiceView);
        mFragments.add(mMineView);
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mPagerAdapter);
    }

    public void resetImg(){
        mImgIndex.setBackgroundResource(R.drawable.ic_show);
        mImgMarket.setBackgroundResource(R.drawable.ic_bottom_share);
        mImgService.setBackgroundResource(R.drawable.ic_service);
        mImgMine.setBackgroundResource(R.drawable.ic_me);
    }

    public void initEvent(){
        tabIndex.setOnClickListener(this);
        tabMarket.setOnClickListener(this);
        tabService.setOnClickListener(this);
        tabMine.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                switch (currentItem){
                    case 0:
                        resetImg();
                        mImgIndex.setBackgroundResource(R.drawable.ic_show_press);
                        break;
                    case 1:
                        resetImg();
                        mImgMarket.setBackgroundResource(R.drawable.ic_bottom_share_press);
                        break;
                    case 2:
                        resetImg();
                        mImgService.setBackgroundResource(R.drawable.ic_service_press);
                        break;
                    case 3:
                        resetImg();
                        mImgMine.setBackgroundResource(R.drawable.ic_me_press);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_index:
                resetImg();
                mViewPager.setCurrentItem(0);
                mImgIndex.setBackgroundResource(R.drawable.ic_show_press);
                break;
            case R.id.tab_market:
                resetImg();
                mViewPager.setCurrentItem(1);
                mImgMarket.setBackgroundResource(R.drawable.ic_bottom_share_press);
                break;
            case R.id.tab_service:
                resetImg();
                mViewPager.setCurrentItem(2);
                mImgService.setBackgroundResource(R.drawable.ic_service_press);
                break;
            case R.id.tab_mine:
                resetImg();
                mViewPager.setCurrentItem(3);
                mImgMine.setBackgroundResource(R.drawable.ic_me_press);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //一定要在程序退出的时候把这个广播给解除绑定,不然会出现内存泄露
        if(broadcastReceiver!=null) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            localBroadcastManager.unregisterReceiver(broadcastReceiver);
        }
    }



}


