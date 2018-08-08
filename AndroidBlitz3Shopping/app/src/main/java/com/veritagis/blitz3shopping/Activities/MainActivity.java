package com.veritagis.blitz3shopping.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.veritagis.blitz3shopping.R;
import com.veritagis.blitz3shopping.Utils.ActionSheetGroupItem;
import com.veritagis.blitz3shopping.Utils.ConnectivityReceiver;
import com.veritagis.blitz3shopping.Utils.MyApplication;
import com.veritagis.blitz3shopping.Utils.PrefUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener, EasyPermissions.PermissionCallbacks {
    private Button btn_go, btn_menu;
    private RelativeLayout activity_main;
    boolean isScreenShotCapture = false;
    private static final int RC_GALLERY_PERM = 124;
    private ShareDialog shareDialog;
    private Dialog feedShareDialog;
    private String web_irl1, web_irl2, web_irl3, facebook_post_url;
    private ProgressBar mProgressBar;

    public static boolean isPackageInstalled(Context c, String targetPackage) {
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new TwitterCore(authConfig));

        // Initialize facebook SDK.
        FacebookSdk.sdkInitialize(getApplicationContext());
        btn_go = findViewById(R.id.btn_go);
        btn_menu = findViewById(R.id.btn_menu);

        activity_main = findViewById(R.id.activity_main);

        MyApplication.getInstance().setConnectivityListener(this);
        checkConnection();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasGalleryPermission()) {
                EasyPermissions.requestPermissions(
                        MainActivity.this,
                        getString(R.string.rationale_gallery),
                        RC_GALLERY_PERM,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        Intent intent = getIntent();
        isScreenShotCapture = intent.getBooleanExtra("isScreenShotCapture", false);
        if (isScreenShotCapture) {
            String capturedScreenShotLocation = intent.getStringExtra("image_path");
            web_irl1 = intent.getStringExtra("web_irl1");
            web_irl2 = intent.getStringExtra("web_irl2");
            web_irl3 = intent.getStringExtra("web_irl3");
            showFeedShareDialog(capturedScreenShotLocation);
        }
        btn_go.setOnClickListener(new View.OnClickListener() { // go to landscape mode activity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
                intent.putExtra("from_go", "from_go");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetPopup actionSheetPopup = new ActionSheetPopup(MainActivity.this);
                actionSheetPopup.show();
            }
        });

    }
    /**
     * method share image to corresponding social media
     */
    private void showFeedShareDialog(final String imageLocation) {

        feedShareDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        feedShareDialog.setContentView(R.layout.custom_feed_share_dialog);
        feedShareDialog.setCancelable(false);
        ToggleButton toggleBtnFacebook = feedShareDialog.findViewById(R.id.idBtnFacebook);
        ToggleButton toggleBtnTwitter = feedShareDialog.findViewById(R.id.idBtnTwitter);
        ToggleButton toggleBtnInstagram = feedShareDialog.findViewById(R.id.idBtnInstagram);
        ImageView imgBtnCloseDialog = feedShareDialog.findViewById(R.id.cancel_btn);
        mProgressBar = feedShareDialog.findViewById(R.id.mProgressBar);
        final EditText etInputComment = feedShareDialog.findViewById(R.id.idInputComment);
        ImageView feedImageView = feedShareDialog.findViewById(R.id.idImageFeed);
        Glide.with(MyApplication.getInstance().getApplicationContext())
                .load(imageLocation)
                .crossFade().into(feedImageView);

        getURLFromServer(imageLocation);

        toggleBtnFacebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (isPackageInstalled(MainActivity.this, "com.facebook.katana")) {
                        String comment = etInputComment.getText().toString().trim();
                        Bitmap bitmap = BitmapFactory.decodeFile(imageLocation);
                        shareFeedToFacebook(comment, bitmap);
                    } else {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana&hl=en")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana&hl=en")));
                        }
                    }
                } else {

                }

            }
        });

        toggleBtnInstagram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                    if (intent != null) {
                        String comment = etInputComment.getText().toString().trim();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setPackage("com.instagram.android");
                        try {
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(
                                    MediaStore.Images.Media.insertImage(getContentResolver(), imageLocation, "Blitz3Social", comment)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        shareIntent.setType("image/jpeg");

                        startActivity(shareIntent);
                    } else {
                        // bring user to the market to download the app.
                        // or let them choose an app?
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
                        startActivity(intent);
                    }
                } else {

                }

            }
        });

        toggleBtnTwitter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    File imgFile = new File(imageLocation);
                    Uri myImageUri = Uri.fromFile(imgFile);
                    String comment = etInputComment.getText().toString().trim();
                    if (facebook_post_url != null) {
                        URL url = null;
                        try {
                            url = new URL(facebook_post_url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                                .image(myImageUri)
                                .url(url)
                                .text(comment);
                        builder.show();

                    } else {
                        TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                                .image(myImageUri)
                                .text(comment);
                        builder.show();
                    }
                } else {

                }

            }
        });


        imgBtnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File file = new File(imageLocation);
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                feedShareDialog.dismiss();
            }
        });
        feedShareDialog.show();
    }

    private void shareFeedToFacebook(String comment, Bitmap bitmap) {


        shareDialog = new ShareDialog(MainActivity.this);
        if (facebook_post_url != null) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(facebook_post_url))
                    .build();
            shareDialog.show(content);
        } else if (bitmap != null) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (hasGalleryPermission()) {
                launchLandscapeScreen();
            } else {
                EasyPermissions.requestPermissions(
                        MainActivity.this,
                        getString(R.string.rationale_gallery),
                        RC_GALLERY_PERM,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        //   String message ="";
        int color;
        if (isConnected) {
            //     message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            String message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(activity_main, "" +
                            message, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean hasGalleryPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void getURLFromServer(String imageLocation) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        String base64Image = getBase64FromFile(imageLocation);
        Log.i("base64Image", base64Image);
        String url = "http://social.blitzit.io/mobile/create/";

        Ion.with(MainActivity.this)
                .load(url)
                .addHeader("Accept", "application/json")
                .setBodyParameter("featureimage", base64Image)
                .setBodyParameter("imageurl1", web_irl1)
                .setBodyParameter("imageurl2", web_irl2)
                .setBodyParameter("imageurl3", web_irl3)
                .setBodyParameter("titlecomment", "")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        System.out.println("response : " + result);
                        try {
                            if (result != null) {
                                JSONObject jsonObject = new JSONObject(result);
                                String url = jsonObject.getString("url");
                                if (url != null) {
                                    mProgressBar.setVisibility(View.GONE);
                                    facebook_post_url = "http://social.blitzit.io/share/" + url;
                                    Log.i("facebook_post_url", facebook_post_url);
                                }
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    public String getBase64FromFile(String path) {
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] baat = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baat = baos.toByteArray();
            encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeString;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        // for rotation
        new CountDownTimer(5000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                int orientation = getResources().getConfiguration().orientation;
                if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
                    Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }.start();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    public void launchLandscapeScreen(){
        ActionSheetPopup.selectedList.clear();
        String webUrl1 = PrefUtil.getString(getApplicationContext(), "socialFeedLeft", "");
        String webUrl2 = PrefUtil.getString(getApplicationContext(), "socialFeedMiddle", "");
        String webUrl3 = PrefUtil.getString(getApplicationContext(), "socialFeedRight", "");
        ArrayList<ActionSheetGroupItem> mList=getData();
        for (ActionSheetGroupItem modelNew : mList) {
            if (!TextUtils.isEmpty(webUrl1) && modelNew.url.equals(webUrl1)) {
                ActionSheetPopup.selectedList.add(modelNew);
            }
            if (!TextUtils.isEmpty(webUrl2) && modelNew.url.equals(webUrl2)) {
                ActionSheetPopup.selectedList.add(modelNew);
            }
            if (!TextUtils.isEmpty(webUrl3) && modelNew.url.equals(webUrl3)) {
                ActionSheetPopup.selectedList.add(modelNew);
            }
        }
        Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private ArrayList<ActionSheetGroupItem> getData() {

//        Social
//        1)Facebook
//        2)Twitter
//        3)Insta
//        4)LinkedIn
//        5)Google+
//        6)Quora
//
//        E-Commerce
//        1)Best-Buy
//        2)Amazon
//        3)Alibaba
//        4)Wal-mart
//        5)costco
//        6)Nordstorm
//
//         NewsChannels
//        1)Foxnews
//        2)BBC News
//        3)CNN News
//        4)CNBC News
//        5)Buzz feed
//        6)Yahoo News

        ArrayList<ActionSheetGroupItem> mList = new ArrayList<>();

        //social
        mList.add(new ActionSheetGroupItem("Best-Buy", true, getApplicationContext().getString(R.string.bestbuy_url)));
        mList.add(new ActionSheetGroupItem("Amazon", true, getApplicationContext().getString(R.string.amazon_url)));
        mList.add(new ActionSheetGroupItem("Alibaba", true, getApplicationContext().getString(R.string.alibaba_url)));
        mList.add(new ActionSheetGroupItem("WalMart", getApplicationContext().getString(R.string.walmart_url)));
        mList.add(new ActionSheetGroupItem("Costco", getApplicationContext().getString(R.string.costco_url)));
        mList.add(new ActionSheetGroupItem("Nordstorm", getApplicationContext().getString(R.string.nordstrom_url)));

        return mList;
    }}
