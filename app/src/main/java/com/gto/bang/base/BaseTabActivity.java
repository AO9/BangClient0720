package com.gto.bang.base;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gto.bang.R;

/**
 * 带有TAB标签的页面基类
 * 支持最多有三个TAB的情形
 */
public class BaseTabActivity extends BaseActivity implements ActionBar.TabListener {

    public ViewPager mPager;// 页卡内容
    private ImageView cursor;// 动画图片
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    ViewPager mViewPager;

    protected  String[] titles ;
    protected  int tabNum;
    protected  int[] tabIds;
    protected  Class [] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_experience_main);
        init();
        InitImageView();
        InitTextView();
        InitViewPager();
    }

    /**
     * 为子类预留的数据初始化方法
     */
    protected void init(){

    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 为每个Tab view 设置监听器
     */
    private void InitTextView() {
        TextView t;
        for(int i=0;i<tabIds.length;i++){
            t=(TextView) findViewById(tabIds[i]);
            t.setText(titles[i]);
            t.setOnClickListener(new CommonOnClickListener(i));
        }
        if(tabNum<3){
            //暂时解决 兼容 两个或者三个TAB标签的情形
            findViewById(R.id.bang_e_section3).setVisibility(View.GONE);
        }
    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.bang_e_vp);
        mPager.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(),this.fragments,this.titles));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
        //设置选中状态
        TextView tv=(TextView)findViewById(tabIds[0]);
        tv.setSelected(true);
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.bang_e_iv);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.x)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / tabNum - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }


    /**
     * Tab view 监听器，让viewPager动起来
     */
    public class CommonOnClickListener implements View.OnClickListener {
        private int index = 0;

        public CommonOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * TAB切换效果
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            TextView tv;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;

            }
            updateTextColor(arg0);
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(200);
            cursor.startAnimation(animation);
        }


        //更新tab标签文字选中状态
        private void updateTextColor(int selected) {
            TextView tv;
            for(int i=0;i<titles.length;i++){
                tv=(TextView)findViewById(tabIds[i]);
                if(selected==i){
                    tv.setSelected(true);
                }else{
                    tv.setSelected(false);
                }
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
