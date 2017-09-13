package com.mredrock.freshmanspecial.units;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.MienBeans.BeautyBean;
import com.mredrock.freshmanspecial.beans.MienBeans.GroupBean;
import com.mredrock.freshmanspecial.beans.MienBeans.OriginalBean;
import com.mredrock.freshmanspecial.beans.MienBeans.StudentsBean;
import com.mredrock.freshmanspecial.beans.MienBeans.TeacherBean;
import com.mredrock.freshmanspecial.beans.QQGroupsBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zxzhu on 2017/8/7.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    public static final int BEAUTY = 0, STUDENT = 1, TEACHER = 2, ORIGINAL = 3, TABS = 4, GROUP = 5, QQGROUP = 6;
    private List data = new ArrayList<>();
    private Context context;
    private int type;
    private PopupWindow popupWindow;
    private OnTabClickListener listener;
    private View firstView;
    private boolean isFirst = true;

    public interface OnTabClickListener {
        void onTabClickListener(int i, View view);
    }

    public View getFirstView() {
        return firstView;
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        this.listener = listener;
    }

    public MyRecyclerAdapter(Context context, List data, int viewType) {
        this.context = context;
        this.data = data;
        this.type = viewType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (type == BEAUTY) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_beauty, parent, false);
            return new MyViewHolder(view, BEAUTY);
        } else if (type == STUDENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_student, parent, false);
            return new MyViewHolder(view, STUDENT);
        } else if (type == TEACHER) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_teacher, parent, false);
            return new MyViewHolder(view, TEACHER);
        } else if (type == ORIGINAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_original, parent, false);
            return new MyViewHolder(view, ORIGINAL);
        } else if (type == TABS) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_tab_groups, parent, false);
            return new MyViewHolder(view, TABS);
        } else if (type == GROUP) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_groups, parent, false);
            return new MyViewHolder(view, GROUP);
        } else if (type == QQGROUP) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_qq_search, parent, false);
            return new MyViewHolder(view, QQGROUP);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        switch (holder.viewType) {
            case BEAUTY:
                BeautyBean.DataBean beauty = (BeautyBean.DataBean) data.get(position);
                int width = ScreenUnit.bulid(context).getPxWide() / 2;
//                params.width = width;
                Glide.with(context).load(beauty.getUrl())
                        .fitCenter()
                        .override(width, width / 2)
                        .crossFade(200)
                        .into(holder.img_beauty);
                holder.content_beauty.setText(beauty.getContent());
                holder.title_beauty.setText(beauty.getTitle());
                break;
            case STUDENT:
                final StudentsBean.DataBean student = (StudentsBean.DataBean) data.get(position);
                holder.name_student.setText(student.getName());
                int width_student = ScreenUnit.bulid(context).getPxWide() / 8;
                Glide.with(context).load(student.getUrl())
                        .fitCenter()
                        .override(width_student, width_student)
                        .crossFade(200)
                        .into(holder.img_student);
                holder.content_student.setText("颁奖词：" + student.getMotto());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initPopupWindow(view, student);
                    }
                });
                break;
            case TEACHER:
                final TeacherBean.DataBean teacher = (TeacherBean.DataBean) data.get(position);
                holder.name_teacher.setText(teacher.getName());
                ViewGroup.LayoutParams params = holder.img_teacher.getLayoutParams();
                int width_teacher = ScreenUnit.bulid(context).getPxWide() / 4;
                Glide.with(context).load(teacher.getUrl())
                        .fitCenter()
                        .override(width_teacher, width_teacher * 9 / 16)
                        .crossFade(200)
                        .into(holder.img_teacher);
                break;
            case ORIGINAL:
                final OriginalBean.DataBean originalBean = (OriginalBean.DataBean) data.get(position);
                holder.title_orignial.setText(originalBean.getName());
                ViewGroup.LayoutParams params_original = holder.layout_orginal.getLayoutParams();
                int width_original = ScreenUnit.bulid(context).getPxWide() / 7 * 3;
                params_original.width = width_original;
                params_original.height = width_original * 4 / 5;
                Glide.with(context).load(originalBean.getCover())
                        .fitCenter()
                        .override(width_original, width_original * 9 / 16)
                        .crossFade(200)
                        .into(holder.img_original);
                holder.time_original.setText(originalBean.getTime());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(originalBean.getUrl());
                        intent.setData(content_url);
                        context.startActivity(intent);
                    }
                });
                break;
            case TABS:
                if (isFirst && position == 0) {
                    firstView = holder.title_tab;
                    holder.title_tab.setBackgroundResource(R.drawable.special_2017_tab_focus);
                    isFirst = false;
                }
                holder.title_tab.setText(data.get(position).toString());
                holder.title_tab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onTabClickListener(position, holder.title_tab);
                        }
                        holder.title_tab.setBackgroundResource(R.drawable.special_2017_tab_focus);
                    }
                });
                break;
            case GROUP:
                GroupBean groupBean = (GroupBean) data.get(position);
                holder.content_group.setText(groupBean.getContent());
                holder.title_group.setText(groupBean.getTitle());
                break;
            case QQGROUP:
                final QQGroupsBean.DataBean QQdataBean = (QQGroupsBean.DataBean) data.get(position);
                holder.tx_qq_searh.setText(QQdataBean.getGroupName() + "：" + QQdataBean.getNumber());
                holder.tx_qq_searh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(QQdataBean.getNumber());
                        Toast.makeText(context, QQdataBean.getGroupName() + "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    private void initPopupWindow(View view, StudentsBean.DataBean bean) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.special_2017_pop_window_excellent, null);
        // 三部曲第二  构造函数关联
        View parent = LayoutInflater.from(context).inflate(R.layout.special_2017_fragment_student, null);
        TextView name, content;
        CircleImageView img;
        ImageView close;
        close = (ImageView) popupView.findViewById(R.id.close_pop);
        name = (TextView) popupView.findViewById(R.id.name_pop);
        content = (TextView) popupView.findViewById(R.id.tx_pop);
        img = (CircleImageView) popupView.findViewById(R.id.img_pop);
        name.setText(bean.getName());
        content.setText(bean.getMotto() + bean.getResume());
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        Glide.with(context).load(bean.getUrl()).into(img);
        int wide = ScreenUnit.bulid(context).getPxWide();
        int height = ScreenUnit.bulid(context).getPxHiget();
        popupWindow = new PopupWindow(popupView, wide * 3 / 4, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // =======  两者结合才能让popup点击外部消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // =======  两者结合才能让popup点击外部消失
        // 让popup占有优先于activity的交互响应能力，不单单是焦点问题。
        popupWindow.setFocusable(true);
        // 设置动画  这里选用系统提供的
        popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
        // popup和软键盘的关系
        // 三部曲第三   展示popup
        backgroundAlpaha((Activity) context, 0.5f);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpaha((Activity) context, 1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     **/
    public void backgroundAlpaha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_student;
        MyImageView img_original, img_teacher;
        ImageView img_beauty;
        TextView title_beauty, name_student, content_beauty, content_student, title_orignial, time_original, name_teacher, title_tab, title_group, content_group, tx_qq_searh;
        FrameLayout layout_orginal;
        int viewType;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            switch (viewType) {
                case BEAUTY:
                    img_beauty = (ImageView) itemView.findViewById(R.id.img_item_beauty);
                    title_beauty = (TextView) itemView.findViewById(R.id.title_item_beauty);
                    content_beauty = (TextView) itemView.findViewById(R.id.tx_item_beauty);
                    break;
                case STUDENT:
                    img_student = (CircleImageView) itemView.findViewById(R.id.img_item_student);
                    name_student = (TextView) itemView.findViewById(R.id.name_item_student);
                    content_student = (TextView) itemView.findViewById(R.id.tx_item_student);
                    break;
                case TEACHER:
                    img_teacher = (MyImageView) itemView.findViewById(R.id.img_teacher);
                    name_teacher = (TextView) itemView.findViewById(R.id.name_teacher);
                    break;
                case ORIGINAL:
                    img_original = (MyImageView) itemView.findViewById(R.id.img_original);
                    title_orignial = (TextView) itemView.findViewById(R.id.title_original);
                    time_original = (TextView) itemView.findViewById(R.id.time_original);
                    layout_orginal = (FrameLayout) itemView.findViewById(R.id.original_layout);
                    break;
                case TABS:
                    title_tab = (TextView) itemView.findViewById(R.id.tab_groups_title);
                    break;
                case GROUP:
                    title_group = (TextView) itemView.findViewById(R.id.item_groups_title);
                    content_group = (TextView) itemView.findViewById(R.id.item_groups_content);
                    break;
                case QQGROUP:
                    tx_qq_searh = (TextView) itemView.findViewById(R.id.tx_item_qq);
            }
        }
    }
}
