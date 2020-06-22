package com.gto.bang.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.base.ResponseListener;
import com.gto.bang.home.ArticleDetailActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 20/6/20 13:07.
 *
 * @// TODO: 20/6/20  查询的后台接口未实现
 */
public class SearchActivity extends BaseActivity {

    public static final String TAG = "SearchActivity";
    EditText searchEditText;
    ImageButton searchButton;

    @Override
    public Context getContext() {
        return SearchActivity.this;
    }

    /**
     * @param keyword
     * @// TODO: 20/6/22 历史记录多较多的时候需要额外处理机制
     */
    public void recordKeyword(String keyword) {
        String value = readFromLocal(Constant.KEYWORD);
        if (StringUtils.isBlank(value)) {
            writeToLocal(Constant.KEYWORD, keyword);
        } else {
            List<String> list = CommonUtil.convert(value);
            // 先判断是否已经写入过这个关键词
            if (!list.contains(keyword)){
                writeToLocal(Constant.KEYWORD, value + Constant.SEPERETOR_COMMA + keyword);
            }
        }
    }


    /**
     * 搜索历史记录初始化
     */
    public void intiSearchHistory() {
        String value = readFromLocal(Constant.KEYWORD);
        if (StringUtils.isBlank(value)) {
            return;
        }
        final String[] keywordArray = value.split(Constant.COMMA);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, keywordArray);
        ListView history = findViewById(R.id.historyWdListView);
        history.setAdapter(adapter);
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String keyword = keywordArray[i];
                search(keyword);
            }
        });

    }

    /**
     * 向服务器发起搜索查询
     * 20200622
     *
     * @param keyword
     */
    public void search(String keyword) {
        Map<String, String> param = new HashMap<String, String>();
        CommonUtil.localLog("step 2" + keyword);
        param.put(Constant.KEYWORD, keyword);
        RequestUtil.request(Constant.QUERY_ARTICLE_URL, param, new SearchResponseListener(), TAG, SearchActivity.this);
        TextView textView = findViewById(R.id.tips);
        textView.setText("搜索加载中..");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = searchEditText.getText().toString();
                CommonUtil.localLog("step 1");
                if (StringUtils.isBlank(keyword)) {
                    CommonUtil.localLog("step 1.1");
                    CommonUtil.showTips("查询内容不能为空", SearchActivity.this);
                    return;
                }
                search(keyword);
                recordKeyword(keyword);
            }
        });

        TextView clearHistory = findViewById(R.id.cleanHistory);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToLocal(Constant.KEYWORD, Constant.EMPTY);
                ListView history = (ListView) findViewById(R.id.historyWdListView);
                history.setAdapter(null);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, keywordArray);
//                ListView history = findViewById(R.id.historyWdListView);
//                history.setAdapter(adapter);
                CommonUtil.showTips("已清空",SearchActivity.this);
            }
        });

        intiSearchHistory();
    }


    public class SearchResponseListener extends ResponseListener {

        @Override
        public void onErrorResponse(VolleyError arg0) {
            CommonUtil.showTips(Constant.REQUEST_ERROR,getContext());
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                CommonUtil.showTips(Constant.SERVER_ERROR,getContext());

            } else {
                final List<Map<String, Object>> datas = RequestUtil.parseResponseForDatas(res);
                if (CollectionUtils.isNotEmpty(datas)) {

                    LinearLayout searchResultLL = (LinearLayout) findViewById(R.id.searchResultLL);
                    searchResultLL.setVisibility(View.VISIBLE);
                    ListView searchResultListView = (ListView) findViewById(R.id.searchResultListView);

                    LinearLayout historyLL = (LinearLayout) findViewById(R.id.historyLL);
                    LinearLayout tipsLl = (LinearLayout) findViewById(R.id.tipsLl);
                    tipsLl.setVisibility(View.GONE);
                    historyLL.setVisibility(View.GONE);

                    CommonUtil.localLog("step 3");

                    SearchAdapter adapter = new SearchAdapter(getContext(), datas);
                    searchResultListView.setAdapter(adapter);
                    searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Intent intent = new Intent(SearchActivity.this, ArticleDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.ID, datas.get(position).get(Constant.ID).toString());
                            bundle.putString(Constant.ARTTITLE, datas.get(position).get(Constant.TITLE).toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    LinearLayout tipsLl = (LinearLayout) findViewById(R.id.tipsLl);
                    tipsLl.setVisibility(View.VISIBLE);

                    TextView textView = findViewById(R.id.tips);
                    textView.setText("抱歉，未找到您搜索的内容");

                    LinearLayout historyLL = (LinearLayout) findViewById(R.id.historyLL);
                    historyLL.setVisibility(View.VISIBLE);
                    LinearLayout searchResultLL = (LinearLayout) findViewById(R.id.searchResultLL);
                    searchResultLL.setVisibility(View.GONE);
                }

            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索页"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索页");
    }


    private class SearchAdapter extends BaseAdapter {

        List<Map<String, Object>> datas;

        private LayoutInflater mInflater;

        public SearchAdapter(Context context, List<Map<String, Object>> datas) {
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
                convertView = mInflater.inflate(R.layout.search_list_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CommonUtil.localLog("step 4");
            String[] feilds = new String[]{Constant.TITLE, Constant.CONTENT};
            TextView[] tvs = new TextView[]{holder.title, holder.content};
            for (int i = 0; i < feilds.length; i++) {

                Object value = datas.get(position).get(feilds[i]);
                if (value == null) {
                    tvs[i].setVisibility(View.GONE);
                } else {
                    tvs[i].setText(value.toString());
                }
            }
            CommonUtil.localLog("step 5");
            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView title;
        public TextView author;
        public TextView content;
        public TextView headIcon;
        public TextView viewTimes;
        public TextView commentNum;
        public TextView praise;
    }

}
