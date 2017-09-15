package hackearth.com.hackearthtest.provider;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import hackearth.com.hackearthtest.model.NewsModel;
import hackearth.com.hackearthtest.utils.AppContext;
import hackearth.com.hackearthtest.utils.HTTPUtils;

/**
 * Created by apple on 14/09/17.
 */

public class NewsProvider {

    private Context context;
    private String TAG = NewsProvider.class.getName();


    public NewsProvider(Context context) {
        this.context = context;
    }


    public void getAllNews() {

        try {

            Log.d(TAG, "Inside  getAllNews() : ");

            TestAsyncTask testAsyncTask = new TestAsyncTask(context, AppContext.HACKEARTH_API_URL);
            testAsyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class TestAsyncTask extends AsyncTask<Void, Void, String> {
        private Context mContext;
        private String mUrl;

        public TestAsyncTask(Context context, String url) {
            mContext = context;
            mUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "Inside  onPreExecute() : ");
        }

        @Override
        protected String doInBackground(Void... params) {
            String resultString = null;

            resultString = HTTPUtils.getJSON(mUrl);

            Log.d(TAG, "Inside  doInBackground() : ");

            return resultString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Log.d(TAG, "Inside  onPostExecute() : " + result);

            processResponse(result);
        }


    }

    private void processResponse(String result) {

        try {

            if(result == null)
            {
                //send broadcast to MainActivity
                Log.d(TAG, "Inside  processResponse() : Result is NULL");

            }else{
                Log.d(TAG, "Inside  processResponse() : Result is VALID");
                //parse the json and set the ArrayList<NewsModel>

                AppContext.allNewsList = new ArrayList<NewsModel>();
                NewsModel eachNewsData = null;

                ArrayList<NewsModel> mapCategoryList = null;

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);


                    eachNewsData = new NewsModel();
                    eachNewsData.setID(jsonobject.getInt("ID"));
                    eachNewsData.setTITLE(jsonobject.getString("TITLE"));
                    eachNewsData.setCATEGORY(jsonobject.getString("CATEGORY"));
                    eachNewsData.setHOSTNAME(jsonobject.getString("HOSTNAME"));
                    eachNewsData.setPUBLISHER(jsonobject.getString("PUBLISHER"));
                    eachNewsData.setTIMESTAMP(jsonobject.getLong("TIMESTAMP"));
                    eachNewsData.setURL(jsonobject.getString("URL"));


                    eachNewsData.setTimestampDate(new Date(jsonobject.getLong("TIMESTAMP")));

                    AppContext.allNewsList.add(eachNewsData);

                    //set the publisher list for the app
                        if(AppContext.publisherList == null){

                            //add entry to publisher arraylist
                                AppContext.publisherList = new ArrayList<>();
                                AppContext.publisherList.add(jsonobject.getString("PUBLISHER"));


                        }else{

                            if(AppContext.publisherList.contains(jsonobject.getString("PUBLISHER"))){
                                //ignore
                            }else{
                                AppContext.publisherList.add(jsonobject.getString("PUBLISHER"));
                            }
                        }

                    //set the category list

                        if(AppContext.categoryList == null){

                            AppContext.categoryList = new ArrayList<>();
                            AppContext.categoryList.add(jsonobject.getString("CATEGORY"));

                            AppContext.categoryMap = new HashMap<>();
                            mapCategoryList = new ArrayList<>();
                            mapCategoryList.add(eachNewsData);
                            AppContext.categoryMap.put(jsonobject.getString("CATEGORY"), mapCategoryList);

                        }else{

                            if(AppContext.categoryList.contains(jsonobject.getString("CATEGORY"))){
                                //ignore

                                mapCategoryList = AppContext.categoryMap.get(jsonobject.getString("CATEGORY"));
                                mapCategoryList.add(eachNewsData);
                                AppContext.categoryMap.put(jsonobject.getString("CATEGORY"), mapCategoryList);


                            }else{
                                AppContext.categoryList.add(jsonobject.getString("CATEGORY"));

                                mapCategoryList = new ArrayList<>();
                                mapCategoryList.add(eachNewsData);
                                AppContext.categoryMap.put(jsonobject.getString("CATEGORY"), mapCategoryList);
                            }
                        }
                }


                //send broadcast to MainActivity
                //Log.d(TAG, "Inside  processResponse() : List : "+ new Gson().toJson(AppContext.allNewsList));

            }

        }catch (Exception e){
            Log.d(TAG, "Inside  processResponse() : Caught Exception");
            e.printStackTrace();

        }

        Intent intent = new Intent(AppContext.ALL_NEWS_BROADCAST);
        context.sendBroadcast(intent);
    }
}
