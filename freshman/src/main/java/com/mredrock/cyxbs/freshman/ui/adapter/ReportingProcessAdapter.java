package com.mredrock.cyxbs.freshman.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.ui.activity.PhotoViewerActivityKt;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import java.util.ArrayList;
import java.util.List;

/*
 by Cynthia at 2018/8/16
 description :
 */
public class ReportingProcessAdapter extends RecyclerView.Adapter<ReportingProcessAdapter.ReportingProcessViewHolder> {

    private List<StrategyData.DetailData> list;
    private ClickItemListener listener;
    private Context context;

    public ReportingProcessAdapter(List<StrategyData.DetailData> report, ClickItemListener listener, Context context) {
        this.list = report;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportingProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(App.getContext()).inflate(R.layout.freshman_recycle_item_report, parent, false);
        return new ReportingProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportingProcessViewHolder holder, int position) {
        holder.loadData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<StrategyData.DetailData> getList() {
        return list;
    }

    public void setList(List<StrategyData.DetailData> mData) {
        this.list = mData;
        notifyItemRangeInserted(0, list.size() - 1);
    }

    class ReportingProcessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RoundedImageView real;
        private RoundedImageView map;
        private TextView title;
        private TextView step;
        private TextView content;
        private ImageView arrow;

        private String realStr;
        private String mapStr;

        ReportingProcessViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            real = view.findViewById(R.id.riv_report_real);
            map = view.findViewById(R.id.riv_report_map);
            title = view.findViewById(R.id.tv_report_location);
            step = view.findViewById(R.id.tv_report_step);
            content = view.findViewById(R.id.tv_report_context);
            arrow = view.findViewById(R.id.iv_report_arrow);

            real.setOnClickListener(this);
            map.setOnClickListener(this);
            content.setOnClickListener(this);
            arrow.setOnClickListener(this);
        }

        private void loadData(StrategyData.DetailData detailData) {
            title.setText(detailData.getName());
            String temp = "步骤" + detailData.getId();
            step.setText(temp);
            realStr = Const.IMG_BASE_URL + detailData.getPicture().get(0);
            mapStr = Const.IMG_BASE_URL + detailData.getPicture().get(1);
            Glide.with(context)
                    .load(realStr)
                    .thumbnail(0.1f)
                    .into(real);
            Glide.with(context)
                    .load(mapStr)
                    .thumbnail(0.1f)
                    .into(map);
            content.setText(detailData.getContent());
        }

        @Override
        public void onClick(View v) {
            List<String> url = new ArrayList<>();
            url.add(realStr);
            url.add(mapStr);
            int i = v.getId();
            if (i == R.id.riv_report_map) {
                PhotoViewerActivityKt.start(context, url, 1);

            } else if (i == R.id.riv_report_real) {
                PhotoViewerActivityKt.start(context, url, 0);

            } else if (i == R.id.tv_report_context || i == R.id.iv_report_arrow) {
                listener.isClick(getLayoutPosition());

            }
        }
    }

    public interface ClickItemListener {
        void isClick(int pos);
    }
}
