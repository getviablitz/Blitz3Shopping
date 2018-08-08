package com.veritagis.blitz3shopping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.veritagis.blitz3shopping.Adapter.PopupListAdapter;
import com.veritagis.blitz3shopping.R;
import com.veritagis.blitz3shopping.Utils.ActionSheetGroupItem;
import com.veritagis.blitz3shopping.Utils.OnSwipeTouchListener;
import com.veritagis.blitz3shopping.Utils.UserData;

import java.util.ArrayList;

public class LandScapeFeedActivity extends Activity {
    WebViewLandscapePopup webViewLandscapePopup;
    private WebView webview_facebook, webview_twitter, webview_instagram;
    //  private CustomWebView webview_twitter;
    private LinearLayout ll_facebook_feed, ll_twitter_feed, ll_instagram_feed;
    private String from_go;
    private FrameLayout framelayout_twitter;
    private RelativeLayout rl_left, rl_middle, rl_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_land_scape_feed);


        webview_facebook = findViewById(R.id.webview_facebook);
        webview_twitter = findViewById(R.id.webview_twitter);
        webview_instagram = findViewById(R.id.webview_instagram);

        webview_facebook.setVerticalScrollBarEnabled(true);
        webview_facebook.setHorizontalScrollBarEnabled(true);

        webview_twitter.setVerticalScrollBarEnabled(true);
        webview_twitter.setHorizontalScrollBarEnabled(true);

        webview_instagram.setVerticalScrollBarEnabled(true);
        webview_instagram.setHorizontalScrollBarEnabled(true);

        ll_facebook_feed = findViewById(R.id.ll_facebook_feed);
        ll_twitter_feed = findViewById(R.id.ll_twitter_feed);
        ll_instagram_feed = findViewById(R.id.ll_instagram_feed);

        rl_left = findViewById(R.id.rl_left);
        rl_middle = findViewById(R.id.rl_middle);
        rl_right = findViewById(R.id.rl_right);

        //   framelayout_twitter = findViewById(R.id.framelayout_twitter);


        if (ActionSheetPopup.selectedList != null && ActionSheetPopup.selectedList.size() > 0) {
            //    for (ActionSheetGroupItem actionSheetGroupItem : ActionSheetPopup.selectedList) { }
            ActionSheetGroupItem actionSheetGroupItem = ActionSheetPopup.selectedList.get(0);
            setWebView(webview_facebook, 1, actionSheetGroupItem.url);
            ActionSheetGroupItem actionSheetGroupItem2 = ActionSheetPopup.selectedList.get(1);
            setWebView(webview_twitter, 2, actionSheetGroupItem2.url);
            ActionSheetGroupItem actionSheetGroupItem3 = ActionSheetPopup.selectedList.get(2);
            setWebView(webview_instagram, 3, actionSheetGroupItem3.url);
        } else {
            setWebView(webview_facebook, 1, getResources().getString(R.string.bestbuy_url));
            setWebView(webview_twitter, 2, getResources().getString(R.string.amazon_url));
            setWebView(webview_instagram, 3, getResources().getString(R.string.alibaba_url));

        }
        if (UserData.fromWebViewPopup) {
            webViewLandscapePopup = new WebViewLandscapePopup(LandScapeFeedActivity.this, UserData.url);
            webViewLandscapePopup.show();
        }

        from_go = getIntent().getStringExtra("from_go");

//        webview_twitter.setGestureDetector(new GestureDetector(getApplicationContext(), MyWebView(getApplicationContext())));

        //  webview_twitter.setGestureDetector(new GestureDetector(getApplicationContext(),new CustomeGestureDetector()));
//        webview_facebook.setGestureDetector(new GestureDetector(getApplicationContext(),new CustomeGestureDetector()));
//        webview_instagram.setGestureDetector(new GestureDetector(getApplicationContext(),new CustomeGestureDetector()));

