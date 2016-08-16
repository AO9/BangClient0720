package com.gto.bang.question;

import com.gto.bang.base.BaseTabActivity;

/**
 * Created by shenjialong on 16/6/10 00:48.
 * update  by sjl 16/7/21 23:09 QCommentFragment 更改为 ECommentFragment，通用一个暂时
 */
public class QMainActivity extends BaseTabActivity{

//    public  static  int COMMENT=10000;
//    String articleId;
//    String artTitle;
//    Bundle bundle;
//
//    @Override
//    public void init(){
//        this.titles = new String[]{getString(R.string.bang_question), getString(R.string.bang_comment)};
//        this.tabIds=new int[]{R.id.bang_e_section1,R.id.bang_e_section2};
//        this.fragments=new Class[]{ QDetailFragment.class,ECommentFragment.class};
//        this.tabNum=2;
//        Object id=getIntent().getExtras().getString("id");
//        Object title=getIntent().getExtras().getString("artTitle");
//        articleId=null!=id?id.toString():null;
//        artTitle=null!=articleId?articleId.toString():null;
//        bundle=getIntent().getExtras();
//    }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
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
//                //点评的按钮
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
//        if (resultCode==CommentInputActivity.SUCCESS){
//            //reload listview data from server
//            Toast.makeText(this,"评论成功",Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public String getArtTitle() {
//        return artTitle;
//    }
//
//    public String getArticleId() {
//        return articleId;
//    }
}
