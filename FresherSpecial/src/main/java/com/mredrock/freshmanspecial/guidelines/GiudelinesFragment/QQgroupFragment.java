package com.mredrock.freshmanspecial.guidelines.GiudelinesFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.base.BaseFragment;

/**
 * Created by Glossimar on 2017/8/3.
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-QQ群
 */

public class QQgroupFragment extends BaseFragment {

    private FrameLayout qq_search;
    private TextView tx_new,tx_local;

    @Override
    protected void initData() {
        qq_search = $(R.id.search_qq);
        tx_new = $(R.id.tx_groups_new);
        tx_local = $(R.id.tx_groups_local);
        setSearch();
        setText();
    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_qq;
    }

    private void setText() {
        tx_new.setText("重庆邮电大学总群：636208141\n" +
                "通信与信息工程学院：498167991\n" +
                "计算机与科学技术学院：638612170\n" +
                "自动化学院：574872113\n" +
                "光电工程学院/国际半导体学院：636449199\n" +
                "外国语学院：333094013\n" +
                "传媒艺术学院：527468298\n" +
                "生物信息学院：637402699\n" +
                "经济管理学院信息管理与信息系统专业：362192309\n" +
                "经济管理学院： 545772871\t\n" +
                "经济管理学院工程管理专业：552540368\n" +
                "软件工程学院：482656306\n" +
                "网络空间安全与信息法学院：162240404 \n" +
                "理学院：575159267\n" +
                "体育学院：649510732\n" +
                "国际学院：17443276\n" +
                "先进制造工程学院：563565394");
        tx_local.setText("贵州：601631814\n" +
                "河北：644348638  \n" +
                "安徽：562487104  \n" +
                "辽宁：134489031  \n" +
                "河南老乡群1：310222276  \n" +
                "河南老乡群2：251311309  \n" +
                "河南安阳：116198098  \n" +
                "山东：384043802  \n" +
                "江苏：123736116 \n" +
                "黑龙江：316348915  \n" +
                "潮汕：4958681  \n" +
                "江西：3889855  \n" +
                "江西上饶：476426072  \n" +
                "浙江：247010642  \n" +
                "广西贵港：5819894  \n" +
                "广西南宁：16026851  \n" +
                "广西：9651531  \n" +
                "广西柳州：7045893  \n" +
                "广东：113179139  \n" +
                "广东韶关：66484867  \n" +
                "广东惠州：213337022  \n" +
                "山西：119738941  \n" +
                "海南：9334029  \n" +
                "福建：173210510  \n" +
                "吉林：118060379  \n" +
                "云南宣威：211910023  \n" +
                "云南玉溪：256581906  \n" +
                "云南曲靖：117499346  \n" +
                "云南：548640416  \n" +
                "云南官方群：42052111  \n" +
                "天津：8690505  \n" +
                "湖北恩施：179765240  \n" +
                "湖北：33861584  \n" +
                "湖北黄冈：181704337  \n" +
                "湖南：204491110  \n" +
                "重庆梁平：85423833  \n" +
                "重庆忠县：115637967  \n" +
                "重庆铜梁：198472776  \n" +
                "重庆大足：462534986  \n" +
                "重庆开县：5657168  \n" +
                "重庆荣昌：149452192  \n" +
                "重庆永川：467050041  \n" +
                "重庆丰都：343292119  \n" +
                "重庆涪陵：199748999  \n" +
                "重庆云阳：118971621  \n" +
                "重庆璧山：112571803  \n" +
                "重庆石柱：289615375  \n" +
                "重庆彭水：283978475  \n" +
                "重庆南川：423494314  \n" +
                "重庆垫江：307233230  \n" +
                "重庆合川：226325326  \n" +
                "重庆荣昌：149452192  \n" +
                "重庆綦江：109665788  \n" +
                "重庆奉节：50078959  \n" +
                "重庆铜梁：198472776  \n" +
                "重庆黔江：102897346  \n" +
                "重庆万州：469527984  \n" +
                "重庆巫溪：143884210  \n" +
                "重庆巫山：129440237  \n" +
                "四川大群：142604890  \n" +
                "四川成都：298299346  \n" +
                "四川自贡：444020511  \n" +
                "四川绵阳：191653502  \n" +
                "陕西：193388613  \n" +
                "新疆：248052400  \n" +
                "青海：282597612  \n" +
                "北京：143833720  \n" +
                "甘肃美术：578076400  \n" +
                "甘肃：155724412 ");
    }

    private void setSearch(){
        qq_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ppp", "onClick: ");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.qq_layout,new QQSearchFragment());
                transaction.commit();
            }
        });
    }
}
