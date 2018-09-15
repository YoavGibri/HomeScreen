package com.yoavgibri.homescreen

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_item_row.view.*

class NewsFeedAdapter(private var articles: ArrayList<NewsFeed.Article>) : RecyclerView.Adapter<NewsFeedAdapter.NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): NewsHolder {
        val inflatedView = parent.inflate(R.layout.article_item_row, false)
        return NewsHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        var article = articles[position]
        holder.bindArticle(article)
    }


    class NewsHolder(private var v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var article: NewsFeed.Article? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            Log.d("RecyclerView", "CLICK!")
        }

        fun bindArticle(article: NewsFeed.Article) {
            this.article = article
            Picasso.get().load(article.urlToImage).into(v.imageViewPhoto)
            v.textViewTitle.text = article.title
            v.textViewDescription.text = article.description
            v.textViewDate.text = article.publishedAt
        }
    }

}