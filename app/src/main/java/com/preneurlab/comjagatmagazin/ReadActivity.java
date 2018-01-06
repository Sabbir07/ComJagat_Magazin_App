package com.preneurlab.comjagatmagazin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.preneurlab.comjagatmagazin.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ReadActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        final WebView webView = (WebView) findViewById(R.id.webView2);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);

        //retriving pdf link from each fragment
        Intent intent = getIntent();
        String pdfLink = intent.getStringExtra("PDFLINK");

        if (pdfLink.endsWith(".pdf")) {
            try {
                String urlEncoded = URLEncoder.encode(pdfLink, "UTF-8");
                pdfLink = "http://drive.google.com/viewerng/viewer?embedded=true&url=" + urlEncoded;// using drive viewer
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(pdfLink);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/internet_error.html");

            }

        });

//view pdf using google doc viewer
//        //webView.loadUrl("https://docs.google.com/viewer?url=" + pdf);
//        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfTagLink);
//        //webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webView.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//               webView.loadUrl("file:///android_asset/internet_error.html");
//                Toast.makeText(getApplicationContext(), "No internet connection! Please try again later.", Toast.LENGTH_LONG).show();
//            }
//        });

    }
}
