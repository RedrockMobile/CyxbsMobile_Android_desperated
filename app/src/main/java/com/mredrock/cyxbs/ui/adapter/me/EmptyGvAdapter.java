package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

import java.util.List;

public class EmptyGvAdapter extends BaseAdapter {

    private Context context;
    private List<String> datas;

    public EmptyGvAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_empty_gv_item, parent, false);
        }
        TextView room = (TextView) convertView.findViewById(R.id.item_empty_gv_room);
        String raw = datas.get(position);
        int start = raw.startsWith("8") ? 3 : 2;
        String text = raw.substring(0, start) + "<font color='#6197FB'>" + raw.substring(start) + "</font>";
        room.setText(Html.fromHtml(text));
        return convertView;
    }
}
