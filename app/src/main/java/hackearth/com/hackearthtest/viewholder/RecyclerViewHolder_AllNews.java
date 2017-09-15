package hackearth.com.hackearthtest.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import hackearth.com.hackearthtest.R;
import hackearth.com.hackearthtest.font.MediumFont;
import hackearth.com.hackearthtest.font.NormalFont;

/**
 * Created by apple on 14/09/17.
 */

public class RecyclerViewHolder_AllNews extends RecyclerView.ViewHolder {


    //ui elements
    public MediumFont mfTitleString;
    public NormalFont nfPublisher, nfCategory,nfHostName,nfTimestamp;
    public ImageView civFavorite;
    public CardView cvNewsData;
    public CircleImageView civLogoImage;
    public ImageView ivMainImage;



    public RecyclerViewHolder_AllNews(View convertView) {
        super(convertView);


        mfTitleString = (MediumFont) convertView.findViewById(R.id.mfTitleString);
        nfPublisher = (NormalFont) convertView.findViewById(R.id.nfPublisher);
        nfCategory = (NormalFont) convertView.findViewById(R.id.nfCategory);
        nfHostName = (NormalFont) convertView.findViewById(R.id.nfHostName);
        nfTimestamp = (NormalFont) convertView.findViewById(R.id.nfTimestamp);
        civFavorite = (ImageView) convertView.findViewById(R.id.civFavorite);
        cvNewsData = (CardView) convertView.findViewById(R.id.cvNewsData);
        civLogoImage = (CircleImageView) convertView.findViewById(R.id.civLogoImage);

        convertView.setTag(this);

    }
}
