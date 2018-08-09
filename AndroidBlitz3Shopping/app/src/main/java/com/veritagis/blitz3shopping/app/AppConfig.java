package com.veritagis.blitz3shopping.app;


import com.veritagis.blitz3shopping.R;
import com.veritagis.blitz3shopping.Utils.MyApplication;

/**
 * Created by LSN-ANDROID2 on 09-08-2018.
 */

public class AppConfig {
    public static String searchUrl[] = {MyApplication.getInstance().getApplicationContext().getString(R.string.bestbuy_url_search),
            MyApplication.getInstance().getApplicationContext().getString(R.string.amazon_url_search),
            MyApplication.getInstance().getApplicationContext().getString(R.string.alibaba_url_search),
            MyApplication.getInstance().getApplicationContext().getString(R.string.walmart_url_search),
            MyApplication.getInstance().getApplicationContext().getString(R.string.costco_url_search),
            MyApplication.getInstance().getApplicationContext().getString(R.string.nordstrom_url_search)};

}
