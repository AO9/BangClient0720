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
    private LinearLayout school;

    private TextView headIcon;
    private TextView userName;

    LinearLayout[] rls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.personal_main, container, false);
        return rootView;
    }

    public void initViews(){
        experience=(LinearLayout)getView().findViewById(R.id.mine_experience);
        coin=(LinearLayout)getView().findViewById(R.id.coin);
        question=(LinearLayout)getView().findViewById(R.id.mine_answer);
        head=(LinearLayout)getView().findViewById(R.id.head_ll);
        feedback=(LinearLayout)getView().findViewById(R.id.feedback_ll);

        collection=(LinearLayout)getView().findViewById(R.id.collection);
        setting=(LinearLayout)getView().findViewById(R.id.setting);
        about=(LinearLayout)getView().findViewById(R.id.about_ll);
        school=(LinearLayout)getView().findViewById(R.id.school_ll);

        headIcon=(TextView)getView().findViewById(R.id.headIcon);
        userName=(TextView)getView().findViewById(R.id.userName);
        rls=new LinearLayout[]{question,experience,head,feedback,about,collection,setting,coin,school};

        String name=getSharedPreferences().getString(Constant.USERNAME_V1,Constant.EMPTY);
        String id=getSharedPreferences().getString(Constant.ID,"0");
        String coin=getSharedPreferences().getString(Constant.COIN,"0");
        CommonUtil.handlerHeadIcon(Integer.valueOf(id),headIcon,name);
        userName.setText(name);

        for(int i=0;i<rls.length;i++){
            rls[i].setOnClickListener(listener);
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.coin:
                    log("我的论文币");
                    intent =new Intent(getActivity(),WalletActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mine_experience:
                    intent =new Intent(getActivity(),PExperienceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mine_answer:
                    intent =new Intent(getActivity(),PQuestionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.head_ll:
                    intent =new Intent(getActivity(),HPersonInfoActivity.class);
                    startActivity(intent);
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
                    Toast.makeText(getActivity(),"你好，您暂无可设置项",Toast.LENGTH_SHORT).show();
                case R.id.collection:
                    log("我的收藏");
                    Toast.makeText(getActivity(),"您还没有收藏任何内容",Toast.LENGTH_SHORT).show();

                case R.id.school_ll:
                    log("我的同学录");
                    Toast.makeText(getActivity(),"云同学录功能，永久保存你的校园回忆",Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }
    };

    @Override
    public String getRequestTag(){
        return "HMineFragment_request";
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