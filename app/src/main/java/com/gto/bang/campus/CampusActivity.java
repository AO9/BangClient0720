package com.gto.bang.campus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 20/10/24 21:02.
 */
public class CampusActivity extends BaseActivity {

    GridView gridView;
    TextView work;
    TextView emotion;
    TextView life;
    TextView study;
    TextView tips;

    @Override
    public Context getContext() {
        return CampusActivity.this;
    }

    @Override
    public String getRequestTag() {
        return CampusActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus);
        initView();
    }

    private void initView() {

        gridView = (GridView) findViewById(R.id.gridview);
        String name[] = {"学长帮帮", "学姐帮帮", "校外求助"};
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", name[i]);
            dataList.add(map);
        }
        String[] from = {"text"};
        int[] to = {R.id.text};
        SimpleAdapter adapter = new SimpleAdapter(getContext(), dataList, R.layout.cp_gridview_item, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent;
                switch (arg2) {
                    case 0:
                        CommonUtil.showTips("学长", getContext());
                        intent = new Intent(getContext(), CreateActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        CommonUtil.showTips("学姐", getContext());
                        intent = new Intent(getContext(), CreateActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        CommonUtil.showTips("校外求助", getContext());
                        intent = new Intent(getContext(), CreateActivity.class);
                        startActivity(intent);
                        break;
//                    case 3:
//                        CommonUtil.showTips("同学录", getContext());
//                        intent = new Intent(getContext(), CreateActivity.class);
//                        startActivity(intent);
//                        break;

                    default:
                        break;
                }
            }
        });

        work = (TextView) findViewById(R.id.cp_work);
        study = (TextView) findViewById(R.id.cp_study);
        emotion = (TextView) findViewById(R.id.cp_emotion);
        life = (TextView) findViewById(R.id.cp_life);
        tips = (TextView) findViewById(R.id.tips);


        View.OnClickListener tvOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = view.getId();
                switch (id) {
                    case R.id.cp_work:
                        CommonUtil.showTips("工作", getContext());
                        break;
                    case R.id.cp_study:
                        CommonUtil.showTips("学习", getContext());
                        break;
                    case R.id.cp_emotion:
                        CommonUtil.showTips("情感", getContext());
                        break;
                    case R.id.cp_life:
                        CommonUtil.showTips("生活", getContext());
                        break;
                    default:
                        CommonUtil.showTips("默认", getContext());
                        break;
                }

            }
        };
        work.setOnClickListener(tvOCL);
        study.setOnClickListener(tvOCL);
        emotion.setOnClickListener(tvOCL);
        life.setOnClickListener(tvOCL);

    }


}
