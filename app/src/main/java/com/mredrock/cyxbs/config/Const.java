package com.mredrock.cyxbs.config;

/**
 * Created by cc on 16/3/19.
 */
public class Const {
    public static final int REDROCK_API_STATUS_SUCCESS = 200;

    public static final String END_POINT_REDROCK = "http://hongyan.cqupt.edu.cn";
    public static final String REDROCK_PORTAL = "http://hongyan.cqupt.edu.cn/portal";
    public static final String API_PERSON_SCHEDULE = "/redapi2/api/kebiao";

    //Explore
    public static final String API_MAP_PICTURE       = "/welcome/2015/index.php/home/api/banner";
    public static final String API_SHAKE             = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shake";
    public static final String API_FOOD_LIST         = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shoplist";
    public static final String API_FOOD_DETAIL       = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shopinfo";
    public static final String API_FOOD_COMMENT_LIST = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/comList";
    public static final String API_SEND_FOOD_COMMENT = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/addCom";

    public static final String APT_SEARCH_STUDENT = "/cyxbsMobile/index.php/home/searchPeople/peopleList";

    public static final String API_SOCIAL_OFFICIAL_NEWS_LIST = "/cyxbsMobile/index.php/Home/Article/listNews";
    public static final String API_SOCIAL_HOT_LIST = "/cyxbsMobile/index.php/Home/Article/searchHotArticle";
    public static final String API_SOCIAL_BBDD_LIST = "/cyxbsMobile/index.php/Home/Article/listArticle";
    public static final String API_SOCIAL_IMG_UPLOAD = "/cyxbsMobile/index.php/Home/Photo/uploadArticle";
    public static final String API_SOCIAL_ARTICLE_ADD = "/cyxbsMobile/index.php/Home/Article/addArticle";
    public static final String API_SOCIAL_COMMENT_LIST = "/cyxbsMobile/index.php/Home/ArticleRemark/getremark";
    public static final String API_SOCIAL_COMMENT_ADD = "/cyxbsMobile/index.php/Home/ArticleRemark/postremarks";
    public static final String API_SOCIAL_LIKE = "/cyxbsMobile/index.php/Home/Praise/addone";
    public static final String API_SOCIAL_UNLIKE = "/cyxbsMobile/index.php/Home/Praise/cancel";


    public static final String API_AVATAR_UPLOAD     = "/cyxbsMobile/index.php/home/photo/upload";
    public static final String API_GET_PERSON_INFO   = "/cyxbsMobile/index.php/Home/Person/search";
    public static final String API_GET_PERSON_LATEST = "/cyxbsMobile/index.php/Home/Article/searchtrends";

    //登陆验证接口
    public static final String API_VERIFY          = "/api/verify";
    public static final String API_EMPTYROOM       = "/api/roomEmpty";
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
    public static final String API_TREND_DETAIL = "/cyxbsMobile/index.php/Home/Article/searchContent";
    //动态查询
    public static final String API_SEARCH_ARTICLE = "/cyxbsMobile/index.php/Home/Article/searchtrends";

    public static final String SP_KEY_USER = "cyxbsmobile_user";
    public static final String SP_KEY_IS_NIGHT = "is_night";

    public static final String COURSE_DB_NAME = "course.db";
    public static final int COURSE_DB_VERSION = 1;

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
}
