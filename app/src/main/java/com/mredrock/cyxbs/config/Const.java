package com.mredrock.cyxbs.config;

/**
 * Created by cc on 16/3/19.
 */
public class Const {
    public static final int REDROCK_API_STATUS_SUCCESS = 200;

    public static final String APP_HOME = "https://wx.redrock.team/cyxbsMobile";

    public static final String END_POINT_REDROCK = "https://wx.redrock.team";
    public static final String REDROCK_PORTAL = "https://wx.redrock.team/aboutus/";
    public static final String API_PERSON_SCHEDULE = "/redapi2/api/kebiao";
    public static final String APP_WEBSITE = "https://wx.redrock.team/app/";
    public static final String API_VOLUNTEER = "https://wx.redrock.team/servicerecord/";

    //Explore
    public static final String API_MAP_PICTURE = "/welcome/2015/index.php/home/api/banner";
    public static final String API_SHAKE = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shake";
    public static final String API_FOOD_LIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shoplist";
    public static final String API_FOOD_DETAIL = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shopinfo";
    public static final String API_FOOD_COMMENT_LIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/comList";
    public static final String API_SEND_FOOD_COMMENT = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/addCom";
    public static final String API_SCHOOL_CARS_LOCATION = "/extension/test";

    public static final String APT_SEARCH_STUDENT = "/cyxbsMobile/index.php/home/searchPeople/peopleList";

    public static final String API_SOCIAL_OFFICIAL_NEWS_LIST = "/cyxbsMobile/index.php/Home/NewArticle/listNews";
    public static final String API_SOCIAL_HOT_LIST = "/cyxbsMobile/index.php/Home/NewArticle/searchHotArticle";
    public static final String API_SOCIAL_BBDD_LIST = "/cyxbsMobile/index.php/Home/NewArticle/listArticle";
    public static final String API_SOCIAL_IMG_UPLOAD = "/cyxbsMobile/index.php/Home/Photo/uploadArticle";
    public static final String API_SOCIAL_ARTICLE_ADD = "/cyxbsMobile/index.php/Home/Article/addArticle";
    public static final String API_SOCIAL_COMMENT_LIST = "/cyxbsMobile/index.php/Home/NewArticleRemark/getremark";
    public static final String API_SOCIAL_COMMENT_ADD = "/cyxbsMobile/index.php/Home/ArticleRemark/postremarks";
    public static final String API_SOCIAL_LIKE = "/cyxbsMobile/index.php/Home/Praise/addone";
    public static final String API_SOCIAL_UNLIKE = "/cyxbsMobile/index.php/Home/Praise/cancel";
    public static final String API_UPDATE_OLD = "/app/cyxbsAppUpdate.xml";

    public static final String API_AVATAR_UPLOAD = "/cyxbsMobile/index.php/home/photo/upload";
    public static final String API_GET_PERSON_INFO = "/cyxbsMobile/index.php/Home/Person/search";
    public static final String API_GET_PERSON_LATEST = "/cyxbsMobile/index.php/Home/NewArticle/searchtrends";

    //登陆验证接口
    public static final String API_VERIFY = "/api/verify";
    //无课表
    public static final String API_EMPTYROOM = "/api/roomEmpty";
    //考试安排
    public static final String API_EXAM_SCHEDULE = "/api/examSchedule";
    //补考安排
    public static final String API_REEXAM_SCHEDULE = "/examapi/index.php";
    //成绩
    public static final String API_SCORE = "/api/examGrade";
    //修改信息
    public static final String API_EDIT_INFO = "/cyxbsMobile/index.php/Home/Person/setInfo";
    //获得个人信息
    public static final String API_GET_INFO = "/cyxbsMobile/index.php/Home/Person/search";
    //与我相关
    public static final String API_ABOUT_ME = "/cyxbsMobile/index.php/Home/Article/aboutme";
    //根据id得到动态详情
    public static final String API_TREND_DETAIL = "/cyxbsMobile/index.php/Home/NewArticle/searchContent";
    //动态查询
    public static final String API_SEARCH_ARTICLE = "/cyxbsMobile/index.php/Home/NewArticle/searchtrends";
    //志愿时长查询
    public static final String API_VOLUNTEER_LOGIN = "/servicerecord/login";
    public static final String API_VOLUNTEER_UID = "/servicerecord/getrecord";

