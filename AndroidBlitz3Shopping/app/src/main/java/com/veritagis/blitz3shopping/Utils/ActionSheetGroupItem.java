package com.veritagis.blitz3shopping.Utils;

/**
 * Created by lsn-dell01 on 15-11-2017.
 */

public class ActionSheetGroupItem {
    public String name;
    public String url;

//    public boolean check;

    public ActionSheetGroupItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public ActionSheetGroupItem(String name, boolean check) {
        this.name = name;
//        this.check = check;
    }

    public ActionSheetGroupItem(String name, boolean check, String url) {
        this.name = name;
//        this.check = check;
        this.url = url;
    }

    public ActionSheetGroupItem() {
    }
}
