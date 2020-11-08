package com.gto.bang.campus;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;

import org.apache.commons.collections4.CollectionUtils;

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

    TextView workFlag;
    TextView emotionFlag;
    TextView lifeFlag;
    TextView studyFlag;

    ListView listView;

    List<Map<String, Object>> datas;


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
        initDatas(1, new ResponseListener(), Constant.TYPE_CP_STUDY);
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
                        intent = new Intent(getContext(), CreateActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), CreateActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getContext(), CreateActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });

        work = (TextView) findViewById(R.id.cp_work);
        study = (TextView) findViewById(R.id.cp_study);
        emotion = (TextView) findViewById(R.id.cp_emotion);
        life = (TextView) findViewById(R.id.cp_life);

        workFlag = (TextView) findViewById(R.id.cp_work_flag);
        studyFlag = (TextView) findViewById(R.id.cp_study_flag);
        emotionFlag = (TextView) findViewById(R.id.cp_emotion_flag);
        lifeFlag = (TextView) findViewById(R.id.cp_life_flag);

        tips = (TextView) findViewById(R.id.tips);

        listView = (ListView) findViewById(R.id.listView);

        View.OnClickListener tvOCL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = view.getId();
                switch (id) {
                    case R.id.cp_work:
                        tabSelect(0);
                        log("校内帮-工作列表");
                        initDatas(1, new ResponseListener(), Constant.TYPE_CP_WORK);
                        break;
                    case R.id.cp_study:
                        tabSelect(1);
                        log("校内帮-学习列表");
                        initDatas(1, new ResponseListener(), Constant.TYPE_CP_STUDY);
                        break;
                    case R.id.cp_emotion:
                        tabSelect(2);
                        log("校内帮-情感列表");
                        initDatas(1, new ResponseListener(), Constant.TYPE_CP_EMOTION);
                        break;
                    case R.id.cp_life:
                        tabSelect(3);
                        log("校内帮-生活列表");
                        initDatas(1, new ResponseListener(), Constant.TYPE_CP_LIFE);
                        break;
                    default:
                        log("校内帮-默认列表");
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

    public void tabSelect(int num) {

        switch (num) {
            case 0:
                workFlag.setVisibility(View.VISIBLE);
                studyFlag.setVisibility(View.GONE);
                emotionFlag.setVisibility(View.GONE);
                lifeFlag.setVisibility(View.GONE);

                work.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.blue_light)));
                study.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                emotion.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                life.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 1:
                workFlag.setVisibility(View.GONE);
                studyFlag.setVisibility(View.VISIBLE);
                emotionFlag.setVisibility(View.GONE);
                lifeFlag.setVisibility(View.GONE);
                work.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                study.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.blue_light)));
                emotion.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                life.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 2:
                workFlag.setVisibility(View.GONE);
                studyFlag.setVisibility(View.GONE);
                emotionFlag.setVisibility(View.VISIBLE);
                lifeFlag.setVisibility(View.GONE);
                work.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                study.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                emotion.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.blue_light)));
                life.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                break;
            case 3:
                workFlag.setVisibility(View.GONE);
                studyFlag.setVisibility(View.GONE);
                emotionFlag.setVisibility(View.GONE);
                lifeFlag.setVisibility(View.VISIBLE);
                work.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                study.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                emotion.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                life.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.blue_light)));
                break;
            default:
                break;


        }

    }

    /**
     * server请求数据
     *
     * @param pageNum
     * @param responseListener
     */
    public void initDatas(int pageNum, ResponseListener responseListener, String type) {

        listView.setAdapter(null);
        TextView tips = (TextView) findViewById(R.id.tips);
        tips.setVisibility(View.VISIBLE);

        String url = Constant.URL_BASE + Constant.ARTICLE_LIST + "type=" + type + "&" +
                Constant.PAGENUM + "=" + pageNum;
        CommonUtil.localLog("campus url=" + url);
        CustomRequest req = new CustomRequest(getContext(), responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getContext()).add(req);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getContext(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
                t = Toast.makeText(getContext(), data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                datas = RequestUtil.parseResponseForDatas(res);
                if (CollectionUtils.isNotEmpty(datas)) {
                    TextView tips = (TextView) findViewById(R.id.tips);
                    tips.setVisibility(View.GONE);
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setVisibility(View.VISIBLE);
                    MyAdapter adapter = new MyAdapter(getContext(), datas);
                    listView.setAdapter(adapter);
                }
            }

        }
    }


    private class MyAdapter extends BaseAdapter {

        List<Map<String, Object>> datas;

        private LayoutInflater mInflater;

        public MyAdapter(Context context, List<Map<String, Object>> datas) {
            this.datas = datas;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.cacticle_item, null);
                holder = new ViewHolder();
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.signUpNum = (TextView) convertView.findViewById(R.id.signUpNum);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());
            holder.price.setText(datas.get(position).get(Constant.PRICE).toString());
            return convertView;
        }
    }

    public final class ViewHolder {
        public TextView content;
        public TextView price;
        //待实现 1108
        public TextView signUpNum;
        public TextView status;
    }


}
