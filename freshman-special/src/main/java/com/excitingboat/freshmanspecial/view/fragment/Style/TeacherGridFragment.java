package com.excitingboat.freshmanspecial.view.fragment.Style;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.Teacher;
import com.excitingboat.freshmanspecial.presenter.GetInformationPresenter;
import com.excitingboat.freshmanspecial.utils.RoundImageView;
import com.excitingboat.freshmanspecial.view.adapter.TeacherRecyclerAdapter;
import com.excitingboat.freshmanspecial.view.iview.IGetInformation;

import java.util.List;

/**
 * Created by PinkD on 2016/8/9.
 * TeacherGridFragment
 */
public class TeacherGridFragment extends Fragment implements IGetInformation<Teacher>, TeacherRecyclerAdapter.OnItemClickListener {
    private GetInformationPresenter<Teacher> presenter;
    private RecyclerView recyclerView;
    private TeacherRecyclerAdapter teacherRecyclerAdapter;
    private int currentPage;
    private Dialog dialog;
    private RoundImageView dialogPicture;
    private TextView dialogName;



    public void setPresenter(Context context, GetInformationPresenter<Teacher> presenter) {
        this.presenter = presenter;
        teacherRecyclerAdapter = new TeacherRecyclerAdapter(context);
        currentPage = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.project_freshman_special__recyclerview, container, false);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        teacherRecyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(teacherRecyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        presenter.getInformation(new int[]{currentPage, 15});
    }

    @Override
    public void requestSuccess(List<Teacher> list) {
        if (list.size() > 0) {
            teacherRecyclerAdapter.addAll(list);
            presenter.getInformation(new int[]{++currentPage, 15});
        }
    }

    @Override
    public void requestFail() {
        Toast.makeText(getContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (dialog == null) {
            dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.project_freshman_special__dialog_t);
            dialogPicture = (RoundImageView) dialog.findViewById(R.id.dialog_picture);
            dialogName = (TextView) dialog.findViewById(R.id.dialog_name);
        }
        Glide.with(getContext())
                .load(teacherRecyclerAdapter.getData().get(position).getPhoto().get(0).getPhoto_src())
                //.placeholder(R.drawable.loading_t)
                .into(dialogPicture);
        dialogName.setText(teacherRecyclerAdapter.getData().get(position).getName());
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unBind();
    }
}
