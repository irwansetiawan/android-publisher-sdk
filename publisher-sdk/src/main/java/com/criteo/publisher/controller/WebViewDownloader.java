package com.criteo.publisher.controller;

import com.criteo.publisher.CriteoInterstitialAdListener;
import com.criteo.publisher.tasks.WebViewDataTask;
import com.criteo.publisher.model.WebViewData;

public class WebViewDownloader {

    private WebViewData webViewData;

    public WebViewDownloader(WebViewData webviewData) {
        this.webViewData = webviewData;
    }

    public boolean isLoading() {
        return webViewData.isLoading();
    }

    public void fillWebViewHtmlContent(String displayUrl, CriteoInterstitialAdListener listener,
            String webViewUserAgent) {
        new WebViewDataTask(webViewData, listener).execute(displayUrl, webViewUserAgent);
    }

    public WebViewData getWebViewData() {
        return webViewData;
    }

    public void refresh() {
        webViewData.refresh();
    }

    public void downloadFailed() {
        webViewData.downloadFailed();
    }

    public void loading() {
        webViewData.downloadloading();
    }
}
