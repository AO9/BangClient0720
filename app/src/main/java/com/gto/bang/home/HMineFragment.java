package com.gto.bang.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseInputFragment;
import com.gto.bang.login.LoginActivity;
import com.gto.bang.navigation.AboutActivity;
import com.gto.bang.navigation.FeedbackActivity;
import com.gto.bang.navigation.WalletActivity;
import com.gto.bang.personal.activity.PExperienceActivity;
import com.gto.bang.personal.activity.PQuestionActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.youth.YouthHomeActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的
 */
public class HMineFragment extends BaseInputFragment {

    public HMineFragment() {
    }

    private LinearLayout experience;
    private LinearLayout question;
    private LinearLayout head;
    private LinearLayout feedback;
    private LinearLayout about;
    private LinearLayout collection;
    private LinearLayout setting;
    private LinearLayout coin;
    private LinearLayout youth;

    private TextView headIcon;
    private TextView userName;

    LinearLayout[] rls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.personal_main, container, false);
        return rootView;
    }

    public void initViews() {
        experience = (LinearLayout) getView().findViewById(R.id.mine_experience);
        coin = (LinearLayout) getView().findViewById(R.id.coin);
        question = (LinearLayout) getView().findViewById(R.id.mine_answer);
        head = (LinearLayout) getView().findViewById(R.id.head_ll);
        feedback = (LinearLayout) getView().findViewById(R.id.feedback_ll);

        collection = (LinearLayout) getView().findViewById(R.id.collection);
        setting = (LinearLayout) getView().findViewById(R.id.setting);
        about = (LinearLayout) getView().findViewById(R.id.about_ll);
        youth = (LinearLayout) getView().findViewById(R.id.youth_ll);

        headIcon = (TextView) getView().findViewById(R.id.headIcon);
        userName = (TextView) getView().findViewById(R.id.userName);
        rls = new LinearLayout[]{question, experience, head, feedback, about, collection, setting, coin, youth};

        String name = getSharedPreferences().getString(Constant.USERNAME_V1, Constant.EMPTY);
        String id = getSharedPreferences().getString(Constant.ID, "0");
        String coin = getSharedPreferences().getString(Constant.COIN, "0");
        CommonUtil.handlerHeadIcon(Integer.valueOf(id), headIcon, name);
        userName.setText(name);

        for (int i = 0; i < rls.length; i++) {
            rls[i].setOnClickListener(listener);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.coin:
                    log("我的论文币");
                    intent = new Intent(getActivity(), WalletActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mine_experience:
                    intent = new Intent(getActivity(), PExperienceActivity.class);
                    startActivity(intent);
                    log("我的分享");
                    break;
                case R.id.mine_answer:
                    intent = new Intent(getActivity(), PQuestionActivity.class);
                    startActivity(intent);
                    log("我的提问");
                    break;
                case R.id.head_ll:
                    intent = new Intent(getActivity(), HPersonInfoActivity.class);
                    startActivity(intent);
                    log("我的个人信息");
                    break;
                case R.id.about_ll:
                    log("我的关于");
                    intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.feedback_ll:
                    log("我的反馈");
                    intent = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting:
                    log("我的设置");
                    Toast.makeText(getActivity(), "你好，您暂无可设置项", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.collection:
                    log("我的收藏");
                    Toast.makeText(getActivity(), "您还没有收藏任何内容", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.youth_ll:
                    log("我的同学录");
                    intent = new Intent(getActivity(), YouthHomeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public String getRequestTag() {
        return HMineFragment.class.getName();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("mine"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("mine");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
}