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
public class NecessaryList extends Fragment {
    View view;
    TextView content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.project_freshman_special__fragment_fg_necessary_list,container,false);
        initView();
        return view;

    }

    private void initView() {
        content = (TextView) view.findViewById(R.id.necessary_second);
        content.setText(Html.fromHtml("携带少量现金（毕竟不是所有的地方都可以使用支付宝的）<BR><font color='#353537'><big>洗护用品方面</big> </font><BR>男生的剃须刀，女生的护肤品（男生也可以适当准备一些)" +
                "常洗漱需要的物品（诸如牙膏牙刷，毛巾，沐浴露，盆桶一" +
                "类），以及洗衣物所需要的洗衣液，刷子等等<BR><font color='#353537'><big>衣物方面</big> </font><BR>四季的外套、袜子等等（要是重庆本地的同学或者是经常回" +
                "家的同学非当季的衣物可以不用携带），各种晾衣物的工具" +
                "（如衣叉，衣架等等）<BR><font color='#353537'><big>寝室住宿方面</big> </font><BR>大部分小鲜肉是会去品尝皇家1,5,6的滋味的，寝室空间比较小。蚊帐（根据个人喜好，也可以采用驱蚊液，这样就可以不挂蚊帐）一些基本的床上用品（枕头，被子等，被单最" +
                "好准备两张，枕套同样如此），凉席（有了空调你也许会忘掉它），遮光帘（根据个人情况，要是对灯光比较敏感，建议准备），\n" +
                "一些日常使用的药品（感冒药，创可贴之类的），台灯，还有各类寝室神器（这些就根据自己的实际需要进行添置）<br><font color='#353537'><big>个人物品使用方面</big> </font><br>电脑（笔记本最宜，其他的数码产品根据自己的情况进行添置），以及一些学习物品（签字笔，笔记本等等）"));
    }
}
