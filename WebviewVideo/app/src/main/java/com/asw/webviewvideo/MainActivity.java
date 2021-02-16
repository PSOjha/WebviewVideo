package com.asw.webviewvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    private JavaScriptInterface javaScriptInterFace;

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress_bar_home);

        if (!isNetworkAvailable()) {

            AlertDialog.Builder a_builder1 = new AlertDialog.Builder(this);
            a_builder1.setMessage("No Internet Connection Please Check Internet Connection !!!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        Intent iLoginVendor = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(iLoginVendor);
                        MainActivity.this.finishAffinity();
                    });

            AlertDialog alert = a_builder1.create();
            alert.setTitle("YouTube");
            alert.show();
        }
        else {
            if (Build.VERSION.SDK_INT >= 19) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            assert webView != null;
            WebSettings webSettings = webView.getSettings();
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

            javaScriptInterFace = new JavaScriptInterface(this);
            webView.addJavascriptInterface(javaScriptInterFace, "AndroidFunction");
            webSettings.setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setLoadsImagesAutomatically(true);

            webSettings.setAllowFileAccess(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

            if (Build.VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(0);
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else if (Build.VERSION.SDK_INT >= 19) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                    try {
                        webView.stopLoading();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    webView.loadUrl("about:blank");
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Check your internet connection and try again.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });

                    alertDialog.show();
                    super.onReceivedError(webView, errorCode, description, failingUrl);
                }
            });

            //Main Code For Landscape Video

            if (savedInstanceState == null) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("https://www.youtube.com/");
                    }
                });
            }

            webView.setWebChromeClient(new WebChromeClient() {

                private View mCustomView;
                private WebChromeClient.CustomViewCallback mCustomViewCallback;
                protected FrameLayout mFullscreenContainer;
                private int mOriginalOrientation;
                private int mOriginalSystemUiVisibility;

                // ChromeClient() {}

                public Bitmap getDefaultVideoPoster() {
                    if (mCustomView == null) {
                        return null;
                    }
                    return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
                }

                public void onHideCustomView() {
                    ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
                    this.mCustomView = null;
                    getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                    setRequestedOrientation(this.mOriginalOrientation);
                    this.mCustomViewCallback.onCustomViewHidden();
                    this.mCustomViewCallback = null;
                }

                public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
                    if (this.mCustomView != null) {
                        onHideCustomView();
                        return;
                    }
                    this.mCustomView = paramView;
                    this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                    this.mOriginalOrientation = getRequestedOrientation();
                    this.mCustomViewCallback = paramCustomViewCallback;
                    ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                    getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }

                public void onProgressChanged(WebView view, int progress) {
//                    getActivity().setProgress(progress * 100);
                    if (progress == 100)
                        progressBar.setVisibility(View.GONE);
                }
            });

            webView.setOnKeyListener((view, keyCode, keyEvent) -> {
                if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
                }
                return false;
            });

        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}