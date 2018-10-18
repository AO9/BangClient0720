package com.gto.bang.home;

import com.gto.bang.R;
import com.gto.bang.base.BaseTabFragment;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 交流互动TAB
 */
public class HInteractionFragment extends BaseTabFragment {

    public HInteractionFragment() {
    }

    @Override
    public  void init(){
        this.titles = new String[]{getString(R.string.tab_question),getString(R.string.tab_experience),getString(R.string.tab_complaints)};
        this.tabIds=new int[]{R.id.bang_e_section1,R.id.bang_e_section2,R.id.bang_e_section3};
        this.fragments=new Class[]{ HQuestionFragment.class,HExperienceFragment.class,HComplaintsFragment.class};
        this.tabNum=3;
    }

    @Override
    protected String getRequestTag(){
        return "HInteractionFragment_request";
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("主页－交流"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("主页－交流");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
}