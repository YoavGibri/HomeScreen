/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yoavgibri.homescreen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.gson.Gson
import com.yoavgibri.homescreen.WebViewManager.Companion.KEY_TRAVEL_DURATION
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

//    private lateinit var adapter: NewsFeedAdapter

    @SuppressLint("SetJavaScriptEnabled", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webViewManager = WebViewManager(webView, Handler {
            textViewTravelDuration.text = it.data.getString(KEY_TRAVEL_DURATION)
            val currentTime = SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)
            textViewUpdateTime.text = "עודכן לאחרונה ב$currentTime"
            true
        })

        webViewManager.startTravelDurationLoadings(60000)



        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewNew.layoutManager = linearLayoutManager

        doAsync {
            val feed: NewsFeed = Gson().fromJson(URL("https://newsapi.org/v2/top-headlines?country=il&apiKey=" + getString(R.string.newsApi_key)).readText(), NewsFeed::class.java)
            uiThread {
                Toast.makeText(this@MainActivity, feed.articles[0].description, Toast.LENGTH_LONG).show()
                recyclerViewNew.adapter = NewsFeedAdapter(feed.articles)
            }
        }
    }


}
