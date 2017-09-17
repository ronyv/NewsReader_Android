package hackearth.com.hackearthtest.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Patterns;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.regex.Pattern;

import hackearth.com.hackearthtest.model.NewsModel;

/**
 * Created by apple on 14/09/17.
 */

public class AppContext {

    public static final String HACKEARTH_API_URL = "https://api.myjson.com/bins/12sapp";


    private static String TAG = AppContext.class.getName();
    public static ArrayList<NewsModel> allNewsList;
    public static LinkedHashMap<Integer, NewsModel> favoriteNewsMap;

    public static ArrayList<String> publisherList = null;

    public static ArrayList<String> categoryList = null;

    public static ArrayList<NewsModel> favoriteNewsList = null;


    public static HashMap<String, ArrayList<NewsModel>> publisherMap = null;
    public static HashMap<String, ArrayList<NewsModel>> categoryMap = null;


    public static String NO_INTERNET = "No Internet";
    public static String ALL_NEWS_BROADCAST = "all_news_broadcast";
    public static String NEWS_FRAGMENT_BROADCAST = "news_fragment_broadcast";
    public static String NO_NEWS = "No News found !!";
    public static String NO_FAVORITE_NEWS = "No Favorite News found !!";
    public static String CUSTOM_ERROR_MESSAGE = "Something went wrong. Please try later !!";

    public static String KEY_LIST_POSITION = "POSITION";
    public static String KEY_NEWS_LIST = "NEWS_LIST";
    public static String VALUE_LIST_ALL = "ALL";
    public static String VALUE_LIST_FAVOURITES = "FAVOURITE";
    public static int VALUE_DEFAULT_POSITION = 0;



    public static boolean checkAvailability(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            } else {
                String reason = "";
                if (netInfo != null) {
                    reason = netInfo.getReason();
                }
                //Log.i(TAG, "Network not availale : " + reason);
            }
            NetworkInfo i = cm.getActiveNetworkInfo();
            if (i == null)
                return false;
            if (!i.isConnected())
                return false;
            if (!i.isAvailable())
                return false;
        } catch (Exception e) {
            //LogTrace.i(TAG, "Error checking network information");
            e.printStackTrace();
        }
        return false;
    }


    public static String convertTimestampToDateFormat(long unixSeconds) {
        Date date = new Date(unixSeconds);
        Format format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return format.format(date);

    }

    public static void removeFromFavoriteList(int id) {

        if (favoriteNewsMap != null && favoriteNewsMap.get(id) != null) {
            favoriteNewsMap.remove(id);
            Log.d(TAG, "Removed Successfully : " + id);
        }

        if (favoriteNewsMap != null && favoriteNewsMap.size() == 0) {
            favoriteNewsMap = null;
        }
    }

    public static void addToFavoriteList(int id, NewsModel dm) {

        if (favoriteNewsMap == null) {
            favoriteNewsMap = new LinkedHashMap<>();
        }

        favoriteNewsMap.put(id, dm);
        Log.d(TAG, "Added Successfully : " + id);
    }

    public static ArrayList<NewsModel> ConvertMapToList(LinkedHashMap<Integer, NewsModel> favoriteNewsMap) {

        ArrayList<NewsModel> favNewsList = null;
        NewsModel eachDataModel = null;

        try {

            favNewsList = new ArrayList<>();

            Iterator it = favoriteNewsMap.entrySet().iterator();
            while (it.hasNext()) {
                LinkedHashMap.Entry pair = (LinkedHashMap.Entry) it.next();
                //System.out.println(pair.getKey() + " = " + pair.getValue());

                eachDataModel = (NewsModel) pair.getValue();
                favNewsList.add(eachDataModel);


            }

        } catch (Exception e) {
            e.printStackTrace();
            favNewsList = null;
        }

        return favNewsList;

    }

    public static String getPrimaryAccount(Context context) {

        String possibleEmail = null;
        try {

            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }

            Account[] accounts = AccountManager.get(context).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    possibleEmail = account.name;
                }else{
                    possibleEmail = null;
                }
            }

        }catch (Exception e){
            possibleEmail = null;
            e.printStackTrace();
        }

        return possibleEmail;
    }


    public static String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) return s;
        }
        return null;
    }
}
