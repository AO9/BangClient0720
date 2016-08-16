package com.gto.bang.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseTabActivity;
import com.gto.bang.create.CreateMainActivity;
import com.gto.bang.navigation.AboutActivity;
import com.gto.bang.navigation.FeedbackActivity;
import com.gto.bang.personal.activity.PMainActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 主页：经验＋问答＋帮帮
 * Created by shenjialong on 16/6/10 14:43.
 */
public class HMainActivity extends BaseTabActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void init(){
        this.titles = new String[]{getString(R.string.title_section1),getString(R.string.title_section2),getString(R.string.title_section3) };
        this.tabIds=new int[]{R.id.bang_e_section1,R.id.bang_e_section2,R.id.bang_e_section3};
        this.fragments=new Class[]{ HExperienceFragment.class,HQuestionFragment.class,HBangbangFragment.class};
        this.tabNum=3;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        Toast t = Toast.makeText(HMainActivity.this, "sorry forbidden", Toast.LENGTH_SHORT);
        switch (id) {
            case R.id.action_logout:
                SharedPreferences.Editor editor=getEditor();
                editor.putString("lastAction","logout");
                MobclickAgent.onProfileSignOff();
                editor.commit();
                finish();
                return true;
            case R.id.action_personal:
                intent = new Intent(this, PMainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_experience:
                intent = new Intent(this, CreateMainActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
