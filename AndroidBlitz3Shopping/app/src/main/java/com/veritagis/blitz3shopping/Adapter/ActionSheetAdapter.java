package com.veritagis.blitz3shopping.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.veritagis.blitz3shopping.Activities.ActionSheetPopup;
import com.veritagis.blitz3shopping.R;
import com.veritagis.blitz3shopping.Utils.ActionSheetGroupItem;
import com.veritagis.blitz3shopping.Utils.PrefUtil;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ActionSheetAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context mContext;
    ArrayList<ActionSheetGroupItem> mList = new ArrayList<>();


    public ActionSheetAdapter(Context mContext, ArrayList<ActionSheetGroupItem> mList) {
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        Log.i("repeat ", "call getCount()");
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.action_sheet_group_item, parent, false);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.checkbox = convertView.findViewById(R.id.mCheck);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ActionSheetGroupItem modelNew = mList.get(position);
        viewHolder.tv_name.setText(String.valueOf(modelNew.name));

        if (ActionSheetPopup.selectedList.contains(modelNew)) {
            viewHolder.checkbox.setChecked(true);
            modelNew.setPosition(position);
        } else {
            viewHolder.checkbox.setChecked(false);
        }
        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                modelNew.check = b;
                ActionSheetGroupItem modelNew = mList.get(position);
                if (b) {
                    if (ActionSheetPopup.selectedList.size() < 3) {
                        ActionSheetPopup.selectedList.add(modelNew);
                    } else {
                        try {
                            ActionSheetGroupItem itemRemoved = ActionSheetPopup.selectedList.remove(0);
                            ActionSheetPopup.selectedList.add(modelNew);

                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).name.equals(itemRemoved.name)) {
                                    notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (ActionSheetPopup.selectedList.contains(modelNew)) {
                        viewHolder.checkbox.setChecked(true);
                    }
                    //  ActionSheetPopup.selectedList.remove(modelNew);
           //         viewHolder.checkbox.setChecked(true);
                }
                //
                try {
                    ActionSheetGroupItem item1 = null;
                    try {
                        item1 = ActionSheetPopup.selectedList.get(0);
                    } catch (Exception e) {
                    }
                    ActionSheetGroupItem item2 = null;
                    try {
                        item2 = ActionSheetPopup.selectedList.get(1);
                    } catch (Exception e) {
                    }
                    ActionSheetGroupItem item3 = null;
                    try {
                        item3 = ActionSheetPopup.selectedList.get(2);
                    } catch (Exception e) {
                    }
//                    Log.i("Feed selected", item1.name + "\n" + item2.name + "\n" + item3.name + "\n");
                    boolean left = false, middle = false, right = false;

                    if (item1 != null) {
                        left = checkDefault(item1);
                        PrefUtil.putString(mContext, "socialFeedLeft", item1.url);
                    }
                    if (item2 != null) {
                        middle = checkDefault(item2);
                        PrefUtil.putString(mContext, "socialFeedMiddle", item2.url);
                    }
                    if (item3 != null) {
                        right = checkDefault(item3);
                        PrefUtil.putString(mContext, "socialFeedRight", item3.url);
                    }
                    if (left && middle && right)
                        PrefUtil.putBoolean(mContext, "socialGroupChanged", false);
                    else
                        PrefUtil.putBoolean(mContext, "socialGroupChanged", true);

                } catch (Exception e) {
                    Log.i("Feed selected", "Error!");
                    e.printStackTrace();
                }
            }

        });


        return convertView;
    }

    private boolean checkDefault(ActionSheetGroupItem item) {

        if (item.url.equals(mContext.getString(R.string.bestbuy_url))
                || item.url.equals(mContext.getString(R.string.amazon_url))
                || item.url.equals(mContext.getString(R.string.alibaba_url)))
            return true;
        else
            return false;
    }

    class ViewHolder {
        private TextView tv_name;
        private CheckBox checkbox;

    }

}

