package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTopByCountry extends AppCompatActivity {

    private ListView listTopByCountry;
    private NewsListAdapter infoNewsAdapter;
    private List<InfoNews> infoNews = new ArrayList<>();
    private TinyDB tinydb;
    private NewsApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_top_by_country);
        tinydb = new TinyDB(this);
        clearListView();
        client = new NewsApiClient("5c63a0bf8af94a6a8c5370304c3c408a");


        TextView placeHolderCountry = (TextView) findViewById(R.id.countryName);
        placeHolderCountry.setText(getIntent().getStringExtra("Country"));

        tinydb.putListString(String.format("Ts %s", getIntent().getStringExtra("Ctry")), (ArrayList<String>) getIntent().getBundleExtra("BUNDLE").getSerializable("Ts"));
        tinydb.putListString(String.format("Td %s", getIntent().getStringExtra("Ctry")), (ArrayList<String>) getIntent().getBundleExtra("BUNDLE").getSerializable("Td"));
        tinydb.putListString(String.format("Tu %s", getIntent().getStringExtra("Ctry")), (ArrayList<String>) getIntent().getBundleExtra("BUNDLE").getSerializable("Tu"));
        for (int i = 0; i < tinydb.getListString(String.format("Ts %s", getIntent().getStringExtra("Ctry"))).size(); i++) {
            pushInListView(tinydb.getListString(String.format("Ts %s", getIntent().getStringExtra("Ctry"))).get(i));
        }

        this.listTopByCountry = (ListView) findViewById(R.id.listTopByCountry);

        listTopByCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ReadNews.class);
                intent.putExtra("Country", getIntent().getStringExtra("Country"));
                intent.putExtra("Title", tinydb.getListString(String.format("Ts %s", getIntent().getStringExtra("Ctry"))).get(i));
                intent.putExtra("Desc", tinydb.getListString(String.format("Td %s", getIntent().getStringExtra("Ctry"))).get(i));
                intent.putExtra("Url", tinydb.getListString(String.format("Tu %s", getIntent().getStringExtra("Ctry"))).get(i));
                startActivity(intent);
            }
        });

        SearchView searchNews = (SearchView)findViewById(R.id.searchNews);
        searchNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNews.setIconified(false);
            }
        });

        searchNews.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                clearListView();
                getEverything(tinydb, s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void pushInListView(String title) {
        InfoNews news = new InfoNews();
        news.setSourceName(title);
        infoNews.add(news);
        listTopByCountry = (ListView) findViewById(R.id.listTopByCountry);
        infoNewsAdapter = new NewsListAdapter(getApplicationContext(), infoNews);
        listTopByCountry.setAdapter(infoNewsAdapter);
    }

    private void clearListView() {
        listTopByCountry = (ListView) findViewById(R.id.listTopByCountry);
        listTopByCountry.setAdapter(null);
        infoNews.clear();
    }

    private void getEverything(TinyDB tinydb, String query) {
        String language = "";
        List<String> languages = Arrays.asList("de", "es", "fr", "it", "ru", "zh");
        if (languages.contains(getIntent().getStringExtra("Ctry")))
            language = getIntent().getStringExtra("Ctry");
        else
            language = "en";
        client.getEverything(
                new EverythingRequest.Builder()
                        .q(query)
                        .language(language)
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
                        tinydb.putListString(String.format("Ts %s", getIntent().getStringExtra("Ctry")), Tt);
                        tinydb.putListString(String.format("Tt %s", getIntent().getStringExtra("Ctry")), Ts);
                        tinydb.putListString(String.format("Td %s", getIntent().getStringExtra("Ctry")), Td);
                        tinydb.putListString(String.format("Tu %s", getIntent().getStringExtra("Ctry")), Tu);
                        for (int i = 0; i < tinydb.getListString(String.format("Ts %s", getIntent().getStringExtra("Ctry"))).size(); i++) {
                            pushInListView(tinydb.getListString(String.format("Ts %s", getIntent().getStringExtra("Ctry"))).get(i));
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }
}