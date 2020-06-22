package com.gto.bang.home;

import android.content.Context;

public class ArticleDetailActivity extends BaseDetailActivity {
    @Override
    public Context getContext() {
        return ArticleDetailActivity.this;
    }

    @Override
    public String getRequestTag() {
        return ArticleDetailActivity.class.getName();
    }

//    String articleId;
//
//    public String getArticleId() {
//        return articleId;
//    }
//
//    public void setArticleId(String articleId) {
//        this.articleId = articleId;
//    }
//
//    public String getArtTitle() {
//        return artTitle;
//    }
//
//    public void setArtTitle(String artTitle) {
//        this.artTitle = artTitle;
//    }
//
//    String artTitle;
//    Bundle bundle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        initViews();
//        init();
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.article_detail_4activity);
//    }
//
//
//    public void init() {
//
//        bundle = getIntent().getExtras();
//        String id = bundle.getString("id");
//        String title = bundle.getString("artTitle");
//        setArticleId(id);
//        setArtTitle(title);
//
//    }
//
//    public void initViews() {
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.comment, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent;
//        switch (item.getItemId()) {
//            case R.id.action_write_comment:
//                intent = new Intent(this, CommentInputActivity.class);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, Constant.CALL_COMMENT_ACTIVITY);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == CommentInputActivity.SUCCESS) {
//            String prompt = "评论成功";
//            Toast.makeText(this, prompt, Toast.LENGTH_LONG).show();
//        }
//    }


}
