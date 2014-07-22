package com.boutline.sports.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boutline.sports.R;
import com.boutline.sports.application.Constants;

/**
 * Created by user on 21/07/14.
 */
public class TwitterWebViewLogin extends Activity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_webview);
        String url = (String) getIntent().getExtras().get("URL");
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient( new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if( url.contains(Constants.TWITTER_CALLBACK_URL))
                {
                    Uri uri = Uri.parse(url);
                    String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra( "oauth_verifier", oauthVerifier );
                    setResult( RESULT_OK, resultIntent );
                    finish();
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);

                    return true;
                }
                return false;
            }


        });
        webView.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent resultIntent = new Intent();

        setResult( 0, resultIntent );
        finish();
        overridePendingTransition(R.anim.pushleftout, R.anim.pushleftin);

    }

}
