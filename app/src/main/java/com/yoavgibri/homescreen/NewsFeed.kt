package com.yoavgibri.homescreen

 class NewsFeed {
    val status: String = ""
    val totalResults: Int = 0
    val articles: ArrayList<Article> = ArrayList()

    class Article {
        val source: Source = Source()
        val author: String = ""
        val title: String = ""
        val description: String = ""
        val url: String = ""
        val urlToImage: String = ""
        val publishedAt: String = ""
        val content: String = ""

        class Source {
            val id: String = ""
            val name: String = ""
        }
    }
}