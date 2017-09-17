package hackearth.com.hackearthtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import hackearth.com.hackearthtest.R;
import hackearth.com.hackearthtest.VerticalScrollViewActivity;
import hackearth.com.hackearthtest.WebViewActivity;
import hackearth.com.hackearthtest.model.NewsModel;
import hackearth.com.hackearthtest.utils.AppContext;
import hackearth.com.hackearthtest.viewholder.RecyclerViewHolder_AllNews;

/**
 * Created by apple on 14/09/17.
 */

public class RecyclerAdapter_All_News extends RecyclerView.Adapter<RecyclerViewHolder_AllNews>{

    RecyclerView rvAllNews;
    Context context;
    LayoutInflater inflater;
    int lastPosition = -1;
    ArrayList<NewsModel> allNewsList;
    Tracker mTracker;


    public RecyclerAdapter_All_News(Context context, RecyclerView rvAllNews, ArrayList<NewsModel> allNewsList,
                                    Tracker mTracker) {

        this.context = context;
        this.rvAllNews = rvAllNews;
        inflater = LayoutInflater.from(context);
        this.allNewsList = allNewsList;
        this.mTracker = mTracker;
    }


    @Override
    public RecyclerViewHolder_AllNews onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.each_news_list_item, parent, false);

        RecyclerViewHolder_AllNews viewHolder = new RecyclerViewHolder_AllNews(v);


        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder_AllNews holder, final int position) {

        final NewsModel dm = allNewsList.get(position);


        holder.mfTitleString.setText(dm.getTITLE()+"");
        holder.nfCategory.setText("Category : "+dm.getCATEGORY());
        holder.nfHostName.setText(dm.getHOSTNAME()+"");
        holder.nfPublisher.setText(dm.getPUBLISHER()+"");

        try {
            String time = AppContext.convertTimestampToDateFormat(dm.getTIMESTAMP());

            holder.nfTimestamp.setText(String.valueOf(time));


        }catch (Exception e){
            e.printStackTrace();
        }

        if(dm.isFavorite()){
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_yellow);
        }else{
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_white);
        }


        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Favorite_Button")
                        .setAction("Click Event For ID : "+dm.getID()+" : Email :"+AppContext.getPrimaryAccount(context))
                        .build());

                if(dm.isFavorite()){
                    //remove the item from the favourates list
                    AppContext.removeFromFavoriteList(dm.getID());
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite_white);

                    //update the list
                    AppContext.allNewsList.get(position).setFavorite(false);
                }else{
                    //add the item to the favourates list
                    AppContext.addToFavoriteList(dm.getID(),dm);
                    holder.ivFavorite.setImageResource(R.drawable.ic_favorite_yellow);

                    //update the list
                    AppContext.allNewsList.get(position).setFavorite(true);

                }

                AppContext.favoriteNewsList = AppContext.ConvertMapToList(AppContext.favoriteNewsMap);
            }
        });

        holder.cvNewsData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(context, "go to page : "+ dm.getURL(), Toast.LENGTH_SHORT).show();

                Intent newIntent = new Intent(context, VerticalScrollViewActivity.class);
                newIntent.putExtra(AppContext.KEY_LIST_POSITION,position);
                newIntent.putExtra(AppContext.KEY_NEWS_LIST,AppContext.VALUE_LIST_ALL);
                context.startActivity(newIntent);

               /* Intent myIntent = new Intent(context, WebViewActivity.class);
                myIntent.putExtra("URL",dm.getURL());
                myIntent.putExtra("PUBLISHER",dm.getPUBLISHER());
                context.startActivity(myIntent);*/


            }
        });

        /** code for animation to the recycler list view - STARTS **/

        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
        /** code for animation to the recycler list view - ENDS **/

    }

    @Override
    public int getItemCount() {
        return allNewsList.size();
    }

}