//        webview_twitter.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                Log.e("touch","touch");
////                Toast.makeText(LandScapeFeedActivity.this, "touch", Toast.LENGTH_SHORT).show();
//                return false;
//                //
//
//            }
//
//        });
        facebookWebSwipeLinstener();
        twitterWebSwipeLinstener();
        instagramWebSwipeLinstener();

    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.expand_left: {
                webViewLandscapePopup = new WebViewLandscapePopup(LandScapeFeedActivity.this, webview_facebook.getUrl());
                webViewLandscapePopup.show();
                break;
            }
            case R.id.expand_middle: {
                webViewLandscapePopup = new WebViewLandscapePopup(LandScapeFeedActivity.this, webview_twitter.getUrl());
                webViewLandscapePopup.show();
                break;
            }
            case R.id.expand_right: {
                webViewLandscapePopup = new WebViewLandscapePopup(LandScapeFeedActivity.this, webview_instagram.getUrl());
                webViewLandscapePopup.show();
                break;
            }
            case R.id.menu_left: {
                populateTimeLinePopupMenu(rl_left, 1);
                break;
            }
            case R.id.menu_middle: {
                populateTimeLinePopupMenu(rl_middle, 2);
                break;
            }
            case R.id.menu_right: {
                populateTimeLinePopupMenu(rl_right, 3);
                break;
            }

            default:
                break;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (webViewLandscapePopup != null && webViewLandscapePopup.isShowing()) {
                UserData.fromWebViewPopup = true;
                UserData.url = WebViewLandscapePopup.url;
                Intent intent = new Intent(LandScapeFeedActivity.this, WebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                UserData.fromWebViewPopup = false;
                Intent intent = new Intent(LandScapeFeedActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //remove call to the super class
// Checks the orientation of the screen
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(LandScapeFeedActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            finish();
        }
        super.onBackPressed();
    }


    private void facebookWebSwipeLinstener() {

        webview_facebook.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeLeft() {

                if (ll_instagram_feed.getVisibility() == View.GONE) {
                    ll_instagram_feed.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams paramFB = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_facebook_feed.setLayoutParams(paramFB);

                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_twitter_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_instagram_feed.setLayoutParams(paramTwitter);
                } else {

                    ll_facebook_feed.setVisibility(View.GONE);

                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_twitter_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_instagram_feed.setLayoutParams(paramTwitter);
                }

            }

            @Override
            public void onSwipeRight() {

                ll_instagram_feed.setVisibility(View.GONE);
                LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.5f
                );
                ll_facebook_feed.setLayoutParams(paramL);

                LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.5f
                );
                ll_twitter_feed.setLayoutParams(paramTwitter);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return super.onTouch(v, event);
            }
        });
    }

    private void twitterWebSwipeLinstener() {
        webview_twitter.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeLeft() {

                if (ll_instagram_feed.getVisibility() == View.GONE) {
                    ll_instagram_feed.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams paramFB = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_facebook_feed.setLayoutParams(paramFB);

                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_twitter_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_instagram_feed.setLayoutParams(paramTwitter);
                } else {

                    ll_facebook_feed.setVisibility(View.GONE);

                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_twitter_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_instagram_feed.setLayoutParams(paramTwitter);
                }

            }

            @Override
            public void onSwipeRight() {
                if (ll_facebook_feed.getVisibility() == View.GONE) {
                    ll_facebook_feed.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams paramFB = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_facebook_feed.setLayoutParams(paramFB);

                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_twitter_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_instagram_feed.setLayoutParams(paramTwitter);
                } else {
                    ll_instagram_feed.setVisibility(View.GONE);
                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_facebook_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_twitter_feed.setLayoutParams(paramTwitter);
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return super.onTouch(v, event);
            }
        });
    }

    private void instagramWebSwipeLinstener() {
        webview_instagram.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeLeft() {

                ll_facebook_feed.setVisibility(View.GONE);

                LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.5f
                );
                ll_twitter_feed.setLayoutParams(paramL);

                LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.5f
                );
                ll_instagram_feed.setLayoutParams(paramTwitter);

            }

            @Override
            public void onSwipeRight() {
                if (ll_facebook_feed.getVisibility() == View.GONE) {
                    ll_facebook_feed.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams paramFB = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_facebook_feed.setLayoutParams(paramFB);

                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_twitter_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                    );
                    ll_instagram_feed.setLayoutParams(paramTwitter);
                } else {
                    ll_instagram_feed.setVisibility(View.GONE);
                    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_facebook_feed.setLayoutParams(paramL);

                    LinearLayout.LayoutParams paramTwitter = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.5f
                    );
                    ll_twitter_feed.setLayoutParams(paramTwitter);
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return super.onTouch(v, event);
            }
        });
    }

    private void populateTimeLinePopupMenu(final RelativeLayout tvAnchor, final int type) {

        //  btnDropdown.setBackgroundResource(R.drawable.up_arrow_gal);

        final ListPopupWindow popupWindow = new ListPopupWindow(this);

//        E-Commerce
//        1)Best-Buy
//        2)Amazon
//        3)Alibaba
//        4)Wal-mart
//        5)costco
//        6)Nordstorm

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Best-Buy");
        stringArrayList.add("Amazon");
        stringArrayList.add("Alibaba");
        stringArrayList.add("WalMart");
        stringArrayList.add("Costco");
        stringArrayList.add("Nordstorm");
        stringArrayList.add("Close");

        PopupListAdapter popupListAdapter = new PopupListAdapter(LandScapeFeedActivity.this, stringArrayList, false);

        popupWindow.setAnchorView(tvAnchor);
        popupWindow.setAdapter(popupListAdapter);
//        popupWindow.setWidth(400); // note: don't use pixels, use a dimen resource
//        popupWindow.setOnItemClickListener(myListener); // the callback for when a list item is selected
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: { // fb
                        if (type == 1) {
                            setWebView(webview_facebook, 1, getResources().getString(R.string.bestbuy_url));
                        } else if (type == 2) {
                            setWebView(webview_twitter, 1, getResources().getString(R.string.bestbuy_url));
                        } else if (type == 3) {
                            setWebView(webview_instagram, 1, getResources().getString(R.string.bestbuy_url));
                        }
                        popupWindow.dismiss();
                        break;
                    }
                    case 1: { // twitter
                        if (type == 1) {
                            setWebView(webview_facebook, 1, getResources().getString(R.string.amazon_url));
                        } else if (type == 2) {
                            setWebView(webview_twitter, 1, getResources().getString(R.string.amazon_url));
                        } else if (type == 3) {
                            setWebView(webview_instagram, 1, getResources().getString(R.string.amazon_url));
                        }
                        popupWindow.dismiss();
                        break;
                    }
                    case 2: { // instagram
                        if (type == 1) {
                            setWebView(webview_facebook, 1, getResources().getString(R.string.alibaba_url));
                        } else if (type == 2) {
                            setWebView(webview_twitter, 1, getResources().getString(R.string.alibaba_url));
                        } else if (type == 3) {
                            setWebView(webview_instagram, 1, getResources().getString(R.string.alibaba_url));
                        }
                        popupWindow.dismiss();
                        break;
                    }
                    case 3: { //linkedin
                        if (type == 1) {
                            setWebView(webview_facebook, 1, getResources().getString(R.string.walmart_url));
                        } else if (type == 2) {
                            setWebView(webview_twitter, 1, getResources().getString(R.string.walmart_url));
                        } else if (type == 3) {
                            setWebView(webview_instagram, 1, getResources().getString(R.string.walmart_url));
                        }
                        popupWindow.dismiss();
                        break;
                    }
                    case 4: { //google plus
                        if (type == 1) {
                            setWebView(webview_facebook, 1, getResources().getString(R.string.costco_url));
                        } else if (type == 2) {
                            setWebView(webview_twitter, 1, getResources().getString(R.string.costco_url));
                        } else if (type == 3) {
                            setWebView(webview_instagram, 1, getResources().getString(R.string.costco_url));
                        }
                        popupWindow.dismiss();
                        break;
                    }
                    case 5: { //quora
                        if (type == 1) {
                            setWebView(webview_facebook, 1, getResources().getString(R.string.nordstrom_url));
                        } else if (type == 2) {
                            setWebView(webview_twitter, 1, getResources().getString(R.string.nordstrom_url));
                        } else if (type == 3) {
                            setWebView(webview_instagram, 1, getResources().getString(R.string.nordstrom_url));
                        }
                        popupWindow.dismiss();
                        break;
                    }
                    case 6: { //close
                        popupWindow.dismiss();
                        break;
                    }

                }
            }
        });
        popupWindow.show();

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }


    private void setWebView(WebView mWebView, int type, String url) {

        if (Build.VERSION.SDK_INT < 18) {
            mWebView.clearView();
        } else {
            mWebView.loadUrl("about:blank");
        }
        CookieManager.getInstance().setAcceptCookie(true);// Enable Cookies
        mWebView.getSettings().setJavaScriptEnabled(true);// Enable Java Script
        mWebView.setWebViewClient(new HelloWebViewClient());
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// Remove
        mWebView.getSettings().setDefaultFontSize(12);// Set Font Size
        mWebView.getSettings().setLoadsImagesAutomatically(true);// Enable Image
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);// Enable Flash
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH); // improves
        mWebView.setBackgroundColor(0x00000000);// Transparent Screen When
        // Loading
        mWebView.getSettings().setBuiltInZoomControls(true);//Set Zoom
        // Controls
        mWebView.getSettings().setDisplayZoomControls(false);//Requires Api
        // 11
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);// Set Cache
        // (8mb)
        String appCachePath = getApplicationContext().getCacheDir()
                .getAbsolutePath();// Set Cache (8mb)
        mWebView.getSettings().setAppCachePath(appCachePath);// Set Cache (8mb)
        mWebView.getSettings().setAllowFileAccess(true);// Set Cache (8mb)
        mWebView.getSettings().setAppCacheEnabled(true);// Set Cache (8mb)
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// Set

        if (type == 1)
            mWebView.loadUrl(url);
        else if (type == 2)
            mWebView.loadUrl(url);
        else if (type == 3)
            mWebView.loadUrl(url);


    }

    @Override
    protected void onResume() {
        super.onResume();
        new CountDownTimer(5000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                int orientation = getResources().getConfiguration().orientation;
                if (Configuration.ORIENTATION_PORTRAIT == orientation) {
                    finish();
                }
            }
        }.start();
    }

    private class HelloWebViewClient extends WebViewClient {
        boolean resizeFit = false;

        public HelloWebViewClient() {
        }

        public HelloWebViewClient(boolean resizeFit) {
            this.resizeFit = resizeFit;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {

            webview.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.setBackgroundColor(Color.WHITE);

        }
    }


}
