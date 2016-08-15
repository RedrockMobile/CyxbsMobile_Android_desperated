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
public class EnrolWayFragment extends Fragment{
    View view;
    TextView FirstTitle;
    TextView SecondTitle;
    TextView ThirdTitle;


    TextView FirstContentOne;
    TextView FirstContentTwo;
    TextView FirstContentThree;

    TextView SecondContentOne;
    TextView SecondContentTwo;

    TextView ThirdContentOne;
    TextView ThirdContenttwo;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.project_freshman_special__fragment_fg_enrol_way,container,false);
        initView();
        return view;


    }

    private void initView() {
        //FirstTitle;
        FirstContentOne= (TextView) view.findViewById(R.id.first_way_information_content_one);
        FirstContentOne.setText(Html.fromHtml("可乘机场大巴至上清寺后转乘<font color='#68AFF4'>108</font>路公交车至南坪,再转乘<font color='#68AFF4'>346</font>或<font color='#68AFF4'>347</font>路公交车到学校"));
        FirstContentTwo= (TextView) view.findViewById(R.id.first_way_information_content_two);
        FirstContentTwo.setText(Html.fromHtml("乘轻轨<font color='#68AFF4'>三号线</font>到南坪,再转乘<font color='#68AFF4'>346</font>或<font color='#68AFF4'>347</font>路公交车即可"));
        FirstContentThree= (TextView) view.findViewById(R.id.first_way_information_content_three);
        FirstContentThree.setText(Html.fromHtml("直接打车到校费用约为70元"));
        //SecondTitle;
        SecondContentOne= (TextView) view.findViewById(R.id.second_way_information_content_one);
        SecondContentOne.setText(Html.fromHtml("可乘<font color='#68Aff4'>323</font>路或<font color='#68Aff4'>168</font>路公交车至南坪,转乘<font color='#68Aff4'>346</font>或<font color='#68Aff4'>347</font>公交车至学校"));
        SecondContentTwo= (TextView) view.findViewById(R.id.second_way_information_content_two);
        SecondContentTwo.setText(Html.fromHtml("乘轻轨<font color='#68AFF4'>三号线</font>到南坪，再转乘<font color='#68AFF4'>346</font>或<font color='#68AFF4'>347</font>路公交车到学校；直接打车到校费用约40元"));
        //ThirdTitle;
        ThirdContentOne= (TextView) view.findViewById(R.id.third_way_information_content_one);
        ThirdContentOne.setText(Html.fromHtml("可在菜园坝广场乘<font color='#68AFF4'>347</font>路公交车至学校"));
        ThirdContenttwo= (TextView) view.findViewById(R.id.third_way_information_content_two);
        ThirdContenttwo.setText(Html.fromHtml("直接打车到校费用约为25元"));
    }
}
