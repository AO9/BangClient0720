package com.gto.bang.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.navigation.AboutActivity;
import com.gto.bang.navigation.FeedbackActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 20190217
 * 20190603 拆分问答为 纯问答+红包问答两个专区
 *
 */
public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, ActionBar.OnNavigationListener {

    public static final String TAB_HOMEPAGE = "TAB_HOMEPAGE";
    public static final String TAB_QUESTION = "TAB_QUESTION";
    public static final String TAB_DISCOVERY = "TAB_DISCOVERY";
    public static final String TAB_MESSAGE = "TAB_MESSAGE";
    public static final String TAB_MINE = "TAB_MINE";
    public static final int TAB_INDEX_MESSAGE = 3;

    public static final Set<Integer> set = new HashSet<Integer>();

    static {
        set.add(4);
        set.add(5);
        set.add(6);
    }

    private Menu menus;
    private java.lang.reflect.Method mMethodSetShowHideAnimationEnabled;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = super.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setHomeButtonEnabled(false);

        setContentView(R.layout.tabs);
        LayoutInflater inflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabContent);
        mTabHost.setOnTabChangedListener(this);
        addTab(inflater, TAB_HOMEPAGE, HomePageTagFragment.class, R.drawable.home, R.string.tab_homepage);
        addTab(inflater, TAB_DISCOVERY, DiscoveryFragment.class, R.drawable.discover, R.string.tab_discovery);
        addTab(inflater, TAB_QUESTION, HQuestionFragment.class, R.drawable.question, R.string.tab_question);
        addTab(inflater, TAB_MESSAGE, HMessageFragment.class, R.drawable.message, R.string.tab_message);
        addTab(inflater, TAB_MINE, HMineFragment.class, R.drawable.mine, R.string.tab_mine);
        mTabHost.getTabWidget().setDividerDrawable(null);

        String info = getSharedPreferences().getString(Constant.INFO, "");
        if (StringUtils.isNotBlank(info)) {
            CommonUtil.showDialog(MainActivity.this, info, "提示", "我知道了");
        }
    }

    public void addTab(LayoutInflater inflater, String tag, Class clss, int icon, int title) {
        View indicator = inflater.inflate(R.layout.tab_item,
                mTabHost.getTabWidget(), false);
        ImageView imgView = (ImageView) indicator.findViewById(R.id.tabIcon);
        TextView titleView = (TextView) indicator.findViewById(R.id.tabTitle);
        imgView.setImageResource(icon);
        titleView.setText(title);
        mTabHost.addTab(mTabHost.newTabSpec(tag).setIndicator(indicator), clss,
                null);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        String currentTag = mTabHost.getCurrentTabTag();
        if (TextUtils.isEmpty(currentTag)) {
            return true;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentTag);
        if (fragment instanceof ActionBar.OnNavigationListener) {
            ((ActionBar.OnNavigationListener) fragment).onNavigationItemSelected(i, l);
        }
        return true;
    }

    @Override
    public void onTabChanged(String tabId) {

        disabledActionBarShowHideAnimation();
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        if (TAB_HOMEPAGE.equals(tabId)) {
            getActionBar().setTitle(R.string.tab_homepage);
        } else if (TAB_DISCOVERY.equals(tabId)) {
            getActionBar().setTitle(R.string.tab_discovery);
        } else if (TAB_MESSAGE.equals(tabId)) {
            getActionBar().setTitle(R.string.tab_message);
            View view = mTabHost.getTabWidget().getChildAt(1);
            ((TextView) view.findViewById(R.id.tabNum)).setText("");
        } else if (TAB_MINE.equals(tabId)) {
            getActionBar().setTitle(R.string.tab_mine);
        }else if (TAB_QUESTION.equals(tabId)) {
            getActionBar().setTitle(R.string.tab_question);
        }
        //不重绘 否则menu不能隐藏
//        invalidateOptionsMenu();
    }

    /**
     * 禁止action bar动画
     */
    private void disabledActionBarShowHideAnimation() {
        try {
            if (mMethodSetShowHideAnimationEnabled == null) {
                mMethodSetShowHideAnimationEnabled = getActionBar().getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class);
            }
            mMethodSetShowHideAnimationEnabled.invoke(getActionBar(), false);
        } catch (Exception exception) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_logout:
                SharedPreferences.Editor editor = getEditor();
                editor.putString("lastAction", "logout");
                MobclickAgent.onProfileSignOff();
                editor.commit();
                finish();
                return true;
            case R.id.action_introduce:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menus = menu;
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUnReadNumOfSystemMessage();
        MobclickAgent.onResume(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initUnReadNumOfSystemMessage() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.NUM_OF_UNREAD_URL + "userId=" + getSharedPreferences().getString(Constant.ID, "");
        CustomRequest req = new CustomRequest(this, listener, listener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }


    private String getRequestTag() {
        return "MainActivity_request";
    }


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        ResponseListener() {
        }

        @Override
        public void onErrorResponse(VolleyError arg0) {
            return;
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                return;
            } else {
                Object data = res.get("data");
                if (null != data) {
                    String num = data.toString();
                    if (null != num && !"0".equals(num)) {
                        View view = mTabHost.getTabWidget().getChildAt(TAB_INDEX_MESSAGE);
                        ((TextView) view.findViewById(R.id.tabNum)).setText("+" + num);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }


}
