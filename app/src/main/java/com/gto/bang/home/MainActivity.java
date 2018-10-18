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
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.create.CreateComplaintActivity;
import com.gto.bang.create.CreateExperienceActivity;
import com.gto.bang.create.CreateQuestionActivity;
import com.gto.bang.navigation.AboutActivity;
import com.gto.bang.navigation.FeedbackActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shenjialong on 16/10/24 22:45.
 * 1105日最新的主页入口类hs
 */
public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, ActionBar.OnNavigationListener {

    public static final String TAB_INTERACTION = "TAB_INTERACTION";
    public static final String TAB_ONE = "TAB_ONE";
    public static final String TAB_MESSAGE = "TAB_MESSAGE";
    public static final String TAB_MINE = "TAB_MINE";
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
        addTab(inflater, TAB_INTERACTION, HInteractionFragment.class, R.drawable.icon, R.string.tab_interaction);
        addTab(inflater, TAB_ONE, HDailyFragment.class, R.drawable.icon, R.string.tab_daily);
//        addTab(inflater,TAB_MESSAGE,  HMessageOldFragment.class,R.drawable.icon, R.string.tab_message);
        addTab(inflater, TAB_MESSAGE, HMessageFragment.class, R.drawable.icon, R.string.tab_message);
        addTab(inflater, TAB_MINE, HMineFragment.class, R.drawable.icon, R.string.tab_mine);
        mTabHost.getTabWidget().setDividerDrawable(null);

        String info = getSharedPreferences().getString(Constant.INFO, "");
        if (StringUtils.isNotBlank(info)) {
            CommonUtil.showDialog(MainActivity.this, info, "提示", "我知道了");
        }
    }

    public void addTab(LayoutInflater inflater, String tag, Class clss, int icon, int title) {
        View indicator = inflater.inflate(R.layout.tab_item,
                mTabHost.getTabWidget(), false);
//        ImageView imgView = (ImageView) indicator.findViewById(R.id.tabIcon);
        TextView titleView = (TextView) indicator.findViewById(R.id.tabTitle);
        //预留TAB图标
//        imgView.setImageResource(icon);
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
        if (TAB_INTERACTION.equals(tabId)) {
            List<Integer> list = new ArrayList<Integer>();
            list.addAll(set);
            list.add(0);
            list.add(1);
            list.add(2);
            changeMenus(list);
//            getActionBar().show();
            getActionBar().setTitle(R.string.tab_interaction);
        } else if (TAB_ONE.equals(tabId)) {
            List<Integer> list = new ArrayList<Integer>();
            list.add(3);
            list.addAll(set);
            getActionBar().setTitle(R.string.one_title);
            changeMenus(list);
        } else if (TAB_MESSAGE.equals(tabId)) {
            List<Integer> list = new ArrayList<Integer>();
            list.addAll(set);
            getActionBar().setTitle(R.string.tab_message);
            changeMenus(list);
            View view = mTabHost.getTabWidget().getChildAt(1);
            ((TextView) view.findViewById(R.id.tabNum)).setText("");
        } else if (TAB_MINE.equals(tabId)) {
            List<Integer> list = new ArrayList<Integer>();
            list.addAll(set);
            getActionBar().setTitle(R.string.tab_mine);
            changeMenus(list);
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
            case R.id.create_complaint:
                intent = new Intent(this, CreateComplaintActivity.class);
                startActivity(intent);
                return true;
            case R.id.create_question:
                intent = new Intent(this, CreateQuestionActivity.class);
                startActivity(intent);
                return true;
            case R.id.create_experience:
                intent = new Intent(this, CreateExperienceActivity.class);
                startActivity(intent);
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


    private void changeMenus(List<Integer> list) {
        if (null != menus) {
            for (int i = 0; i < menus.size(); i++) {
                if (list.contains(i)) {
                    menus.getItem(i).setVisible(true);
                } else {
                    menus.getItem(i).setVisible(false);
                }
            }
        }
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
                        View view = mTabHost.getTabWidget().getChildAt(2);
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
