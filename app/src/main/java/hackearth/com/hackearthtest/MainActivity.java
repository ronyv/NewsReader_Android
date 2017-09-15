package hackearth.com.hackearthtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import hackearth.com.hackearthtest.adapter.RecyclerAdapter_All_News;
import hackearth.com.hackearthtest.adapter.RecyclerAdapter_Favorite_News;
import hackearth.com.hackearthtest.dialogue.AppDialogues;
import hackearth.com.hackearthtest.model.NewsModel;
import hackearth.com.hackearthtest.provider.NewsProvider;
import hackearth.com.hackearthtest.utils.AppContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getName();
    private LinearLayout llLatestUpdatesLayout, llFavouritesLayout, llMyProfileLayout, llContentError;
    private static RecyclerView rvAllNews;
    private RecyclerView rvFavoriteNews;
    private ProgressBar pbLoadNews;
    private TextView tvErrorMessage;
    private static LinearLayoutManager mLayoutManager;
    private static RecyclerAdapter_All_News allNewsAdapter;
    private RecyclerAdapter_Favorite_News favoriteNewsAdapter;

    private Button btnPublisher, btnCategory, btnSortByTime;

    private CircleImageView civMyPhoto;

    private static Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.registerReceiver(receiverUpdate, new IntentFilter(AppContext.ALL_NEWS_BROADCAST));


        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        /**
         *  Implementation of the Drawer layout
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /** intitialize the layouts **/
        initayouts();

        /**
         * Implementation of the Bottom bar
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                setupFavoritesLayout();
                                break;
                            case R.id.action_updates:
                                setupUpdatesLayout();
                                break;
                            case R.id.action_profile:
                                setupPortforlioLayout();
                                break;
                        }
                        return false;
                    }
                });


    }



    /* Initialize the views
         * Set the Updates layout as selected by default.
         * Hide other sections from the UI.
         */
    private void initayouts() {

        Log.d(TAG, "Inside initBottomBarLayouts : ");

        llLatestUpdatesLayout = (LinearLayout) findViewById(R.id.llLatestUpdatesLayout);
        llFavouritesLayout = (LinearLayout) findViewById(R.id.llFavouritesLayout);
        llMyProfileLayout = (LinearLayout) findViewById(R.id.llMyProfileLayout);
        llContentError = (LinearLayout) findViewById(R.id.llContentError);

        rvAllNews = (RecyclerView) findViewById(R.id.rvAllNews);
        rvFavoriteNews = (RecyclerView) findViewById(R.id.rvFavoriteNews);

        pbLoadNews = (ProgressBar) findViewById(R.id.pbLoadNews);
        pbLoadNews.getIndeterminateDrawable().setColorFilter(Color.parseColor("#303F9F"), android.graphics.PorterDuff.Mode.SRC_ATOP);

        civMyPhoto = (CircleImageView) findViewById(R.id.civMyPhoto);

        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);


        btnPublisher = (Button) findViewById(R.id.btnPublisher);
        btnCategory = (Button) findViewById(R.id.btnCategory);
        btnSortByTime = (Button) findViewById(R.id.btnSortByTime);


        llLatestUpdatesLayout.setVisibility(View.VISIBLE);
        llFavouritesLayout.setVisibility(View.GONE);
        llMyProfileLayout.setVisibility(View.GONE);
        llContentError.setVisibility(View.GONE);

        rvAllNews.setVisibility(View.GONE);
        rvFavoriteNews.setVisibility(View.GONE);

        pbLoadNews.setVisibility(View.GONE);

        setupUpdatesLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();



        mTracker.setScreenName("Email : "+AppContext.getPrimaryAccount(MainActivity.this)+" - Activity  : " + WebViewActivity.class.getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        civMyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Rony Varghese P", Toast.LENGTH_SHORT).show();

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("MY_PIC_CLICK")
                        .setAction("Click Event For Rony's Pic : Email :"+AppContext.getPrimaryAccount(MainActivity.this))
                        .build());
            }
        });


        try {

            btnPublisher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(AppContext.publisherList.size() > 0){
                        AppDialogues.publisherSelect(MainActivity.this,"SELECT A PUBLISHER");
                    }else{
                        Toast.makeText(MainActivity.this, "No Publishers found !", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }


        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppContext.categoryList.size() > 0){
                    AppDialogues.categorySelect(MainActivity.this,"CHOOSE A CATEGORY");
                }else{
                    Toast.makeText(MainActivity.this, "No Categories found !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSortByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppContext.allNewsList.size() > 0){
                    AppDialogues.sortSelect(MainActivity.this,"SORT");
                }else{
                    Toast.makeText(MainActivity.this, "Please try later !", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /* Fetch and display all the news from the api
         * Display items in list view
         */
    private void setupUpdatesLayout() {


        Log.d(TAG, "Inside setupUpdatesLayout : ");


        llLatestUpdatesLayout.setVisibility(View.VISIBLE);
        llFavouritesLayout.setVisibility(View.GONE);
        llMyProfileLayout.setVisibility(View.GONE);
        llContentError.setVisibility(View.GONE);


        if(AppContext.checkAvailability(MainActivity.this) && AppContext.allNewsList == null ){

            Log.d(TAG, "Inside setupUpdatesLayout : Get data from server");


            pbLoadNews.setVisibility(View.VISIBLE);

            NewsProvider newsProvider = new NewsProvider(MainActivity.this);
            newsProvider.getAllNews();

        }
        else if(AppContext.allNewsList != null && AppContext.allNewsList.size() > 0)
        {
            Log.d(TAG, "Inside setupUpdatesLayout : Load data in the cache");
            //set the adapter
            SetAllNewsAdapter();

        }else{
            Log.d(TAG, "Inside setupUpdatesLayout : Offline");
            llContentError.setVisibility(View.VISIBLE);

            llFavouritesLayout.setVisibility(View.GONE);
            llMyProfileLayout.setVisibility(View.GONE);
            llLatestUpdatesLayout.setVisibility(View.GONE);

            tvErrorMessage.setText(AppContext.NO_INTERNET);

            Snackbar.make(llLatestUpdatesLayout, AppContext.NO_INTERNET, 3000);
        }
    }


    /*
     * List the favorite items marked by the user in a list view
     * Give option to remove item from favorites
     */
    private void setupFavoritesLayout() {
        Log.d(TAG, "Inside setupFavoritesLayout : ");

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Favorite_TAB")
                .setAction("Click Event For Favorites tab : Email :"+AppContext.getPrimaryAccount(MainActivity.this))
                .build());

        llFavouritesLayout.setVisibility(View.VISIBLE);
        llLatestUpdatesLayout.setVisibility(View.GONE);
        llMyProfileLayout.setVisibility(View.GONE);

        if(AppContext.favoriteNewsMap != null && AppContext.favoriteNewsMap.size() > 0){

            //make the araylist of favorate items from the hashmap
            ArrayList<NewsModel> favoriteNewsList = AppContext.ConvertMapToList(AppContext.favoriteNewsMap);


            //set the adapter
            SetFavouriteNewsAdapter(favoriteNewsList);



        }else{
            //set no favorites message

            Log.d(TAG, "Inside setupUpdatesLayout : Offline");
            llContentError.setVisibility(View.VISIBLE);

            llFavouritesLayout.setVisibility(View.GONE);
            llMyProfileLayout.setVisibility(View.GONE);
            llLatestUpdatesLayout.setVisibility(View.GONE);

            tvErrorMessage.setText(AppContext.NO_FAVORITE_NEWS);
        }
    }

    /*
     * Custom layout to put my work experience and other qualifications.
     */
    private void setupPortforlioLayout() {

        Log.d(TAG, "Inside setupPortforlioLayout : ");

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("PORTFOLIO_TAB")
                .setAction("Click Event For Portfolio tab : Email :"+AppContext.getPrimaryAccount(MainActivity.this))
                .build());

        llMyProfileLayout.setVisibility(View.VISIBLE);
        llFavouritesLayout.setVisibility(View.GONE);
        llLatestUpdatesLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_offline_mode) {

            Toast.makeText(this, "Offline Mode Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
     * Sets the all news adapter to Updates Layout
     */
    private void SetAllNewsAdapter() {

        rvAllNews.setVisibility(View.VISIBLE);
        allNewsAdapter = new RecyclerAdapter_All_News(MainActivity.this,rvAllNews, AppContext.allNewsList,mTracker);
        rvAllNews.setAdapter(allNewsAdapter);
        rvAllNews.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAllNews.setLayoutManager(mLayoutManager);
    }

    /*
     * Sets the Favorite news adapter to Favourite Layout
     */
    private void SetFavouriteNewsAdapter(ArrayList<NewsModel> favoriteNewsList) {

        rvFavoriteNews.setVisibility(View.VISIBLE);
        favoriteNewsAdapter = new RecyclerAdapter_Favorite_News(MainActivity.this,rvFavoriteNews,favoriteNewsList);
        rvFavoriteNews.setAdapter(favoriteNewsAdapter);
        rvFavoriteNews.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvFavoriteNews.setLayoutManager(mLayoutManager);
    }


    private BroadcastReceiver receiverUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                pbLoadNews.setVisibility(View.GONE);

                //Set the adapter if the ArrayList<NewsModel> is Valid

                if(AppContext.allNewsList != null && AppContext.allNewsList.size() > 0){

                    //set the recycle list adapter
                    SetAllNewsAdapter();

                }else{

                    //set the error page with proper response

                    llContentError.setVisibility(View.VISIBLE);

                    llFavouritesLayout.setVisibility(View.GONE);
                    llMyProfileLayout.setVisibility(View.GONE);
                    llLatestUpdatesLayout.setVisibility(View.GONE);

                    tvErrorMessage.setText(AppContext.NO_NEWS);
                }




            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "Inside BroadcastReceiver : Caught Exception");

                llContentError.setVisibility(View.VISIBLE);

                llFavouritesLayout.setVisibility(View.GONE);
                llMyProfileLayout.setVisibility(View.GONE);
                llLatestUpdatesLayout.setVisibility(View.GONE);

                tvErrorMessage.setText(AppContext.CUSTOM_ERROR_MESSAGE);
            }
        }
    };



    @Override
    public void onDestroy() {
        try {
            this.unregisterReceiver(receiverUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    public static void SortRecyclerView(Context context, boolean isAsc) {

        Log.d(TAG, "Inside SortRecyclerView : ");
        // Sort by created at timestamp
        // For regerence : http://stackoverflow.com/questions/1814095/sorting-an-arraylist-of-contacts-based-on-name

        if(isAsc)
        {
            Toast.makeText(context, "Older News first", Toast.LENGTH_SHORT).show();
            Collections.sort(AppContext.allNewsList, NewsModel.COMPARE_BY_TIME_ASC);
        }else{
            Toast.makeText(context, "Latest News first", Toast.LENGTH_SHORT).show();
            Collections.sort(AppContext.allNewsList, NewsModel.COMPARE_BY_TIME_DESC);
        }

        allNewsAdapter.notifyDataSetChanged();



    }

    public static void updateListBasedOnCategory(Context context, String selection) {

        Log.d(TAG, "Inside updateListBasedOnCategory : ");

        allNewsAdapter = new RecyclerAdapter_All_News(context,rvAllNews, AppContext.categoryMap.get(selection),mTracker);
        rvAllNews.setAdapter(allNewsAdapter);
        rvAllNews.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(context);
        rvAllNews.setLayoutManager(mLayoutManager);
    }



}
