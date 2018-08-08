package com.veritagis.blitz3shopping.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.veritagis.blitz3shopping.R;


public class WebViewLandscapePopup extends Dialog implements
        View.OnClickListener {
    public static String url;
    private Activity mContext;
    private WebView mWebView;
    private ProgressBar progressBar;

    public WebViewLandscapePopup(Activity mContext, String url) {
        super(mContext);
        this.mContext = mContext;
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview_landscape);

        getWindow().setGravity(Gravity.CENTER);
//        getWindow().setWindowAnimations(R.style.DialogSlideAnim);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setDimAmount(0.3f);
        setCanceledOnTouchOutside(false);

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Log.v("width", width + "");
//        if (type == 3) {
//            getWindow().setLayout(width, (4 * height) / 5);
//        } else
            getWindow().setLayout(width, height);//4

        mWebView = findViewById(R.id.mWebView);
        progressBar = findViewById(R.id.progress_loading);
        ImageView btnCancel = findViewById(R.id.cancel_btn);
        btnCancel.setOnClickListener(this);

     setWebView(mWebView,1,url);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cancel_btn: {
                dismiss();
                break;
            }

            default:
                break;
        }

    }

    private void setWebView(WebView mWebView, int type, String url) {

        if (Build.VERSION.SDK_INT < 18) {
            mWebView.clearView();
        } else {
            mWebView.loadUrl("about:blank");
        }
        CookieManager.getInstance().setAcceptCookie(true);// Enable Cookies
        mWebView.getSettings().setJavaScriptEnabled(true);// Enable Java Script
        mWebView.setWebViewClient(new HelloWebViewClient(progressBar));
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
        String appCachePath = mContext.getApplicationContext().getCacheDir()
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

    private class HelloWebViewClient extends WebViewClient {
        boolean resizeFit = false;
        ProgressBar progressBar;

        public HelloWebViewClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.GONE);

        }
    }
}
