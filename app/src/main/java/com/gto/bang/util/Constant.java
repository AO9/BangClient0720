package com.gto.bang.util;

/**
 * Created by shenjialong on 16/6/10 13:39.
 */
public class Constant {

    public static String MESSAGE = "message";
    public static final String TYPE_EXPERIENCE = "1";
    public static final String TYPE_ARTICLE = "5";
    public static final String TYPE_QUESTION = "2";
    public static final String TYPE_COMPLAINTS = "3";

    public static final String PAGENUM= "pageNum";
    public static final String ANDROID_ID= "androidId";
    public static final String ARTICLEID= "articleId";
    public static final int ARTICLETYPE_HOT= 1;
    public static final String ARTICLETYPE_RECCOMMEND= "1";
    public static final String ARTICLETYPE_NEW= "2";


    //红包问答
    public static final String TYPE_SUPPORT = "4";
    public static final String Toast_Experience = "经验值 ＋5";
    public static final String Toast_Question = "经验值 ＋2";

    public static final String COMMA = ",";
    //request code
    public static final int PINFO_UPDATE_NAME = 1001;
    public static final int PINFO_UPDATE_GENDER = 1002;
    public static final int PINFO_UPDATE_REGINON = 1003;
    public static final int PINFO_UPDATE_SIGNATURE = 1004;
    public static final int PINFO_UPDATE_ACADAMY = 1005;

    public static int CALL_COMMENT_ACTIVITY = 1000;
    public static String URL_BASE = "http://www.ababy.world/bangbang/";
    public static String SUBMMITING = "提交中";
    public static String SUBMMITTED = "已提交";

    public static final String URL_SEPARATOR = "?";
    public static final String URL_PARAM_SEPARATOR = "&";
    public static final String URL_EQUAL = "=";

    // 评论点赞 关注 (收藏)
    public static final String QUESTION_PRAISE_URL = "v2/comment/praise";
    public static final String QUESTION_COLLECTION_URL = "v2/comment/collection";

    public static final String LOGIN_URL = "/v1/user/login";
    public static final String REGISTER_URL = "/v1/user/register";
    public static final String STATEMENT_URL = "/statement/view";
    public static final String NOTICE_URL = "v1/notice/view?";

    public static final String FEEDBACK_CREATE_AJAX = "fCreate.ajax";

    public static final String COMMENT_UPDATE_STATUS_URL = "cUpdateStatus.ajax?";
    public static final String MESSAGE_UPDATE_STATUS_URL = "mUpdateStatus.ajax?";

    public static final String BANG_CREATE_AJAX = "bang.ajax";

    public static final String ACTICLE_VIEW_URL = "acticle/view";
    public static final String PRODUCT_CREATE_URL = "product/create";
    public static final String PRODUCT_LIST_URL = "product/list";
    public static final String PRODUCT_VIEW_URL = "product/view";
    public static final String PRODUCT_PRAISE_URL = "product/praise";

    public static final String ARTICLE_CREATE_AJAX = "v2/article/create";
    public static final String COMMENT_CREATE_AJAX = "cCreate.ajax";
    public static final String UPDATE_PERSION_INFO_AJAX = "user/update.ajax";
    //获取文章列表 经验｜问答 列表数据
//    public static final String ARTICLE_LIST_AJAX = "getArticleList.ajax?";
    // 0619改版 使用新接口
    public static final String ARTICLE_LIST_AJAX = "v1/article/list?pageSize=10&";

    public static final String QUERY_ARTICLE_URL = "v2/article/search?";


    public static final String SALON_VIEW_URL = "salon/view?";
    //文章详情
    public static final String ARTICLE_DETAIL_AJAX = "getArticleDetail.ajax?";


    public static final String USER_LIST_URL = "getUsers.ajax?num=100";
    public static final String SALON_CREATE_URL = "salon/create?";

