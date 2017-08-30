package com.mredrock.freshmanspecial.view.JunxunFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.FengcaiBeans.JunxunpicBeans;
import com.mredrock.freshmanspecial.beans.FengcaiBeans.JunxunvideoBeans;
import com.mredrock.freshmanspecial.model.HttpModel;
import com.mredrock.freshmanspecial.units.ScreenUnit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by zia on 17-8-8.
 */

public class FengcaiAdapter extends RecyclerView.Adapter<FengcaiAdapter.MyViewHolder> {

    private static final int JUNXUN_TUPIAN = 1;
    private static final int JUNXUN_SHIPING = 3;
    private static final int JUNXUN_TUIJIAN = 5;

    private Context context = null;
    private JunxunRecyclerAdapter adapter = null;


    public FengcaiAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == JUNXUN_TUPIAN) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_junxunpic, parent, false);
            return new MyViewHolder(view, JUNXUN_TUPIAN);
        } else if (viewType == JUNXUN_SHIPING) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_junxinvideo, parent, false);
            return new MyViewHolder(view, JUNXUN_SHIPING);
        } else if (viewType == JUNXUN_TUIJIAN) {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_junxunrecommend, parent, false);
            return new MyViewHolder(view, JUNXUN_TUIJIAN);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.special_2017_item_fengcaidecorte, parent, false);
            return new MyViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.textView.setText("军训图片");
                break;
            case 1://图片
                final JunxunRecyclerAdapter picAdapter = new JunxunRecyclerAdapter(context, JunxunRecyclerAdapter.TUPIAN);
                holder.junxunpicRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.junxunpicRecycler.setAdapter(picAdapter);
                HttpModel.bulid()
                        .getJunxunpic()
                        .subscribe(new Observer<JunxunpicBeans>() {
                            List<String> titleList = new ArrayList<>();
                            List<String> urlList = new ArrayList<>();

                            @Override
                            public void onComplete() {
                                picAdapter.setPicList(titleList, urlList);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("FengcaiAdapter", "request error");
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(JunxunpicBeans junxunpicBeans) {
                                Log.d("FengcaiAdapter", junxunpicBeans.getData().getTitle().get(0));
                                titleList.addAll(junxunpicBeans.getData().getTitle());
                                urlList.addAll(junxunpicBeans.getData().getUrl());
                                onComplete();
                            }
                        });

                break;
            case 2:
                holder.textView.setText("军训视频");
                break;
            case 3://视频
                HttpModel.bulid().getJunxunvideo()
                        .flatMap(beans -> Observable.fromIterable(beans.getData()))
                        .subscribe(new Observer<JunxunvideoBeans.DataBean>() {
                            List<String> picList = new ArrayList<String>();
                            List<String> urlList = new ArrayList<String>();
                            List<String> titleList = new ArrayList<String>();

                            @Override
                            public void onComplete() {
                                int w = ScreenUnit.bulid(context).getPxWide() / 2 - 5;
                                holder.shiping_text_left.setText(titleList.get(0));
                                holder.shiping_text_right.setText(titleList.get(1));
                                Log.d("fengcai", urlList.get(0));
                                Log.d("fengcai", urlList.get(1));
                                Glide.with(context).load(picList.get(0)).override(w, w / 16 * 10).into(holder.shiping_left);
                                Glide.with(context).load(picList.get(1)).override(w, w / 16 * 10).into(holder.shiping_right);
                                holder.shiping_left.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(urlList.get(0));
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        context.startActivity(intent);
                                    }
                                });
                                holder.shiping_right.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(urlList.get(1));
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        context.startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("FengcaiAdapter", "request error");
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(JunxunvideoBeans.DataBean dataBean) {
                                Log.d("FengcaiAdapter", dataBean.getTitle());
                                picList.add(dataBean.getCover());
                                urlList.add(dataBean.getUrl());
                                titleList.add(dataBean.getTitle());
                            }
                        });
                break;
            case 4:
                holder.textView.setText("军歌推荐");
                break;
            case 5://推荐
                adapter = new JunxunRecyclerAdapter(context, JunxunRecyclerAdapter.TUIJIAN);
                holder.junxunrecommendRecycler.setLayoutManager(new GridLayoutManager(context, 2));
                holder.junxunrecommendRecycler.setAdapter(adapter);
                List<String> musicList = new ArrayList<>();
                List<String> authorList = new ArrayList<>();
                musicList.add("强军战歌");
                authorList.add("阎维文");
                musicList.add("咱当兵的人");
                authorList.add("刘斌");
                musicList.add("团结就是力量");
                authorList.add("霍勇");
                musicList.add("军中绿花");
                authorList.add("小曾");
                musicList.add("战友还记得吗");
                authorList.add("小曾");
                musicList.add("一二三四歌");
                authorList.add("阎维文");
                musicList.add("75厘米");
                authorList.add("小曾");
                musicList.add("打靶归来");
                authorList.add("阎维文");
                musicList.add("精忠报国");
                authorList.add("屠洪刚");
                musicList.add("我的老班长");
                authorList.add("小曾");
                musicList.add("保卫黄河");
                authorList.add("瞿弦和");
                musicList.add("国际歌");
                authorList.add("张穆庭");
                adapter.setMusicList(musicList, authorList);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return JUNXUN_TUPIAN;
            case 2:
                return 2;
            case 3:
                return JUNXUN_SHIPING;
            case 4:
                return 4;
            case 5:
                return JUNXUN_TUIJIAN;
            default:
                return position;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerView junxunpicRecycler, junxunrecommendRecycler;
        TextView textView, shiping_text_left, shiping_text_right;
        ImageView shiping_left, shiping_right;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case JUNXUN_TUPIAN:
                    junxunpicRecycler = itemView.findViewById(R.id.junxunpic_recycler);
                    break;
                case JUNXUN_SHIPING:
                    shiping_left = itemView.findViewById(R.id.video_left);
                    shiping_right = itemView.findViewById(R.id.video_right);
                    shiping_text_left = itemView.findViewById(R.id.shiping_text_left);
                    shiping_text_right = itemView.findViewById(R.id.shiping_text_right);
                    break;
                case JUNXUN_TUIJIAN:
                    junxunrecommendRecycler = itemView.findViewById(R.id.junxunrecommend_recycler);
                    break;
                default:
                    textView = itemView.findViewById(R.id.fengcai_text);
            }
        }
    }
}
