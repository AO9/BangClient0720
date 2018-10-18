package com.gto.bang.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;

/**
 *
 */
public class BaseTabFragment extends Fragment implements ActionBar.TabListener {

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


    public BaseTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bang_experience_main, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        init();
        InitImageView(getView());
        InitTextView(getView());
        InitViewPager(getView());
    }


    /**
     * 为每个Tab view 设置监听器
     */
    private void InitTextView(View rootView ) {
        TextView t;
        for(int i=0;i<tabIds.length;i++){
            t=(TextView)rootView.findViewById(tabIds[i]);
            t.setText(titles[i]);
            t.setOnClickListener(new CommonOnClickListener(i));
        }
        if(tabNum==2){
            //暂时解决 兼容 两个或者三个TAB标签的情形
            rootView.findViewById(R.id.bang_e_section3).setVisibility(View.GONE);
            rootView.findViewById(R.id.bang_e_section4).setVisibility(View.GONE);
        }else if(tabNum==3){
            rootView.findViewById(R.id.bang_e_section4).setVisibility(View.GONE);
        }

    }

    /**
     * 初始化ViewPager
     * getActivity().getSupportFragmentManager()
     */
    private void InitViewPager( View rootView ) {
        mPager = (ViewPager) rootView.findViewById(R.id.bang_e_vp);
        mPager.setAdapter(new CommonViewPagerAdapter(getChildFragmentManager(),this.fragments,this.titles));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
        //设置选中状态
        TextView tv=(TextView)rootView.findViewById(tabIds[0]);
        tv.setSelected(true);
    }

    /**
     * 初始化动画
     */
    private void InitImageView(View rootView) {
        cursor = (ImageView) rootView.findViewById(R.id.bang_e_iv);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.x)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
     * 为子类预留的数据初始化方法
     */
    protected void init(){

    }

    /**
     * 请求的标签
     */
    protected String getRequestTag(){
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }

    public SharedPreferences getSharedPreferences(){
        return getActivity().getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
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


    /**
     * TAB切换效果
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3
        int three = one * 3;// 页卡1 -> 页卡4

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            Log.i("sjl","onPageSelected :"+arg0+" currIndex:"+currIndex);
            TextView tv;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    }else if (currIndex == 0) {
                        animation = new TranslateAnimation(0, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }else if (currIndex==1){
                        animation = new TranslateAnimation(one, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }else if (currIndex==2){
                    animation = new TranslateAnimation(two, two, 0, 0);
                }
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    }else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                    }else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, three, 0, 0);
                    }
                    break;

            }
            updateTextColor(arg0);
            currIndex = arg0;
            if(animation!=null){
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(200);
                cursor.startAnimation(animation);
            }
        }


        //更新tab标签文字选中状态
        private void updateTextColor(int selected) {
            TextView tv;
            for(int i=0;i<titles.length;i++){
                tv=(TextView)getView().findViewById(tabIds[i]);
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