    //轮播图
    public static final String API_ROLLER_VIEW = "https://wx.redrock.team/app/api/pictureCarousel.php";

    //Affair
    public static final String API_GET_AFFAIR = "/cyxbsMobile/index.php/Home/Person/getTransaction";

    public static final String API_ADD_AFFAIR = "/cyxbsMobile/index.php/Home/Person/addTransaction";

    public static final String API_EDIT_AFFAIR = "/cyxbsMobile/index.php/Home/Person/editTransaction";

    public static final String API_DELETE_AFFAIR = "/cyxbsMobile/index.php/Home/Person/deleteTransaction";

    public static final String API_START_PAGE = "/cyxbsMobile/index.php/Home/Photo/showPicture";

    //电费查询
    public static final String API_ELECTRIC_CHARGE = "/MagicLoop/index.php?s=/addon/ElectricityQuery/ElectricityQuery/queryElecByRoom";

    //往期电费
    public static final String API_BIND_DORMITORY = "/cyxbsMobile/index.php/Home/Person/bindDormitory";
    public static final String API_UNBIND_DORMITORU = "/cyxbsMobile/index.php/Home/Person/deBindDormitory";
    public static final String API_ELECTRIC_QUERY_STUNUM = "/cyxbsMobile/index.php/Home/Extent/getElectric";


    // 失物招领

    public static final String END_POINT_LOST = "https://wx.redrock.team/app/laf/api";
    public static final String API_LOST_LIST = "/view/{theme}/{category}/{page}";
    public static final String PATH_THEME = "theme";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_PAGE = "page";
    public static final String API_LOST_DETAIL = "/detail/{product}";
    public static final String PATH_PRODUCT = "product";
    public static final String API_LOST_CREATE = "/create";

    public static final String SP_KEY_USER = "cyxbsmobile_user";
    public static final String SP_KEY_IS_NIGHT = "is_night";

    //话题
    public static final String API_ALL_TOPIC_LIST = "/cyxbsMobile/index.php/Home/Topic/topicList";
    public static final String API_MY_TOPIC_LIST = "/cyxbsMobile/index.php/Home/Topic/myJoinedTopic";
    public static final String API_TOPIC_ARTICLE = "/cyxbsMobile/index.php/Home/Topic/listTopicArticle";
    public static final String API_ADD_TOPIC_ARTICLE = "/cyxbsMobile/index.php/Home/Topic/addTopicArticle";

    public static final class Extras {
        public static final String EDIT_USER = "10";
        public static final String EDIT_NICK_NAME = "10";
        public static final String EDIT_INTRODUCTION = "30";
        public static final String EDIT_QQ = "15";
        public static final String EDIT_PHONE = "11";
    }

    public static final class Requests {
        public static final int SELECT_PICTURE = 1;
        public static final int SELECT_CAMERA = 2;
        public static final int EDIT_NICKNAME = 3;
        public static final int EDIT_INTRODUCE = 4;
        public static final int EDIT_QQ = 5;
        public static final int EDIT_PHONE = 6;
    }

    /**
     * type_id of some red rock api
     */
    public static final class TypeId {
        public static final int CYXW = 1;
        public static final int JWZX = 2;
        public static final int XSJZ = 3;
        public static final int XWGG = 4;
        public static final int BBDD = 5;
        public static final int NOTICE = 6;
    }

    public static final class CyxbsUri {
        // please modify AndroidManifest.xml if you change it
        public static final String SCHEME = "cyxbsmobile";
        public static final String HOST_TOPIC = "topic";
    }
}
