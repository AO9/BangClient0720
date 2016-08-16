package com.gto.bang.bangwrite.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gto.bang.R;
import com.gto.bang.bangwrite.fragment.BWLoginFragment;
import com.gto.bang.bangwrite.fragment.BWRegisterFragment;

import java.util.Locale;

/**
 * Created by shenjialong on 16/6/5.
 */
public class BWFragmentAdapter extends FragmentPagerAdapter {
    Activity context;
    String[] titles ;
    public BWFragmentAdapter(FragmentManager fm, Activity  context) {
        super(fm);
        this.context=context;
        titles = new String[]{context.getString(R.string.action_login), context.getString(R.string.action_register)};
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f=null;
        if(position==0){
            f=new BWLoginFragment();
        }else if(position==1){
            f=new BWRegisterFragment();
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
