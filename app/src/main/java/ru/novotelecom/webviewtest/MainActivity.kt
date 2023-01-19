package ru.novotelecom.webviewtest

import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview)
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
        webView.loadUrl("https://3dsecmt.sberbank.ru/payment/se/keys.do")
    }
}