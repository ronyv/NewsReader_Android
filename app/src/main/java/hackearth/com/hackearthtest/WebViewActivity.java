package hackearth.com.hackearthtest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import hackearth.com.hackearthtest.utils.AppContext;

public class WebViewActivity extends AppCompatActivity {


    private String TAG = WebViewActivity.class.getName();
    private WebView wvLoadUrlData;
    private ProgressBar wvProgressBar;

    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        wvProgressBar = (ProgressBar) findViewById(R.id.wvProgressBar);
        wvProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#303F9F"), android.graphics.PorterDuff.Mode.SRC_ATOP);

        wvProgressBar.setVisibility(View.GONE);

        wvLoadUrlData =  (WebView) findViewById(R.id.wvLoadUrlData);

        if (getIntent() != null && getIntent().getExtras().getString("URL") != null ) {

            Log.d(TAG, "Insdie WebViewActivity : URL : "+ getIntent().getExtras().getString("URL"));
            Log.d(TAG, "Insdie WebViewActivity : PUBLISHER : "+ getIntent().getExtras().getString("PUBLISHER"));

            getSupportActionBar().setTitle(getIntent().getExtras().getString("PUBLISHER"));

            loadWebView(getIntent().getExtras().getString("URL"));
        }
    }

    private void loadWebView(String url) {

        if (AppContext.checkAvailability(WebViewActivity.this)) {
            wvLoadUrlData.setVisibility(View.VISIBLE);
            wvLoadUrlData.getSettings().setLoadsImagesAutomatically(true);
            wvLoadUrlData.getSettings().setJavaScriptEnabled(true);
            wvLoadUrlData.setWebViewClient(new HelloWebViewClient(wvProgressBar));
            wvLoadUrlData.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wvLoadUrlData.loadUrl(url);
        } else {
            Toast.makeText(this, AppContext.NO_INTERNET, Toast.LENGTH_SHORT).show();

        }

    }

    private class HelloWebViewClient extends WebViewClient {

        private ProgressBar progressBar;

        public HelloWebViewClient(ProgressBar wvProgressBar) {
            this.progressBar=wvProgressBar;
            progressBar.setVisibility(View.VISIBLE);

        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.e("ProcessPayment", "onReceivedError = " + errorCode);

            //404 : error code for Page Not found
            if(errorCode==404){
                // show Alert here for Page Not found
                //view.loadUrl("file:///android_asset/Page_Not_found.html");
                Toast.makeText(WebViewActivity.this, "Error in page. Please try later.", Toast.LENGTH_SHORT).show();
            }
            else{

            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);


    }


    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName("Email : "+AppContext.getPrimaryAccount(WebViewActivity.this)+" - Activity  : " + WebViewActivity.class.getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }
}
