package com.example.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsListAdapter extends BaseAdapter {

    private Context context;
    private List<InfoNews> news;

    public NewsListAdapter(Context context, List<InfoNews> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.news_row, null);
        }

        InfoNews infoNews = news.get(i);

        TextView newsSourceName = (TextView) view.findViewById(R.id.newsSourceName);

        newsSourceName.setText(infoNews.getSourceName());

        return view;
    }
}
