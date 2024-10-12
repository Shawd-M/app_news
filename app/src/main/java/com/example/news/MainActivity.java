package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listNews;
    private NewsListAdapter infoNewsAdapter;
    private List<InfoNews> infoNews = new ArrayList<>();
    private NewsApiClient client;
    private TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinydb = new TinyDB(this);
        tinydb.clearAll();
        client = new NewsApiClient("5c63a0bf8af94a6a8c5370304c3c408a");
        reloadInfos();

        this.listNews = (ListView) findViewById(R.id.listNews);

        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ListTopByCountry.class);
                intent.putExtra("Country", getEmoji(infoNews.get(i).getRelatedCountry()));
                intent.putExtra("Ctry", infoNews.get(i).getRelatedCountry());
                Bundle args = new Bundle();
                args.putSerializable("Ts", (Serializable) tinydb.getListString(String.format("Ts %s", infoNews.get(i).getRelatedCountry())));
                args.putSerializable("Td", (Serializable) tinydb.getListString(String.format("Td %s", infoNews.get(i).getRelatedCountry())));
                args.putSerializable("Tu", (Serializable) tinydb.getListString(String.format("Tu %s", infoNews.get(i).getRelatedCountry())));
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });
    }

    private void getTopHeadlines(TinyDB tinyDB, String country) {
        client.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .country(country)
                        .pageSize(5)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        ArrayList<String> Ts = new ArrayList<String>();
                        ArrayList<String> Tt = new ArrayList<String>();
                        ArrayList<String> Td = new ArrayList<String>();
                        ArrayList<String> Tu = new ArrayList<String>();
                        for (int i = 0; i < response.getArticles().size(); i++) {
                            Ts.add(response.getArticles().get(i).getSource().getName());
                            Tt.add(response.getArticles().get(i).getTitle());
                            Td.add(response.getArticles().get(i).getDescription());
                            Tu.add(response.getArticles().get(i).getUrl());
                        }
                        tinyDB.putListString(String.format("Ts %s", country), Tt);
                        tinyDB.putListString(String.format("Tt %s", country), Ts);
                        tinyDB.putListString(String.format("Td %s", country), Td);
                        tinyDB.putListString(String.format("Tu %s", country), Tu);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                    }
                }
        );
    }

    private void reloadInfos() {
        String[] countries = {"fr", "us", "gb", "de", "it", "es", "cn", "jp", "kr", "ru"};

        for (int i = 0; i < countries.length; i++) {
            getTopHeadlines(tinydb, countries[i]);
            pushInListView(countries[i]);
        }
    }

    private void pushInListView(String country) {
        InfoNews news = new InfoNews();
        String category = getEmoji(country);
        news.setSourceName(category);
        news.setRelatedCountry(country);
        infoNews.add(news);
        listNews = (ListView) findViewById(R.id.listNews);
        infoNewsAdapter = new NewsListAdapter(getApplicationContext(), infoNews);
        listNews.setAdapter(infoNewsAdapter);
    }

    private String getEmoji(String country) {
        String category = "";
        int[] codepoints = new int[2];
        String emoji = new String(codepoints, 0, codepoints.length);
        switch(country) {
            case "fr":
                codepoints[0] = 0x1F1EB;
                codepoints[1] = 0x1F1F7;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " FRANCE";
                break;
            case "us":
                codepoints[0] = 0x1F1FA;
                codepoints[1] = 0x1F1F8;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " UNITED STATES";
                break;
            case "gb":
                codepoints[0] = 0x1F1EC;
                codepoints[1] = 0x1F1E7;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " GREAT BRITAIN";
                break;
            case "de":
                codepoints[0] = 0x1F1E9;
                codepoints[1] = 0x1F1EA;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " DEUTSCHLAND";
                break;
            case "cn":
                codepoints[0] = 0x1F1E8;
                codepoints[1] = 0x1F1F3;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " 中華";
                break;
            case "jp":
                codepoints[0] = 0x1F1EF;
                codepoints[1] = 0x1F1F5;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " にほん";
                break;
            case "kr":
                codepoints[0] = 0x1F1F0;
                codepoints[1] = 0x1F1F7;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " 한국";
                break;
            case "es":
                codepoints[0] = 0x1F1EA;
                codepoints[1] = 0x1F1F8;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " ESPAÑA";
                break;
            case "it":
                codepoints[0] = 0x1F1EE;
                codepoints[1] = 0x1F1F9;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " ITALIA";
                break;
            case "ru":
                codepoints[0] = 0x1F1F7;
                codepoints[1] = 0x1F1FA;
                emoji = new String(codepoints, 0, codepoints.length);
                category = emoji + " Россия";
                break;
        }
        return category;
    }
}