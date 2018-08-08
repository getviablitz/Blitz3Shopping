package com.veritagis.blitz3shopping.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.veritagis.blitz3shopping.R;

import java.util.ArrayList;

public class PopupListAdapter extends BaseAdapter {
    private boolean chat;
    private Activity activity;
    private ArrayList<String> data;
    private LayoutInflater inflater;

    public PopupListAdapter(Activity activity, ArrayList<String> data, boolean chat) {
        this.data = data;
        this.chat = chat;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) convertView = inflater.inflate(R.layout.popup_list_item, null);
        TextView tv = convertView.findViewById(R.id.label);
        View view = convertView.findViewById(R.id.view);
        tv.setText(Html.fromHtml(data.get(position)));
        if (chat && position == 0) {
          //  tv.setTextColor(activity.getResources().getColor(R.color.red));
        } else {
         //   tv.setTextColor(activity.getResources().getColor(R.color.gray_light));
        }
        if (chat) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
