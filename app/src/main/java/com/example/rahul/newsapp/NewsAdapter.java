package com.example.rahul.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by rahul on 2017-09-30.
 */
// to bind the data we created a adapter class
//we gonna use viewholder inside of Newsadapter thats why we will use<NewsAdapter.ViewHolder>
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    //define NewsData List
    private List<NewsData> news;
    private Context context;
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

        final NewsData newz = news.get(position);
        holder.sectionView.setText(newz.getSectionBelong());
        holder.articleView.setText(newz.getArticleName());
        holder.dateView.setText(newz.getDate());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked something" + newz.getSectionBelong(), Toast.LENGTH_SHORT).show();
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri webUri = Uri.parse(newz.getWebURL());
                // Create a new intent to view the newsdata URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, webUri);
                view.getContext().startActivity(websiteIntent);
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
        public TextView sectionView;
        public TextView articleView;
        public LinearLayout linearLayout;
        public TextView dateView;
        //with this constructor we get above XML views
        public ViewHolder(View itemView) {
            super(itemView);
            sectionView = (TextView) itemView.findViewById(R.id.titleSection);
            articleView = (TextView) itemView.findViewById(R.id.titleArticle);
            dateView = (TextView) itemView.findViewById(R.id.date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

        }
    }
}
