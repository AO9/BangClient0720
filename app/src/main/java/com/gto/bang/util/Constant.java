package com.gto.bang.util;

/**
 * Created by shenjialong on 16/6/10 13:39.
 */
public class Constant {

    public static final String TYPE_EXPERIENCE="1";
    public static final String TYPE_QUESTION="2";

    public static final String COMMA=",";
    //request code
    public  static  int CALL_COMMENT_ACTIVITY=10000;
//    public static  String URL_BASE="http://192.168.0.102:8080/";
    public static  String URL_BASE="http://lunwenbang.duapp.com/BangBang/";
//    public static  String URL_BASE="http://10.168.46.6:8080/";
    //登录
    public static final String 	 LOGIN_AJAX = "login.ajax";
    //注册
    public static final String 	 REGISTER_AJAX = "register.ajax";
    public static final String 	 FEEDBACK_CREATE_AJAX = "fCreate.ajax";

    public static final String 	 COMMENT_UPDATE_STATUS_URL = "cUpdateStatus.ajax?";
    public static final String 	 MESSAGE_UPDATE_STATUS_URL = "mUpdateStatus.ajax?";

    public static final String 	 BANG_CREATE_AJAX = "bang.ajax";
    public static final String 	 ARTICLE_CREATE_AJAX = "create.ajax";
    public static final String 	 COMMENT_CREATE_AJAX = "cCreate.ajax";
    //获取文章列表 经验｜问答 列表数据
    public static final String 	 ARTICLE_LIST_AJAX = "getArticleList.ajax?";
    //文章详情
    public static final String 	 ARTICLE_DETAIL_AJAX = "getArticleDetail.ajax?";

    public static final String 	 ARTICLE_MYLIST_AJAX = "getMyArticleList.ajax?";
    public static final String 	 COMMENT_MINE_AJAX = "getMyComments.ajax?";
    public static final String 	 COMMENT_UNREAD_NUM_AJAX = "getMyUnReadCommentsNum.ajax?";
    public static final String 	 SYSTEM_MESSAGE_AJAX = "getMessageList.ajax?";

    //评论列表
    public static final String 	 COMMENT_LIST_AJAX = "getCommentList.ajax?";

    public static final String 	 RES_SUCCESS = "1";

    //基本信息配置文件名称
    public static final String   DB="bang";

    //常用字段
    public static final String   STATUS="status";
    public static final String   DATA="data";
    public static final String   USERNAME="username";
    public static final String   PASSWORD="password";
    public static final String   PHONE="phone";
    public static final String   EMAIL="email";
    public static final String   ACADEMY="academy";

    public static final String   ID="id";

    //sharedpreference  配置里存储的字段
    public static final String   LASTACTION="lastAction";

    public static final String TEM="TEM";

    public static final String   AUTHORID_DEFAULT="0";
    public static final String   ARTICLE_TYPE_QUESTION="2";
    public static final String   ARTICLE_TYPE_EXPERIENCE="1";
    public static final String   EMPTY="";
    public static final String   SUBMIT="提交";




    public static final String   REGISTER_ERROR="注册失败，请稍后重试！";
    public static final String   SERVER_ERROR="服务异常，请稍后重试！";
    public static final String   MESSAGE_REQUEST_ERROR="网络请求失败，未能获取到新消息数量！";
    public static final String   REQUEST_ERROR="网络请求失败，请稍后重试！";


}
