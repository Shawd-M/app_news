package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ReadNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        TextView placeHolderCountry = (TextView) findViewById(R.id.countryName);
        placeHolderCountry.setText(getIntent().getStringExtra("Country"));

        TextView newsDesc = (TextView) findViewById(R.id.newsDesc);
        newsDesc.setText(getIntent().getStringExtra("Desc"));

        TextView newsUrl = (TextView) findViewById(R.id.newsUrl);
        newsUrl.setText(getIntent().getStringExtra("Url"));
        newsUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("Url")));
                startActivity(browserIntent);
            }
        });



    }
}