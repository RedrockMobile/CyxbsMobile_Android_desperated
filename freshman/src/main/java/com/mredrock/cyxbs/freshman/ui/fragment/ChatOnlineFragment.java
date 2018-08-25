package com.mredrock.cyxbs.freshman.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.ChatOnline;
import com.mredrock.cyxbs.freshman.mvp.contract.ChatOnlineContract;
import com.mredrock.cyxbs.freshman.mvp.model.ChatOnlineModel;
import com.mredrock.cyxbs.freshman.mvp.presenter.ChatOnlinePresenter;
import com.mredrock.cyxbs.freshman.ui.adapter.ChatOnlineAdapter;
import com.mredrock.cyxbs.freshman.ui.widget.JCardView;
import com.mredrock.cyxbs.freshman.utils.DensityUtils;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatOnlineFragment extends Fragment implements ChatOnlineContract.IChatOnlineView {
    private String kind;
    private View parent;
    private String key = "";

    private EditText editText;
    private RecyclerView recyclerView;
    private ChatOnlinePresenter presenter;
    private List<ChatOnline.ChatBean> datas;
    private ChatOnlineAdapter adapter;
    private ImageView search_img;
    private JCardView jCardView;

    public void setInit(String kind) {
        this.kind = kind;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.freshman_fragment_chatonline, container, false);

        initP();
        init();
        setET();
        return parent;
    }

    private void initP() {
        presenter = new ChatOnlinePresenter(new ChatOnlineModel());
        presenter.attachView(this);
    }

    private void init() {
        editText = parent.findViewById(R.id.freshman_chatonline_et);
        recyclerView = parent.findViewById(R.id.freshman_chatonline_rv);
        search_img = parent.findViewById(R.id.freshman_chat_search);
        jCardView = parent.findViewById(R.id.freshman_chatonline_jc);
        datas = new ArrayList<>();
    }

    private void setET() {
        adapter = new ChatOnlineAdapter(getContext(), datas, new int[]{R.layout.freshman_item_chatonline_lv});
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int screenHeight = DensityUtils.getScreenHeight(getContext());

        ViewGroup.LayoutParams lp = jCardView.getLayoutParams();
        lp.height = (int) (screenHeight / 9.5);
        jCardView.setLayoutParams(lp);


        ViewGroup.LayoutParams lp1 = search_img.getLayoutParams();
        lp1.height = lp.height / 3;
        search_img.setLayoutParams(lp1);

        recyclerView.setPadding(0, screenHeight / 12, 0, 0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                key = s.toString();
                presenter.search(kind, key);
                if (s.length() == 0) {
                    adapter.clearData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1 != null) {
            imm1.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                search_img.setVisibility(View.GONE);
                setHint();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            } else {
                if (key.length() == 0) {
                    search_img.setVisibility(View.VISIBLE);
                    hideHint();
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                hideHint();
            }
        });
    }

    private void setHint() {
        if (kind.equals("老乡群")) {
            editText.setHint("请输入地区");
        } else {
            editText.setHint("请输入学院");
        }
    }

    private void hideHint() {
        editText.setHint("");
    }


    @Override
    public void setData(ChatOnline bean) {
        if (bean.getArray().size() > 0) {
            adapter.refreshData(bean);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.show("没有搜索到对应数据噢！");
        }
    }

    public EditText getEditText() {
        return editText;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
