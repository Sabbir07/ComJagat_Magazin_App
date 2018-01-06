package com.preneurlab.comjagatmagazin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.preneurlab.comjagatmagazin.R;

public class DownloadActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Intent intent = getIntent();
        String pdfLink = intent.getStringExtra("PDFLINK");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfLink));
        startActivity(browserIntent);
        finish();
    }
}
