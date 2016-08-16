package com.gto.bang.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.Locale;

/**
 * Created by shenjialong on 16/6/10 01:50.
 */
public class CommonViewPagerAdapter extends FragmentPagerAdapter {

    Class[] fragments=new Class[]{};
    String [] titles=new String[]{};

    public CommonViewPagerAdapter(FragmentManager fm, Class[] fragments, String[] titles) {
        super(fm);
        this.fragments=fragments;
        this.titles=titles;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment f=null;
        try {
            f=(Fragment) fragments[position].newInstance();
//            f.setArguments(new Bundle().p);
        } catch (Exception e) {
            Log.i("sjl","初始化fragment失败");
            e.printStackTrace();
        }
        return f;
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        return titles[position];
    }
}
