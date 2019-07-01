/**
 * @Class : ActivityHelpSupport
 * @Usage : This activity is used for managing the Help and Support from web URL
 * @Author : 1769
 */
package com.govida.ui_section.help_and_support_section

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.govida.R
import com.govida.ui_section.base_class_section.BaseActivity

class ActivityHelpSupport : BaseActivity() {

    private lateinit var mWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)
        setupUI()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        showLoading()
        val toolbar: Toolbar = findViewById(R.id.toolbar_settings)
        val toolbarTitle: TextView =toolbar.findViewById(R.id.title)
        toolbarTitle.text=getString(R.string.help_and_support_title)
        mWebView = findViewById(R.id.web_view)
        loadWebView()
    }

    /**
     *  @Function : loadWebView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to load content from web URL to webview
     *  @Author   : 1769
     */
    private fun loadWebView() {
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                hideLoading()
            }
        }
        mWebView.loadUrl("https://www.e-zest.com/contact-us/")
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }
}