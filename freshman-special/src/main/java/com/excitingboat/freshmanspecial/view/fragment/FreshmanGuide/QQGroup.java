package com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;

/**
 * Created by xushuzhan on 2016/8/4.
 */
public class QQGroup extends Fragment {
    TextView QQGroupFreshman;
    TextView QQGroupFellowVillager;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.project_freshman_special__fragment_fg_qq_group, container, false);
        initView();
        return view;
    }

    private void initView() {
        QQGroupFreshman = (TextView) view.findViewById(R.id.qq_group_freshman);
        QQGroupFreshman.setText(Html.fromHtml("各学院新生群：<BR>\n" +
                "通信与信息工程学院：561165428 \n" +
                "554320581（交流群）<BR>\n" +
                "计算机与科学技术学院：295557817 \n" +
                "338572942 （学生会）<BR>\n" +
                "自动化学院：152354704<BR>\n" +
                "光电学院 国际半导体学院：481054099<BR>\n" +
                "软件工程学院：237085236<BR>\n" +
                "先进制造学院：459929235<BR>\n" +
                "外国语学院：528147534<BR>\n" +
                "体育学院：514172370<BR>\n" +
                "传媒艺术学院：218509477<BR>\n" +
                "生物信息学院：201563258<BR>\n" +
                "法学院：569104494<BR>\n" +
                "经济管理学院：107961087 （信管：324274314)<BR>工程管理：547781573<BR>工商：497763971<BR>电子商务： 578509964）<BR>\n" +
                "中美合作群：548496592（国际学院） <BR>\n" +
                "理学院：301047601<BR>\n" +
                "重邮16级广电与数媒类群 ：495242730<BR>\n" +
                "重邮新生群（中加） ：580035411<BR>\n" +
                "国际学院，中美群:345070285 (辅导员创建)<BR>\n"));
        QQGroupFellowVillager = (TextView) view.findViewById(R.id.qq_group_fellow_villager);
        QQGroupFellowVillager.setText(Html.fromHtml("贵州：126851693\n" +
                "<BR>河北：548535234\n" +
                "<BR>安徽：483413066 562487104\n" +
                "<BR>辽宁：134489031\n" +
                "<BR>河南：310222276 \n" +
                "<BR>重邮河南老乡群：251311309\n" +
                "<BR>河南安阳：116198098\n" +
                "<BR>山东：384043802\n" +
                "<BR>江苏：123736116\n" +
                "<BR>黑龙江：316348915\n" +
                "<BR>潮汕：4958681\n" +
                "<BR>贵州：126851693\n" +
                "<BR>江西：3889855\n" +
                "<BR>江西上饶：476426072\n" +
                "<BR>浙江：247010642\n" +
                "<BR>广西贵港：5819894\n" +
                "<BR>广西南宁：16026851 \n" +
                "<BR>广西：9651531\n" +
                "<BR>广西柳州：7045893\n" +
                "<BR>广东：113179139\n" +
                "<BR>广东韶关：66484867\n" +
                "<BR>广东惠州：213337022\n" +
                "<BR>山西：119738941\n" +
                "<BR>海南：9334029\n" +
                "<BR>福建：173210510\n" +
                "<BR>吉林：118060379\n" +
                "<BR>云南宣威：211910023\n" +
                "<BR>云南玉溪：256581906\n" +
                "<BR>云南曲靖：117499346\n" +
                "<BR>云南： 548640416\n" +
                "<BR>重邮云南老乡会（包括云南各地）官方群：42052111\n" +
                "<BR>天津：8690505\n" +
                "<BR>湖北恩施：179765240\n" +
                "<BR>湖北：33861584\n" +
                "<BR>湖北黄冈老乡群：181704337\n" +
                "<BR>湖南：204491110\n" +
                "<BR>重庆梁平：85423833\n" +
                "<BR>重庆忠县：115637967\n" +
                "<BR>重庆铜梁：198472776 \n" +
                "<BR>重庆大足：462534986 \n" +
                "<BR>重庆开县：5657168    \n" +
                "<BR>重庆荣昌：149452192\n" +
                "<BR>重庆永川：467050041\n" +
                "<BR>重庆丰都：343292119 \n" +
                "<BR>重庆涪陵：199748999 \n" +
                "<BR>重庆云阳：118971621\n" +
                "<BR>重庆璧山：112571803 \n" +
                "<BR>重庆石柱：289615375 \n" +
                "<BR>重庆彭水：283978475 \n" +
                "<BR>重庆南川：423494314\n" +
                "<BR>重庆长寿：69124125   \n" +
                "<BR>重庆垫江307233230\n" +
                "<BR>重庆合川：226325326 \n" +
                "<BR>重庆綦江：109665788\n" +
                "<BR>重庆奉节：50078959   \n" +
                "<BR>重庆铜梁：198472776 \n" +
                "<BR>重庆黔江：102897346 \n" +
                "<BR>重庆万州：469527984\n" +
                "<BR>重庆巫溪：143884210 \n" +
                "<BR>重庆巫山：129440237\n" +
                "<BR>重庆垫江：316213623\n" +
                "<BR>四川简阳：570320904\n" +
                "<BR>四川大群：148985246 142604890 \n" +
                "<BR>四川成都：298299346 \n" +
                "<BR>四川自贡：444020511\n" +
                "<BR>四川绵阳：191653502 \n" +
                "<BR>四川眉山：273968031 \n" +
                "<BR>陕西：193388613  \n" +
                "<BR>黑龙江：316348915   \n" +
                "<BR>重邮新疆： 248052400\n" +
                "<BR>青海：282597612 \n" +
                "<BR>北京：143833720\n" +
                "<BR>甘肃美术老乡：578076400 \n" +
                "<BR>甘肃：155724412\n"));
    }
}
