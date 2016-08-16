package com.gto.bang.create.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.gto.bang.R;
import com.gto.bang.base.BaseInputFragment;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * Created by shenjialong on 16/6/10 02:06.
 * 新建 问答  update 2016-07-14
 */
public class QCreateFragment extends BaseInputFragment {


    TextView question_theme;
    TextView question_describe;
    Button question_save;
    TextView [] textViews;
    String [] inputHints=new String[]{"请填写问答题目","请填写问答内容"};


    public QCreateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bang_question, container, false);
        return rootView;
    }

    @Override
    public void initViews(){

        View rootView=getView();
        question_describe=(TextView)rootView.findViewById(R.id.question_describe_et);
        question_theme=(TextView)rootView.findViewById(R.id.question_theme_et);
        textViews=new TextView[]{question_theme,question_describe};

        question_save=(Button) rootView.findViewById(R.id.question_ok_btn);

        question_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isFlag()){
                    Log.i("sjl","开始校验");
                    if(check()){
                        Log.i("sjl","校验通过");
                        //校验通过后拼接请求参数并向服务器发送请求
                        HashMap<String, String> params=new HashMap<String, String>();
                        params.put("title",question_theme.getText().toString());
                        params.put("content",question_describe.getText().toString());
                        params.put("type",Constant.ARTICLE_TYPE_QUESTION);


                        Log.i("sjl","sjlsjl"+getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));

                        params.put("authorid",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
                        publish(params);


                    }else{
                        Log.i("sjl","校验失败");
                        // 可以继续点击了
                        setFlag(true);
                    }
                }
            }
        });
    }

    /**
     * 发布
     */
    public void publish(HashMap<String, String> params) {
        Toast t = Toast.makeText(getActivity(), "正在发布问题...", Toast.LENGTH_SHORT);
        t.show();

        ResponseListener listener = new ResponseListener();
        String url=Constant.URL_BASE+ Constant.ARTICLE_CREATE_AJAX;
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,params,url, Request.Method.POST);
        Log.i("sjl","发布经验url:"+ url);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    /**
     * 字段非空校验
     * @return
     */
    private boolean check(){
        Log.i("sjl","begin check");
        for(int i=0;i<textViews.length;i++){
            if(null==textViews[i].getText()|| StringUtils.isEmpty(textViews[i].getText().toString())){
                Toast t = Toast.makeText(getActivity(), inputHints[i], Toast.LENGTH_SHORT);
                t.show();
                setFlag(true);
                return false;
            }
        }
        return true;
    }

    @Override
    protected String getRequestTag(){
        return "QCreateTag";
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("创建－问答"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("创建－问答");
    }
}