    public static final String ARTICLE_MYLIST_AJAX = "getMyArticleList.ajax?";
    public static final String COMMENT_MINE_AJAX = "getMyComments.ajax?";
    public static final String COMMENT_UNREAD_NUM_AJAX = "getMyUnReadCommentsNum.ajax?";
    public static final String SYSTEM_MESSAGE_UNREAD_NUM_AJAX = "numOfUnReadSystemMessage.ajax?";
    public static final String NUM_OF_UNREAD_URL = "user/numOfUnReadInfo?";
    public static final String SYSTEM_MESSAGE_AJAX = "getMessageList.ajax?";
    public static final String USER_INFO_AJAX = "user.ajax?";

    //评论列表
    public static final String COMMENT_LIST_AJAX = "getCommentList.ajax?";
    public static final String COMMENT_LIST = "v1/comment/list?";

    public static final String RES_SUCCESS = "1";

    //基本信息配置文件名称
    public static final String DB = "bang";
    public static final String LOG_TAG = "sjl";
    public static final String NEWWORD_ERROR_TIPS = "网络异常，请稍后再试";
    public static final String SEVER_ERROR_TIPS = "服务器繁忙，请稍后再试";


    //常用字段
    public static final String STATUS = "status";
    public static final String DATA = "data";
    public static final String INTRODUCE = "introduce";
    public static final String THEME = "theme";
    public static final String LOCATION = "lation";
    public static final String DATE = "date";
    public static final String VIEWTIMES = "viewtimes";
    public static final String COMMENTNUM = "commentNum";
    public static final String PRAISE = "praise";
    public static final String TITLE = "title";
    public static final String USERNAME = "username";
    public static final String USERNAME_V1 = "userName";
    public static final String CREATETIME = "createTime";
    public static final String SEARCH_WORD = "searchWord";
    public static final String ARTTITLE = "artTitle";
    public static final String CONTENT = "content";
    public static final String TYPE = "type";
    public static final String PRICE = "price";
    public static final String FROMUSERID = "userId";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String ACADEMY = "academy";
    public static final String EDUCATION = "education";
    public static final String SCHOOL = "school";
    public static final String USERID = "userid";
    public static final String CITY = "city";
    public static final String LEVEL = "level";
    public static final String SIGNATURE = "signature";
    public static final String GENDER = "gender";
    public static final String QQ = "qq";
    public static final String VIP = "vip";
    public static final String INFO = "info";
    public static final String IS_AUTHENTICATE = "isAuthenticate";
    public static final String AUTHENTICATED = "1";

    public static final String AUTHENTICATE_INFO = "authenticateInfo";

    public static final String LEVEL_INSTRUCTION = "level_instruction";
    public static final String PROMPT = "prompt";
    public static final String PROMPT_INFO = "亲，若您要发布服务，需要升级为VIP会员，请联系管理员.";


    public static final String UNREAD = "0";
    public static final String READ = "1";
    public static final String ID = "id";
    public static final String COIN = "coin";

    //sharedpreference  配置里存储的字段
    public static final String LASTACTION = "lastAction";

    public static final String TEM = "TEM";
    public static final String IMEI = "imei";
    public static final String WECHAT = "wechat";

    public static final String AUTHORID_DEFAULT = "0";
    public static final String EMPTY = "";
    public static final String SUBMIT = "提交";
    public static final String BOY = "1";
    public static final String GIRL = "0";

    public static final String REGISTER_ERROR = "注册失败，请稍后重试！";
    public static final String SERVER_ERROR = "服务异常，请稍后重试！";
    public static final String MESSAGE_REQUEST_ERROR = "网络请求失败，未能获取到新消息数量！";
    public static final String REQUEST_ERROR = "网络请求失败，请稍后重试！";
    public static final String REFRESH_NOTHING = "休息一会~暂时无新内容推送给您";
    public static final String REFRESH_PROMPT = "已为您推送新内容";

}
