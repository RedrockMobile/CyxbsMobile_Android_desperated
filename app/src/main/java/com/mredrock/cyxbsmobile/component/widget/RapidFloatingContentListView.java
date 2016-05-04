package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.tag.FlowLayout;
import com.mredrock.cyxbsmobile.component.widget.tag.TagAdapter;
import com.mredrock.cyxbsmobile.component.widget.tag.TagFlowLayout;
import com.mredrock.cyxbsmobile.ui.adapter.BuildingAdapter;
import com.mredrock.cyxbsmobile.util.DensityUtils;
import com.wangjie.rapidfloatingactionbutton.contentimpl.viewbase.RapidFloatingActionContentViewBase;
import com.wangjie.shadowviewhelper.ShadowProperty;
import com.wangjie.shadowviewhelper.ShadowViewHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

public class RapidFloatingContentListView
        extends RapidFloatingActionContentViewBase implements View.OnClickListener, TagFlowLayout.OnSelectListener, AdapterView.OnItemSelectedListener {

    /**
     * 教学楼
     */
    private Spinner mSpBuildings;

    /**
     * 课时
     */
    private TagFlowLayout mSectionTagLayout;

    private int mBuildNumPosition = -1;
    private Set<Integer> mSectionPosSet;

    private OnCompleteButtonClickListener mCompleteListener;

    public RapidFloatingContentListView(Context context) {
        super(context);
    }

    public RapidFloatingContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RapidFloatingContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @NonNull
    @Override
    protected View getContentView() {
        LinearLayout contentView = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.view_empty_fab, null);
        /**fab所展开view的阴影*/
        ShadowViewHelper.bindShadowHelper(
                new ShadowProperty()
                        .setShadowRadius(DensityUtils.dp2px(getContext(), 4))
                        .setShadowColor(0x66404040)
                ,
                contentView
        );
        setup(contentView);
        return contentView;
    }

    public void setup(View view) {
        final List<String> buildNumList = Arrays.asList(getResources().getStringArray(R.array.empty_buildings));
        final String[] sectionNumArray = getResources().getStringArray(R.array.empty_sections);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        setupSpBuildings(view, buildNumList);
        setupSectionTagLayout(view, inflater, sectionNumArray);
        final Button completeBtn = ButterKnife.findById(view, R.id.empty_fab_btn_complete);
        completeBtn.setOnClickListener(this);
    }

    private void setupSpBuildings(View view, final List<String> buildNumList) {
        mSpBuildings = ButterKnife.findById(view, R.id.empty_fab_sp_buildings);
        BuildingAdapter buildingAdapter = new BuildingAdapter(buildNumList);
        mSpBuildings.setAdapter(buildingAdapter);
        mSpBuildings.setOnItemSelectedListener(this);
    }

    private void setupSectionTagLayout(View view, LayoutInflater inflater,
                                       final String[] sectionNumArray) {
        mSectionTagLayout = ButterKnife.findById(view, R.id.empty_fab_section_tagLayout);
        mSectionTagLayout.setAdapter(new TagAdapter<String>(sectionNumArray) {
            @Override
            public View getView(FlowLayout parent, int position, String section) {
                TextView tv = (TextView) inflater.inflate(R.layout.item_empty_fab_tag_item,
                        mSectionTagLayout, false);
                tv.setText(section);
                return tv;
            }
        });
        mSectionTagLayout.setOnSelectListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mCompleteListener != null) {
            mCompleteListener.onCompleteButtonClickListener(mBuildNumPosition, mSectionPosSet);
        }
    }

    @Override
    public void onSelected(Set<Integer> sectionPosSet) {
        //position从0开始
        this.mSectionPosSet = sectionPosSet;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //二教的position为1,三教的position为2.....
        mBuildNumPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public interface OnCompleteButtonClickListener {
        void onCompleteButtonClickListener(int buildNumPosition, Set<Integer> sectionPosSet);
    }

    public void setOnCompleteButtonClickListener(OnCompleteButtonClickListener listener) {
        mCompleteListener = listener;
    }
}
