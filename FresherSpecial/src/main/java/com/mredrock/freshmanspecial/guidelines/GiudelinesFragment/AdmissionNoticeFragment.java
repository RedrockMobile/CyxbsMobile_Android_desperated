package com.mredrock.freshmanspecial.guidelines.GiudelinesFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.freshmanspecial.beans.GuidelinesAdmissionFirBean;
import com.mredrock.freshmanspecial.guidelines.Adapter.FirstAdmissionRecyclerAdapter;
import com.mredrock.freshmanspecial.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glossimar on 2017/8/3.
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-入学须知
 */

public class AdmissionNoticeFragment extends Fragment{
    private List<GuidelinesAdmissionFirBean> firBeenList;
    private List<GuidelinesAdmissionFirBean.GuidelinesAdmissionSecBean> secBeanList;
    private GuidelinesAdmissionFirBean firBean;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_2017_fragment_admission, container, false);
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_admission_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new FirstAdmissionRecyclerAdapter(firBeenList, view.getContext()));
        return view;
    }

    public void initData() {
        firBeenList = new ArrayList<>();
        secBeanList = new ArrayList<>();

        //新生清单
        addSecAdmission("报道时间：", "本科新生2017年9月14、15日报道");
        addSecAdmission("报道地点：", "重庆邮电大学风雨操场");
        addSecAdmission("新生必带：", "自带同版近期照片共15张（要求光面相纸洗印，白底一寸，半神，正脸，免冠大头照片），" +
                "新生档案，党团关系证明，户口本（需要迁户口的同学携带），录取通知书，高考准考证，身份证以及身份证" +
                "（复印件多复印几份，多复印几份，虽然学校并未做要求），银行卡（缴学费）,少量现金。");
        addSecAdmission("洗护用品：", "护肤品，剃须刀，日常洗漱需要的物品（诸如牙膏牙刷，毛巾，沐浴露，洗衣液等）");
        addSecAdmission("生活用品：", "四季的衣物（重庆本地的同学或经常回家的同学携带当季衣服即可），各种晾晒衣物的工具" +
                "，蚊帐，一些基本的床上用品，凉席，遮光帘（要是对灯光比较敏感，建议准备），一些常用药品，台灯，还有各类寝室神器。");
        addSecAdmission("个人物品：",  "电脑（笔记本最宜，其他的数码产品根据自己的情况进行添置），以及一些学习物品" +
                "（签字笔，笔记本等等），台灯，水杯等等。");
        addFirstAdmission("新生清单");

        //安全守则
        addSecAdmission("防止上当受骗：", "一些不法分子利用刚入学的新生不熟悉情况，以老师，" +
                "学长或者老乡的身份骗取新生信任，然后以代费、减免学费等多种方式进行诈骗。");
        addSecAdmission("不携带过多现金：", "数额较大的现金应该及时存入银行，存折、银行卡、" +
                "身份证尽量分开放；使用银行卡要谨慎以防密码泄露。");
        addSecAdmission("拒绝上门推销：", "许多不法分子以到寝室推销为名进行诈骗或盗窃，如若发现上门推销人员，" +
                "应该及时报告宿管人员或者保卫处。");
        addSecAdmission("室内注意防盗：", "要保管好自己的笔记本电脑、手机等贵重物品，不要将其随意放置，以免被“顺手牵羊”。");
        addSecAdmission("注意消防安全：","爱护消防设施，寝室内不违章使用大功率电器。");
        addSecAdmission("注意交通安全：","不乘坐“黑车”和存在安全隐患的车辆。");
        addSecAdmission("遇到情况及时与公安机关联系：","在遇到不法侵害时，要及时与公安机关（110）或者学校保卫处联系（62461018,62460110）。");
        addFirstAdmission("安全守则");

        //乘车路线
        addSecAdmission("方案一： ","迎新接站：报道期间，我校将在重庆火车北站南、北广场设新生接待站，有同学负责引导新生到指定地点乘车");
        addSecAdmission("方案二： ", "重庆江北机场（距离学校约40公里）：可乘机场大巴至上清寺后转乘108路公交车至南坪，再转乘346或347路公交车到学校；或乘轻轨三号线到南坪，再转乘346或347路公交车到学校；直接打车到校费用约为70元；");
        addSecAdmission("方案三： ", "龙头寺火车站、重庆北站（距离学校约20公里）：乘323路或168路公交车至南坪，转乘346或347路公交车至学校：或乘轻轨三号线到南坪，再转乘346或347路公交");
        addSecAdmission("方案四： ", "菜园坝火车站、汽车站（距离学校约12公里）：可在菜园坝广场乘347路公交车至学校；直接打车到校费用约为25元；");
        addSecAdmission("方案五： ", "朝天门码头（距离学校约9公里）：可乘车至南坪后转乘346或347路公交车至学校；直接打车到校费用约为20元。");
        addFirstAdmission("乘车路线");

    }


    public void addFirstAdmission(String title) {
        firBean = new GuidelinesAdmissionFirBean();
        firBean.setFirstTitle(title);
        firBean.setGuidelinesAdmissionSecBeanList(secBeanList);
        firBeenList.add(firBean);
        secBeanList = new ArrayList<>();
        for (int i = 0; i < 2; i ++) {
//            Log.d(TAG, "addFirstAdmission: " + firBean.getGuidelinesAdmissionSecBeanList().get(i).getSecondTitle());
        }

    }

    public void addSecAdmission(String secTitle, String secText) {
        GuidelinesAdmissionFirBean.GuidelinesAdmissionSecBean bean = new GuidelinesAdmissionFirBean.GuidelinesAdmissionSecBean();
        String text = "<font color='#65B2FF'>" + secTitle + "</font>" + secText;
        bean.setText(text);
        secBeanList.add(bean);
//        Log.d(TAG, "addSecAdmission: " + bean.getSecondTitle()+ secBeanList.get(1).getSecondTitle()
//        );
    }
}
