package ru.novotelecom.webviewtest

import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var textView: TextView
    lateinit var urlTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        urlTextView = findViewById(R.id.urlTextView)
        textView = findViewById(R.id.textView)

        val url = "https://3dsecmt.sberbank.ru/payment/se/keys.do"
//        val url = "https://example.com"

        urlTextView.text = "Url: $url"

        loadInWebView(url)
        makeGetQuery(url)
    }

    private fun loadInWebView(url: String) {
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                when (error!!.primaryError) {
                    SslError.SSL_UNTRUSTED -> Log.d("WEB_VIEW_EXAMPLE", "The certificate authority is not trusted.") // (1)
                    SslError.SSL_EXPIRED -> Log.d("WEB_VIEW_EXAMPLE", "The certificate has expired.")
                    SslError.SSL_IDMISMATCH -> Log.d("WEB_VIEW_EXAMPLE", "The certificate Hostname mismatch.")
                    SslError.SSL_NOTYETVALID -> Log.d("WEB_VIEW_EXAMPLE", "The certificate is not yet valid.")
                }
                super.onReceivedSslError(view, handler, error)
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }


    private fun makeGetQuery(url: String) {
        Thread {
            try {
                val client = OkHttpClient();
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response = client.newCall(request).execute()
                response.body?.let { b ->
                    textView.text = b.string()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                textView.text = e.message
            }
        }.start()
    }
}