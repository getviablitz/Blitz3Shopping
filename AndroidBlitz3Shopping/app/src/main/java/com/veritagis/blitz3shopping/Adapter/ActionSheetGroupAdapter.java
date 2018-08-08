//package com.veritagis.blitz3social.Adapter;
//
//import android.app.Activity;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//
//
//import com.veritagis.blitz3social.R;
//
//import java.util.ArrayList;
//
//public class ActionSheetGroupAdapter extends RecyclerView.Adapter<ActionSheetGroupAdapter.MyViewHolder> {
//
//    private Activity mContext;
//    private List<WebGroupItem> promoList;
//    private int type, feed;
//
//    public ActionSheetGroupAdapter(Activity mContext, List<WebGroupItem> moviesList, int type, int feed) {
//        this.promoList = moviesList;
//        this.mContext = mContext;
//        this.type = type;
//        this.feed = feed;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.custom_action_sheet, parent, false);
////        view.setMinimumWidth(parent.getMeasuredWidth() / 3);
//////    MyViewHolder subActivityViewHolder = new MyViewHolder(view);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        final WebGroupItem modelNew = promoList.get(position);
//        holder.ivImage.setImageResource(modelNew.resId);
//
//        holder.ivImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                WebGroupItem modelNew = promoList.get(position);
////                if (position < 2) {
////                    //First pane
////                    UserData.webGroupItemLeft = modelNew;
////                } else if (position < 4) {
////                    //second pane
////                    UserData.webGroupItemMiddle = modelNew;
////                } else {
////                    //third pane
////                    UserData.webGroupItemRight = modelNew;
////                }
////                LandscapeBrowsingActivityV2.webFeedGroupLandscape.dismiss();
//
//                //
//                WebGroupItem modelNew = promoList.get(position);
//                if (feed == 1) {
//                    //First pane
//                    UserData.webGroupItemLeft = modelNew;
//                } else if (feed == 2) {
//                    //second pane
//                    UserData.webGroupItemMiddle = modelNew;
//                } else if (feed == 3) {
//                    //third pane
//                    UserData.webGroupItemRight = modelNew;
//                }
//                LandscapeBrowsingActivityV2.webFeedGroupLandscape.dismiss();
//            }
//        });
//
//        if (WebFeedGroupLandscape.selectedList.contains(modelNew)) {
//            holder.mCheck.setChecked(true);
//        } else {
//            holder.mCheck.setChecked(false);
//        }
//        holder.mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                modelNew.check = b;
//                WebGroupItem modelNew = promoList.get(position);
//                if (b) {
//                    if (WebFeedGroupLandscape.selectedList.size() < 3) {
//                        WebFeedGroupLandscape.selectedList.add(modelNew);
//                    } else {
//                        try {
//                            WebGroupItem itemRemoved = WebFeedGroupLandscape.selectedList.remove(0);
//                            WebFeedGroupLandscape.selectedList.add(modelNew);
//
//                            for (int i = 0; i < promoList.size(); i++) {
//                                if (promoList.get(i).name.equals(itemRemoved.name)) {
//                                    notifyItemChanged(i);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
////                else {
////                    WebFeedGroupLandscape.selectedList.remove(modelNew);
////                }
//                //
//                try {
//                    WebGroupItem item1 = null;
//                    try {
//                        item1 = WebFeedGroupLandscape.selectedList.get(0);
//                    } catch (Exception e) {
//                    }
//                    WebGroupItem item2 = null;
//                    try {
//                        item2 = WebFeedGroupLandscape.selectedList.get(1);
//                    } catch (Exception e) {
//                    }
//                    WebGroupItem item3 = null;
//                    try {
//                        item3 = WebFeedGroupLandscape.selectedList.get(2);
//                    } catch (Exception e) {
//                    }
////                    Log.i("Feed selected", item1.name + "\n" + item2.name + "\n" + item3.name + "\n");
//                    boolean left = false, middle = false, right = false;
//                    if (type == 1) {
//                        if (item1 != null) {
//                            left = checkDefault(item1);
//                            PrefUtil.putString(mContext, "socialFeedLeft", item1.url);
//                        }
//                        if (item2 != null) {
//                            middle = checkDefault(item2);
//                            PrefUtil.putString(mContext, "socialFeedMiddle", item2.url);
//                        }
//                        if (item3 != null) {
//                            right = checkDefault(item3);
//                            PrefUtil.putString(mContext, "socialFeedRight", item3.url);
//                        }
//                        if (left && middle && right)
//                            PrefUtil.putBoolean(mContext, "socialGroupChanged", false);
//                        else
//                            PrefUtil.putBoolean(mContext, "socialGroupChanged", true);
//                    } else if (type == 2) {
//                        if (item1 != null) {
//                            left = checkDefault(item1);
//                            PrefUtil.putString(mContext, "shopFeedLeft", item1.url);
//                        }
//                        if (item2 != null) {
//                            middle = checkDefault(item2);
//                            PrefUtil.putString(mContext, "shopFeedMiddle", item2.url);
//                        }
//                        if (item3 != null) {
//                            right = checkDefault(item3);
//                            PrefUtil.putString(mContext, "shopFeedRight", item3.url);
//                        }
//                        if (left && middle && right)
//                            PrefUtil.putBoolean(mContext, "eComGroupChanged", false);
//                        else PrefUtil.putBoolean(mContext, "eComGroupChanged", true);
//                    } else if (type == 3) {
//                        if (item1 != null) {
//                            left = checkDefault(item1);
//                            PrefUtil.putString(mContext, "newsFeedLeft", item1.url);
//                        }
//                        if (item2 != null) {
//                            middle = checkDefault(item2);
//                            PrefUtil.putString(mContext, "newsFeedMiddle", item2.url);
//                        }
//                        if (item3 != null) {
//                            right = checkDefault(item3);
//                            PrefUtil.putString(mContext, "newsFeedRight", item3.url);
//                        }
//                        if (left && middle && right)
//                            PrefUtil.putBoolean(mContext, "newsGroupChanged", false);
//                        else
//                            PrefUtil.putBoolean(mContext, "newsGroupChanged", true);
//                    }
//                } catch (Exception e) {
//                    Log.i("Feed selected", "Error!");
//                    e.printStackTrace();
//                }
//            }
//
//        });
//
//
////        if (position > prevPosition) {
////            animate(holder, true);
////        } else {
////            animate(holder, false);
////        }
////        prevPosition = position;
//    }
//
//    private boolean checkDefault(WebGroupItem item) {
//        if (type == 1)
//            if (item.url.equals(mContext.getString(R.string.facebook_url))
//                    || item.url.equals(mContext.getString(R.string.twitter_url))
//                    || item.url.equals(mContext.getString(R.string.insta_url)))
//                return true;
//        if (type == 2)
//            if (item.url.equals(mContext.getString(R.string.amazon_url))
//                    || item.url.equals(mContext.getString(R.string.alibaba_url))
//                    || item.url.equals(mContext.getString(R.string.walmart_url)))
//                return true;
//        if (type == 3)
//            if (item.url.equals(mContext.getString(R.string.foxnews_url))
//                    || item.url.equals(mContext.getString(R.string.bbc_url))
//                    || item.url.equals(mContext.getString(R.string.cnn_url)))
//                return true;
//        return false;
//    }
//
//    @Override
//    public int getItemCount() {
//        return promoList.size();
//    }
//
//    public void addItem(WebGroupItem s) {
//        promoList.add(s);
//        notifyItemInserted(promoList.size());
//    }
//
//    public void removeItem(WebGroupItem s) {
//        int pos = promoList.indexOf(s);
//        if (pos != -1 && promoList.size() > pos) {
//            promoList.remove(pos);
//            notifyItemRemoved(pos);
//        }
//    }
//
//    public void removeItem(int pos) {
//        if (pos < promoList.size()) {
//            promoList.remove(pos);
//            notifyItemRemoved(pos);
//        }
//    }
//
//    private void updateViews() {
//        ArrayList<WebGroupItem> mList = new ArrayList<>(WebFeedGroupLandscape.selectedList);
//        WebFeedGroupLandscape.selectedList.clear();
//        notifyDataSetChanged();
//        WebFeedGroupLandscape.selectedList = mList;
//        for (int i = 0; i < promoList.size(); i++) {
//            for (int j = 0; j < WebFeedGroupLandscape.selectedList.size(); j++) {
//                if (promoList.get(i).name.equals(WebFeedGroupLandscape.selectedList.get(j).name)) {
//                    notifyItemChanged(i);
//                }
//            }
//        }
//    }
//
////    void animate(RecyclerView.ViewHolder holder, boolean goesDown) {
////        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 300 : -300, 0);
////        animatorTranslateY.setDuration(1000);
////        animatorTranslateY.start();
////    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivImage;
//        CheckBox mCheck;
//
//        public MyViewHolder(View view) {
//            super(view);
//            ivImage = view.findViewById(R.id.ivImage);
//            mCheck = view.findViewById(R.id.mCheck);
//        }
//    }
//
//}