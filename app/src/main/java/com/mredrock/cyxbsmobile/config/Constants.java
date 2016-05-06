package com.mredrock.cyxbsmobile.config;

public class Constants {
    //HEADER
    public static final String HEADER_KEY = "API_APP";
    public static final String HEADER_VALUE = "android";

    public static final String REDROCK_PORTAL = "http://hongyan.cqupt.edu.cn/portal";

    public static final String APP_HOME = "http://hongyan.cqupt.edu.cn/app";

    public static final String END_POINT_REDROCK = "http://hongyan.cqupt.edu.cn";
    //个人课表
    public static final String API_PERSON_SCHEDULE = "/redapi2/api/kebiao";
    //无课表
    public static final String API_FREE_SCHEDULE = "";
    //教室课表
    public static final String API_ROOM_SCHEDULE = "/api/roomkebiao";
    //登陆验证接口
    public static final String API_VERIFY = "/api/verify";
    //空教室
    public static final String API_EMPTYROOM = "/api/roomEmpty";
    //考试安排
    public static final String API_EXAM_SCHEDULE = "/api/examSchedule";
    //补考安排
    public static final String API_REEXAM_SCHEDULE = "/api/examReexam";
    //成绩
    public static final String API_SCORE = "/api/examGrade";
    //新闻列表
    public static final String API_NEWS_LIST = "/cyxbsMobile/index.php/home/news/searchtitle";
    //新闻内容
    public static final String API_NEWS_CONTENT = "/cyxbsMobile/index.php/home/news/searchcontent";

    public static final String END_POINT_WEATHER = "http://www.weather.com.cn/data/cityinfo/";
    //天气(GET请求)
    public static final String API_WEATHER = "/101040100.html?appid=213821f9a813579d&private_key=00632f_SmartWeatherAPI_c31aa2d";
    //更新old（GET)
    public static final String API_UPDATE_OLD = "/app/cyxbsAppUpdate.xml";
    //更新new
    public static final String API_UPDATE_NEW = "";
    //意见反馈（POST）
    public static final String API_ADVICE = "/cyxbs_api_2014/cqupthelp/index.php?s=/admin/shop/registSuggestion";
    //周边
    public static final String API_EAT_WHAT = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shake";

    public static final String API_JOB_LIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/jobList";
    public static final String API_JOB_DETAIL = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/jobInfo";

    public static final String API_SHOP_COMMENT_SEND = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/addCom";

    public static final String API_RESTAURANT_MENULIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/menulist";
    public static final String API_RESTAURANT_MENUPRAISE = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/praise";

    public static final String API_SHOP_COMMENT_LIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/comList";

    public static final String API_RESTAURANT_LIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shoplist";
    public static final String API_RESTAURANT_DETAIL = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shopinfo";
    //bbs
    public static final String API_BBS = "http://202.202.43.120/bbs/forum.php";

    //辅导员图片
    public static final String API_TEACH = "http://hongyan.cqupt.edu.cn/welcome/2015/Public/";

    public static final String END_POINT_WELCOME_2015 = "http://hongyan.cqupt.edu.cn/welcome/2015/index.php/home/api";
    //数据展示
    public static final String API_DATA_SHOW = "/data?push=data";
    //查室友、查同学、查辅导员、查同爱好的人
    public static final String API_FIND_FIREND = "/data?push=friend";
    //登陆验证
    public static final String API_LOGIN = "/index?push=login";
    //个人信息
    public static final String API_STUDENT_INFO = "/data?push=info";
    //填写爱好接口
    public static final String API_EXTRA_HOBBY = "/index?push=extra";
    //图片
    public static final String API_PIC = "/banner";
    //知乎接口
    public static final String END_POINT_ZHIHU = "http://news-at.zhihu.com";

    public static final String API_START_PIC = "/api/4/start-image/1080*1776";
    //根据学号找人
    public static final String API_SEARCH_PEOPLE = "http://202.202.43.125/cyxbsMobile/index.php/home/searchPeople?stunum=";


    /**
     * sharepreference name
     */
    public final static String LOCAL_SP = "redrock";
    public final static String LOCAL_SP_ACCOUNT = "account";
    public final static String LOCAL_SP_ACCOUNTS = "accounts";
    public final static String LOCAL_SP_GUID = "guid";

    public final static String schedulePicName = "schedulePic.png";
    public final static String dataFilePath = android.os.Environment.getExternalStorageDirectory() + "/" + "Android/data/com.mredrock.cyxbs/";
    public final static String updataFilePath = android.os.Environment.getExternalStorageDirectory() + "/" + "download/";
    public final static String updataFilename = "com.mredrock.cyxbs.apk";

    public final static String website = "http://hongyan.cqupt.edu.cn/";

    public final static String START_PIC = "start_pic";
    public final static String ISEMPTY_PIC = "isEmptyPic";

    public static int GRIVIEW_COLUMN_HEIGHT = 0;// griview设置的高度
    public static int GRIVEW_COLUMN_NUMS = 4;// 列数
}
