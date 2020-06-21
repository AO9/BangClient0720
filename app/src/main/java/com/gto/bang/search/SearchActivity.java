package com.gto.bang.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wd = searchEditText.getText().toString();
                if (StringUtils.isBlank(wd)) {
                    return;
                }
                Map<String, String> param = new HashMap<String, String>();
                param.put(Constant.SEARCH_WORD, wd);
                CommonUtil.request(Constant.QUERY_ARTICLE_URL, param, new SearchResponseListener(), TAG, SearchActivity.this);
            }
        });
    }


    public class SearchResponseListener extends ResponseListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getContext(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                String data = (null == res.get(Constant.DATA)) ? "null" : res.get(Constant.DATA).toString();
                t = Toast.makeText(SearchActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                final List<Map<String, Object>> datas = null;
                if (CollectionUtils.isNotEmpty(datas)) {
                    ListView listView = (ListView) findViewById(R.id.recommendWdListView);
                    listView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < datas.size(); i++) {
                        Map<String, Object> map = datas.get(i);
                        map.put(Constant.CREATETIME, null == map.get(Constant.CREATETIME) ? null : map.get(Constant.CREATETIME).toString().substring(0, 10));
                    }
                    SearchAdapter adapter = new SearchAdapter(getContext(), datas);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

            String[] feilds = new String[]{Constant.TITLE, Constant.CONTENT};
            for (int i = 0; i < feilds.length; i++) {

            }

            holder.title.setText(datas.get(position).get(Constant.TITLE).toString());
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());

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
