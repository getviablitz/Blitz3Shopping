package com.veritagis.blitz3shopping.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.veritagis.blitz3shopping.R;


public class WebViewActivity extends Activity {
    private WebView mWebView;
    private Button btnBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.mWebView);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progress_loading);

        setWebView(mWebView, 1, WebViewLandscapePopup.url);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebViewActivity.this, LandScapeFeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(WebViewActivity.this, LandScapeFeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    @Override
    public void onBackPressed() {
        //remove call to the super class
        //super.onBackPressed();
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
