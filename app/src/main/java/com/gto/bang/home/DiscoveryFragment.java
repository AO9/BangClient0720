package com.gto.bang.home;

import com.gto.bang.R;
import com.gto.bang.base.BaseTabFragment;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页
 */
public class DiscoveryFragment extends BaseTabFragment {

    public static String TAG = "首页TAB";

    public DiscoveryFragment() {
    }

    @Override
    public void init() {
        this.titles = new String[]{getString(R.string.tab_question), getString(R.string.article), getString(R.string.tab_complaints)};
        this.tabIds = new int[]{R.id.bang_e_section1, R.id.bang_e_section2, R.id.bang_e_section3};
        this.fragments = new Class[]{HQuestionFragment.class,HActicleFragment.class, HComplaintsFragment.class};
        this.tabNum = 3;

//        HExperienceFragment 经验
    }

    @Override
    protected String getRequestTag() {
        return "HInteractionFragment_request";
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
}