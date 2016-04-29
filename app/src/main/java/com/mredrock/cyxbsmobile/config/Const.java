package com.mredrock.cyxbsmobile.config;

/**
 * Created by cc on 16/3/19.
 */
public class Const {
    public static final int REDROCK_API_STATUS_SUCCESS = 200;

    public static final String END_POINT_REDROCK              = "http://hongyan.cqupt.edu.cn";
    public static final String REDROCK_PORTAL                 = "http://hongyan.cqupt.edu.cn/portal";
    public static final String API_PERSON_SCHEDULE            = "/redapi2/api/kebiao";

    //Explore
    public static final String API_EAT_WHAT                   = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shake";
    public static final String API_AROUND_FOOD_RESTAURANTS    = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shoplist";
    public static final String API_RESTAURANT_DETAIL          = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/shopinfo";
    public static final String API_RESTAURANT_COMMENTS        = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/comList";
    public static final String API_RESTAURANT_SEND_COMMENT    = "/cyxbs_api_2014/cqupthelp/index.php/admin/shop/addCom";
    public static final String APT_SEARCH_STUDENT             = "/cyxbsMobile/index.php/home/searchPeople/peopleList";
    //空教室
    public static final String API_EMPTYROOM           = "/api/roomEmpty";
    //考试安排
    public static final String API_EXAM_SCHEDULE       = "/api/examSchedule";
    //补考安排
    public static final String API_REEXAM_SCHEDULE     = "/examapi/index.php";
    //成绩
    public static final String API_SCORE               = "/api/examGrade";
    //修改信息
    public static final String API_EDIT_INFO           = "/cyxbsMobile/index.php/Home/Person/setInfo";
    //获得个人信息
    public static final String API_GET_INFO            = "/cyxbsMobile/index.php/Home/Person/search";
    //与我相关
    public static final String API_ABOUT_ME            = "/cyxbsMobile/index.php/Home/Article/aboutme";
    //动态查询
    public static final String API_SEARCH_ARTICLE      = "/cyxbsMobile/index.php/Home/Article/searchtrends";

    public static final String SP_KEY_USER                    = "user";

    public static final String COURSE_DB_NAME          = "course.db";
    public static final int COURSE_DB_VERSION          = 1;
    public static final String DB_NAME                 = "zscy.db";
    public static final int DB_VERSION                 = 1;
}
