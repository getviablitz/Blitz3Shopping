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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener, EasyPermissions.PermissionCallbacks {
    private Button btn_go, btn_menu;
    private RelativeLayout activity_main;
    boolean isScreenShotCapture = false;
    private static final int RC_GALLERY_PERM = 124;
    private ShareDialog shareDialog;
    private Dialog feedShareDialog;
    private String web_irl1, web_irl2, web_irl3, facebook_post_url;
    private ProgressBar mProgressBar;
    private EditText etInputSearch;
    private ToggleButton toggleSearch;
    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
        toggleSearch = findViewById(R.id.idBtnSearch);        etInputSearch = findViewById(R.id.idInputSearchKey);

        MyApplication.getInstance().setConnectivityListener(this);
        checkConnection();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            accessLocationAndStoragePermissions();
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
                launchLandscapeScreen();
            }
        });
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetPopup actionSheetPopup = new ActionSheetPopup(MainActivity.this);
                actionSheetPopup.show();
            }
        });
        toggleSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etInputSearch.setVisibility(View.VISIBLE);
                } else {
                    etInputSearch.setText("");
                    etInputSearch.setVisibility(View.GONE);
                }
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

    @AfterPermissionGranted(RC_GALLERY_PERM)
    public void accessLocationAndStoragePermissions() {
        if (!hasGalleryPermission()) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    RC_GALLERY_PERM,
                    permissionsRequired);
        }
    }

    private boolean hasGalleryPermission() {
        return EasyPermissions.hasPermissions(MyApplication.getInstance().getApplicationContext(), permissionsRequired);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //layoutDeniedPermissionLayout.setVisibility(View.GONE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //layoutDeniedPermissionLayout.setVisibility(View.VISIBLE);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
                    launchLandscapeScreen();
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


    public void launchLandscapeScreen() {
        String searchKey = etInputSearch.getText().toString().trim();
        Intent intent = new Intent(MainActivity.this, LandScapeFeedActivity.class);
        intent.putExtra("searchKey", searchKey);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


