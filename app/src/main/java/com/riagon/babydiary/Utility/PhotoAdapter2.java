package com.riagon.babydiary.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.R;
import java.util.List;

public class PhotoAdapter2 extends RecyclerView.Adapter<PhotoAdapter2.MyViewHolder> {
    private Context mContext;
    private List<Photo> photosList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.photo);
            cardView = (CardView) view.findViewById(R.id.card_view_photo);
        }
    }


    public PhotoAdapter2(Context mContext, List<Photo> photosList) {
        this.mContext = mContext;
        this.photosList = photosList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DbBitmapUtility bm = new DbBitmapUtility();
        Photo photo = photosList.get(position);

        if (photo.getPhotoImage()==null)
        {

        }
        else
        {
            holder.imageView.setImageBitmap(bm.getImage(photo.getPhotoImage()));
        }

//        holder.cardView.setOnClickListener(v -> {
//
//            Intent intent = new Intent(mContext, PhotoDetailActivity.class);
//            intent.putExtra(PHOTO_ID, photo);
//            intent.putExtra(GROWTH_ID, photo.getGrowthId());
//            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
//            intent.putExtra(POSITION, position);
//            //startActivity(intent);
//            ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_UPDATE_PHOTO);
//
//
//
//        });

        // holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, setTint(stats.getFixedID())));

    }



    @Override
    public int getItemCount() {
        return photosList.size();
    }
}



