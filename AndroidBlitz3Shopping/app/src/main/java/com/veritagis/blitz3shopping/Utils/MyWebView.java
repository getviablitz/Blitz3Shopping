package com.veritagis.blitz3shopping.Utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.Toast;

public class MyWebView extends WebView {
    GestureDetector.SimpleOnGestureListener gestListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent event) {
            return true;
        }

        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            //if (event1.getRawX() > event2.getRawX()) {
            //    show_toast("swipe left");
            //} else {
            //    show_toast("swipe right");
            //}
            //you can trace any touch events here
            return true;
        }
    };
    private Context context;
    private GestureDetector gestDetector;

    public MyWebView(Context context) {
        super(context);

        this.context = context;
        gestDetector = new GestureDetector(context, gestListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestDetector.onTouchEvent(event);
    }

    void show_toast(final String text) {
        Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        t.show();
    }
}