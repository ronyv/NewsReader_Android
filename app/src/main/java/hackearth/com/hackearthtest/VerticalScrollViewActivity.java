package hackearth.com.hackearthtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import hackearth.com.hackearthtest.adapter.ScreenSlidePagerAdapter;
import hackearth.com.hackearthtest.utils.AppContext;
import hackearth.com.hackearthtest.views.VerticalViewPager;

public class VerticalScrollViewActivity extends AppCompatActivity {

    private String TAG = VerticalScrollViewActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_scroll_view);

        if(getIntent() != null && getIntent().getExtras() != null ){

            if(getIntent().getExtras().getString(AppContext.KEY_NEWS_LIST) != null &&
                    getIntent().getExtras().getString(AppContext.KEY_NEWS_LIST).equals(AppContext.VALUE_LIST_ALL) ){

                Log.d(TAG, "All news list selected");
                Log.d(TAG, "Position : "+ getIntent().getIntExtra(AppContext.KEY_LIST_POSITION,0));

                ScreenSlidePagerAdapter screenSlidePagerAdapter =
                        new ScreenSlidePagerAdapter(getSupportFragmentManager(),AppContext.VALUE_LIST_ALL);


                VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.vvpNewsPager);
                verticalViewPager.setAdapter(screenSlidePagerAdapter);
                verticalViewPager.setCurrentItem(getIntent().getIntExtra(AppContext.KEY_LIST_POSITION,0));





            }else if(getIntent().getExtras().getString(AppContext.KEY_NEWS_LIST) != null &&
                    getIntent().getExtras().getString(AppContext.KEY_NEWS_LIST).equals(AppContext.VALUE_LIST_FAVOURITES)){

                Log.d(TAG, "Inside VerticalScrollViewActivity : Favourites news list selected");

                ScreenSlidePagerAdapter screenSlidePagerAdapter =
                        new ScreenSlidePagerAdapter(getSupportFragmentManager(), AppContext.VALUE_LIST_FAVOURITES);

                VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.vvpNewsPager);
                verticalViewPager.setAdapter(screenSlidePagerAdapter);
                verticalViewPager.setCurrentItem(getIntent().getIntExtra(AppContext.KEY_LIST_POSITION,0));


            }

            Toast.makeText(this, "Swipe down for next page", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);


    }
}
