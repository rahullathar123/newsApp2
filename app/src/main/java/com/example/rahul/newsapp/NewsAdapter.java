package com.example.rahul.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xdroid.toaster.Toaster;

import static com.example.rahul.newsapp.R.id.date;


/**
 * Created by rahul on 2017-09-30.
 */
// to bind the data we created a adapter class
//we gonna use viewholder inside of Newsadapter thats why we will use<NewsAdapter.ViewHolder>
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private static final String LOG_TAG = NewsAdapter.class.getName();

    Date date= null;

    //define NewsData List
    private List<NewsData> news = null;
    NewsData newz;
    private Context context;
    private SwipeRefreshLayout mSwipe;
    //constructor to initialize above objects
    public NewsAdapter(List<NewsData> news, Context context) {
        this.news = news;
        this.context = context;
    }
    //this method is called whenever our viewHolder extends Recyclerview.ViewHolder is created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);
        return new ViewHolder(v);
    }

    //this method binds the data to viewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date date= null;
        try {
            date = simpleDateFormat.parse(newz.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDateFormat= new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
        String finalDate = newDateFormat.format(date);


        newz = news.get(position);
        holder.sectionView.setText(newz.getSectionBelong());
        holder.articleView.setText(newz.getArticleName());
        holder.dateView.setText(finalDate);
        Log.wtf(LOG_TAG,"WTF");
        holder.authorView.setText(newz.getAuthor());



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toaster.toast(R.string.web_message);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri webUri = Uri.parse(newz.getWebURL());
                // Create a new intent to view the newsdata URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, webUri);
                view.getContext().startActivity(websiteIntent);

                if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(websiteIntent);
                } else {
                    Toast.makeText(context,
                            "Cannot handle this action",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if (news != null) {
            return news.size();
        } else return 0;
    }

    public void addData(List<NewsData> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //define textviews
        @BindView(R.id.titleSection) TextView sectionView;
        @BindView(R.id.titleArticle) TextView articleView;
        @BindView(R.id.date) TextView dateView;
        @BindView(R.id.authorSection) TextView authorView;
        @BindView(R.id.linearLayout) LinearLayout linearLayout;

        //with this constructor we get above XML views
        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public void setNews(List<NewsData> news){
        news =  new ArrayList<>();
        news.addAll(news);
        notifyDataSetChanged();
    }
}
