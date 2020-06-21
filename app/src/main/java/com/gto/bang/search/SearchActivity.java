package com.gto.bang.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 20/6/20 13:07.
 *
 * @// TODO: 20/6/20  待实现 网络请求暂未完成
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

            }
        });

    }


    public void search(int pageNum, SearchResponseListener responseListener) {

        String userId = getUserId();
        String url = Constant.URL_BASE + Constant.ARTICLE_LIST_AJAX + "type=" + Constant.TYPE_ARTICLE + "&" +
                Constant.PAGENUM + "=" + pageNum + "&userId=" + userId + "&articleType=" + Constant.ARTICLETYPE_HOT;
        CustomRequest req = new CustomRequest(getContext(), responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(TAG);
        VolleyUtils.getRequestQueue(getContext()).add(req);
    }


    public class SearchResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
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
                t = Toast.makeText(SearchActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                List<Map<String, Object>> datas = null;
                if (CollectionUtils.isNotEmpty(datas)) {
                    ListView listView = (ListView) findViewById(R.id.historyWdListView);
                    listView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < datas.size(); i++) {
                        Map<String, Object> map = datas.get(i);
                        map.put("createTime", null == map.get("createTime") ? null : map.get("createTime").toString().substring(0, 10));
                    }
                    SearchAdapter adapter = new SearchAdapter(getContext(), datas);
                    listView.setAdapter(adapter);
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
                convertView = mInflater.inflate(R.layout.hacticle_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.viewTimes = (TextView) convertView.findViewById(R.id.viewTimes);
                holder.commentNum = (TextView) convertView.findViewById(R.id.commentNum);
                holder.praise = (TextView) convertView.findViewById(R.id.praise);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(datas.get(position).get(Constant.TITLE).toString());
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());
            holder.viewTimes.setText(datas.get(position).get(Constant.VIEWTIMES).toString());

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
