package com.gto.bang.home;

import com.gto.bang.R;
import com.gto.bang.base.BaseTabFragment;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 20171119 消息
 */
public class HMessageFragment extends BaseTabFragment {

    public HMessageFragment() {
    }

    public void initViews() {

    }

    @Override
    public void init() {
        this.titles = new String[]{getString(R.string.bang_pinfo), getString(R.string.bang_pnotice)};
        this.tabIds = new int[]{R.id.bang_e_section1, R.id.bang_e_section2};
        this.fragments = new Class[]{HPersonMessageFragment.class, HNoticeFragment.class};
        this.tabNum = 2;
    }

    @Override
    protected String getRequestTag() {
        return "HMessageFragment_request";
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("消息");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
}