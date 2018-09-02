package com.mredrock.cyxbs.freshman.utils.net;

import com.mredrock.cyxbs.freshman.bean.ChatOnline;
import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.bean.Entity;
import com.mredrock.cyxbs.freshman.bean.MienStu;
import com.mredrock.cyxbs.freshman.bean.MilitaryShow;
import com.mredrock.cyxbs.freshman.bean.SexProportion;
import com.mredrock.cyxbs.freshman.bean.SexRatio;
import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.bean.SubjectProportion;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit 的精髓，正常写Retrofit的接口就好
 */
public interface APIService {
    /**
     * 接口1,获得某一类实体的总数和实体的名称（用实体名称来获取文字描述和图片）
     *
     * @param index index的值应为Const类中INDEX_开头的常量
     * @return Entity实体类
     * @see Const
     * @see Entity
     */
    @GET("data/describe/getamount")
    Observable<Entity> getEntityName(@Query("index") String index);

    /**
     * 接口2，获得男生与女生的人数
     *
     * @param name 学院名
     * @return SexRatio
     * @see SexRatio
     */
    @GET("search/school/1")
    Observable<SexRatio> getSexRatio(@Query("name") String name);

    /**
     * 接口4,获得学院名称（因为只有name，懒得新建类）
     *
     * @return Entity实体类
     * @see Entity
     */
    @GET("search/school/getname")
    Observable<Entity> getAcademyName();

    @GET("data/get/describe")
    Observable<Description> getDescriptions(@Query("index") String index);

    @GET("data/get/junxun")
    Observable<MilitaryShow> getMilitaryShow();

    @GET("data/get/byindex")
    Observable<MienStu> getMienStu(@Query("index") String index, @Query("pagenum") String pageNum, @Query("pagesize") String pageSize);

    /**
     * 数据揭秘相同几个页面的全部数据
     *
     * @param index    标题，Const类里的一部分
     * @param pageNum  分页请求，页数。写死，1页
     * @param pageSize 分页请求，数据数量。写死，无限大
     * @return StrategyData类
     * @see Const
     * @see StrategyData
     */
    @GET("data/get/byindex")
    Observable<StrategyData> getStrategyData(@Query("index") String index, @Query("pagenum") int pageNum, @Query("pagesize") int pageSize);

    /**
     * 数据揭秘学生寝室的全部数据
     *
     * @param name 楼栋名称,Const类里有
     *             知行苑 宁静苑 兴业苑 明理苑
     * @return StrategyData类
     * @see Const
     * @see StrategyData
     */
    @GET("data/get/sushe")
    Observable<StrategyData> getDormitoryData(@Query("name") String name);

    @GET("search/chatgroup/abstractly")
    Observable<ChatOnline> getChatOnline(@Query("index") String index, @Query("key") String key);

    @GET("search/school/1")
    Observable<SexProportion> getSexProportion(@Query("name") String name);

    @GET("search/school/2")
    Observable<SubjectProportion> getSubjectProportion(@Query("name") String name);
}
