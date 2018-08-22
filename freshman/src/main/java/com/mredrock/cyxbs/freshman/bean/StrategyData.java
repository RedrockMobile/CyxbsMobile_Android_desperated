package com.mredrock.cyxbs.freshman.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StrategyData implements Serializable {
    /**
     * array : [{"content":"地理位置：老校门斜对面第二个巷子内\n店家描述：学校附近少有的装修精美的餐馆，没有了其他街头小店的浮躁，多的是对美食，对生活精致的态度。这份精致，体现在每一道菜品别具一格的名字，体现在对味道的极致追求。\n推荐菜目：日本豆腐煲，谢氏玉米烙\n","id":1,"name":"无届青年餐馆","picture":["/picture/dbacc0c129424d3cb10a1d3144c10b62.jpg"],"property":"25"},{"content":"地理位置：老校门对面\n店家描述：延续了古茗一贯的好味道，每一口细啜都能品味到一两分独属于茶叶的清香。结合了中国传统茶的精髓与现代拼配制茶的理念，不断地推陈出新，为各位学子带来季度好味。\n推荐饮料：芝士红茶，芝士抹茶，一颗柠檬茶","id":2,"name":"古茗","picture":["/picture/425d66ccf25a4bccb8719f91389ea3a7.jpg","/picture/0d4308cb8505415a8b155e98bae1aa15.jpg","/picture/962337b890894c6e9fbda54b2f50978d.jpg"],"property":"11"},{"content":"地理位置：堕落1街左侧（海鲜焖面那一侧尽头）\n店家描述：在一众正餐中脱颖而出的榴芒君有着与它口碑一样好的味道，香甜馥郁的奶油与当季新鲜水果的缠绵碰撞出了情理之中，意料之外的美味。更让人期待的是，每季榴芒君推出新品都会通过积赞的方式给出大折扣，实在是划算至极。\n推荐菜目：抹茶毛巾卷，芒果班戟","id":3,"name":"榴芒君","picture":["/picture/40c9cc7868214fb9b3cde19f1cbd7389.jpg","/picture/4838a19b7f2e4dc888a538ea5ba2d562.jpg"],"property":"15"},{"content":"地理位置： 多位于堕落街内\n推荐理由：不分地域，不分口味，是相比于火锅来的更为沉稳的好味道，酥脆的土豆和各类颇有嚼劲的肉食在辣椒的微微焦糊味中结合，靠着两三分丰富配菜的滋味，总是能给食客带来味蕾上的极致享受。\n推荐店家：陈记干锅","id":4,"name":"香锅/干锅","picture":["/picture/01ca3b4e34e84f32b4371a5f4f436c80.jpg","/picture/95b4a8e6f4494e6a98b98d1910856f1b.jpg"],"property":"27"},{"content":"地理位置：堕落二街内/新校门右侧一排建筑物\n菜品描述：盛夏，傍晚，一人，余晖中静坐着等待一份好味，或温和，或躁动。酸辣中夹带着河海的鲜味，似乎将氤氲在四周蠢蠢欲动的潮热暑气也隔绝开来，是了，这是独属于花甲米线的创造力。\n推荐店家：哈哈花甲，四海花甲","id":5,"name":"花甲米线","picture":["/picture/545847046ece4096a88785215a6b20f8.jpg","/picture/3729e568f5c54836a7797ddb90a3df97.jpg"],"property":"16"}]
     * index : 周边美食
     */

    @SerializedName("index")
    private String name;
    @SerializedName("array")
    private List<DetailData> details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DetailData> getDetails() {
        return details;
    }

    public void setDetails(List<DetailData> details) {
        this.details = details;
    }

    public static class DetailData implements Serializable {
        /**
         * content : 地理位置：老校门斜对面第二个巷子内
         * 店家描述：学校附近少有的装修精美的餐馆，没有了其他街头小店的浮躁，多的是对美食，对生活精致的态度。这份精致，体现在每一道菜品别具一格的名字，体现在对味道的极致追求。
         * 推荐菜目：日本豆腐煲，谢氏玉米烙
         * <p>
         * id : 1
         * name : 无届青年餐馆
         * picture : ["/picture/dbacc0c129424d3cb10a1d3144c10b62.jpg"]
         * property : 25
         */

        private String content;
        private int id;
        private String name;
        private String property;
        private List<String> picture;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }
    }
}
