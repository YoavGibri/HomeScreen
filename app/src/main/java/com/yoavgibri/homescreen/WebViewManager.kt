package com.yoavgibri.homescreen

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.util.Log
import android.webkit.*



@SuppressLint("SetJavaScriptEnabled")
class WebViewManager(private val webView: WebView, private val handler: Handler) {
    companion object {
        val KEY_TRAVEL_DURATION: String? = "travel_duration"
    }
    private val tag = "WebViewManager"

    init {
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavaScriptInterface(handler), "HtmlViewer")

        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.2526.73 Safari/537.36"

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Log.d(tag, description)
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView, req: WebResourceRequest, rerr: WebResourceError) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.errorCode, rerr.description.toString(), req.url.toString())
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d(tag, "onPageStarted")

            }

            override fun onPageFinished(view: WebView?, url: String?) {
//                view?.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.querySelector(\".section-directions-trip-numbers\").firstElementChild.firstElementChild.innerText+'</html>');")
                view?.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.querySelector(\".delay-light\").firstChild.textContent+'</html>');")
                Log.d(tag, "onPageFinished")

            }
        }

    }

    fun loadTravelDuration() {
//        val fromLat = 32.4314925
//        val fromLong = 34.8831702
//        val toLat = 32.5451804
//        val toLong = 34.8498463
//        webView.loadUrl("https://www.google.com/maps/dir/$fromLat,$fromLong/$toLat,$toLong/@$toLat,$toLong," +
//                "11z/data=!3m1!4b1!4m10!4m9!1m1!4e1!1m5!1m1!1s0x151daec5ce61bc75:0x633084c52a81b8b7!2m2!1d35.098613!2d32.658776!3e0")
        webView.loadUrl("https://goo.gl/maps/yM2eEjpeXoE2")
        Log.d(tag, "loadTravelDuration")
    }




    val loaderHandler = Handler()
    private val travelDurationLoader = object : Runnable {
        override fun run() {
            try {
                val message = Message()
                message.data.putString(KEY_TRAVEL_DURATION, "טוען...")
                handler.sendMessage(message)
                loadTravelDuration()
            } finally {
                loaderHandler.postDelayed(this, mInterval)
            }
        }
    }


    private var mInterval: Long = 0
    fun startTravelDurationLoadings(interval: Long) {
        mInterval = interval
        travelDurationLoader.run()
    }

    fun endTravelDurationLoadings(interval: Long) {
        loaderHandler.removeCallbacks(travelDurationLoader)
    }




    inner class MyJavaScriptInterface(private val handler: Handler) {

        @JavascriptInterface
        fun showHTML(html: String) {
            //code to use html content here
            handler.post {
                val message = Message()
                message.data.putString(KEY_TRAVEL_DURATION, android.text.Html.fromHtml(html).toString())
                handler.sendMessage(message)
                Log.d("html", android.text.Html.fromHtml(html).toString())
            }
        }
    }

}