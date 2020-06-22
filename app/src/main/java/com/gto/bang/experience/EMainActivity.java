package com.gto.bang.experience;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gto.bang.R;
import com.gto.bang.base.BaseTabActivity;
import com.gto.bang.comment.CommentInputActivity;
import com.gto.bang.experience.fragment.CDetailFragment;
import com.gto.bang.experience.fragment.ECommentFragment;
import com.gto.bang.experience.fragment.EDetailFragment;
import com.gto.bang.question.fragment.QDetailFragment;
import com.gto.bang.util.Constant;
import com.umeng.analytics.MobclickAgent;

/**
 * 经验主页：经验详情＋评论列表
 * update by sjl 2016/07/21 经验和问答 都用这个ACTIVITY
 * 从经验列表跳转到该ACTIVITY时，携带有文章标题和id字段
 * 携带artType 经验、问答、吐槽
 */
public class EMainActivity extends BaseTabActivity {

    String articleId;
    String artTitle;
    Bundle bundle;
    String artType;

    public String getArtType() {
        return artType;
    }

    public void setArtType(String artType) {
        this.artType = artType;
    }

    @Override
    public Context getContext() {
        return EMainActivity.this;
    }

    @Override
    public String getRequestTag() {
        return EMainActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        artType = bundle.get("artType").toString();

        if (Constant.TYPE_EXPERIENCE.equals(artType)) {
            actionBar.setTitle("经验");
        } else if (Constant.TYPE_COMPLAINTS.equals(artType)) {
            actionBar.setTitle("吐槽");
        } else if (Constant.TYPE_QUESTION.equals(artType)) {
            actionBar.setTitle("问答");
        } else if (Constant.TYPE_SUPPORT.equals(artType)) {
            actionBar.setTitle("帮帮");
        }

        //从我的消息功能跳转过来时，需默认显示评论fragment
        if (Constant.MESSAGE.equals(bundle.get("from") == null ? Constant.EMPTY : bundle.get("from").toString())) {
            super.mPager.setCurrentItem(1);
        }
    }

    @Override
    public void init() {

        bundle = getIntent().getExtras();
        Object id = bundle.getString("id");
        Object title = bundle.getString("artTitle");
        articleId = null != id ? id.toString() : null;
        artTitle = null != title ? title.toString() : null;

        this.tabIds = new int[]{R.id.bang_e_section1, R.id.bang_e_section2};
        this.tabNum = 2;
        String type = bundle.get("artType").toString();

        if (Constant.TYPE_EXPERIENCE.equals(type)) {
            this.titles = new String[]{getString(R.string.bang_detail), getString(R.string.bang_comment)};
            this.fragments = new Class[]{EDetailFragment.class, ECommentFragment.class};
        } else if (Constant.TYPE_QUESTION.equals(type)) {
            this.titles = new String[]{getString(R.string.bang_detail), getString(R.string.bang_answer)};
            this.fragments = new Class[]{QDetailFragment.class, ECommentFragment.class};
        } else if (Constant.TYPE_COMPLAINTS.equals(type)) {
            this.titles = new String[]{getString(R.string.bang_detail), getString(R.string.bang_complaint)};
            //增加吐槽类型的页面
            this.fragments = new Class[]{CDetailFragment.class, ECommentFragment.class};
        } else if (Constant.TYPE_SUPPORT.equals(type)) {
            //增加论文帮类型的页面
            this.titles = new String[]{getString(R.string.bang_detail), getString(R.string.bang_comment)};
            this.fragments = new Class[]{QDetailFragment.class, ECommentFragment.class};
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (Constant.TYPE_EXPERIENCE.equals(artType)) {
            getMenuInflater().inflate(R.menu.comment, menu);
        } else if (Constant.TYPE_COMPLAINTS.equals(artType)) {
            getMenuInflater().inflate(R.menu.complaint, menu);
        } else if (Constant.TYPE_QUESTION.equals(artType)) {
            getMenuInflater().inflate(R.menu.answer, menu);
        } else {
            getMenuInflater().inflate(R.menu.comment, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_write_comment:
                //点评的按钮
                intent = new Intent(this, CommentInputActivity.class);

                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.CALL_COMMENT_ACTIVITY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommentInputActivity.SUCCESS) {
            String prompt = "评论成功";
            if (Constant.TYPE_QUESTION.equals(artType)) {
                prompt = Constant.Toast_Question;
            }
            Toast.makeText(this, prompt, Toast.LENGTH_LONG).show();
        }
    }

    public String getArtTitle() {
        return artTitle;
    }

    public String getArticleId() {
        return articleId;
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
}
