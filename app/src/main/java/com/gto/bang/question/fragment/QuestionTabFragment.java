package com.gto.bang.question.fragment;

import com.gto.bang.R;
import com.gto.bang.base.BaseTabFragment;
import com.gto.bang.home.HActicleFragment;
import com.gto.bang.home.HComplaintsFragment;
import com.gto.bang.home.HQuestionFragment;
import com.gto.bang.home.HSupportFragment;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页
 */
public class QuestionTabFragment extends BaseTabFragment {

    public static String TAG = "首页TAB";

    public QuestionTabFragment() {
    }

    @Override
    public void init() {
        this.titles = new String[]{getString(R.string.tab_new),getString(R.string.tab_reccoment)};
        this.tabIds = new int[]{R.id.bang_e_section1, R.id.bang_e_section2};
        this.fragments = new Class[]{NewQuestionFragment.class,ReccomendQuestionFragment.class};
        this.tabNum = 2;
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