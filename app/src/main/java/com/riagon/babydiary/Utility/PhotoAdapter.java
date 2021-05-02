package com.riagon.babydiary.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.riagon.babydiary.AddGrowthRecordActivity;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.PhotoDetailActivity;
import com.riagon.babydiary.R;

import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private Context mContext;
    private List<Photo> photosList;
    public static final String PHOTO_ID = "PHOTO_ID";
    public static final int REQUEST_CODE_UPDATE_PHOTO = 04061;
    public static final String PHOTO_COUNTER = "PHOTO_COUNTER";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.photo);
            cardView = (CardView) view.findViewById(R.id.card_view_photo);
        }
    }


    public PhotoAdapter(Context mContext, List<Photo> photosList) {
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

      //  Log.i("DETAIL","PhotoAdapter:" + photo.getPhotoId());

        if (photo.getPhotoImage() == null) {

        } else {
            holder.imageView.setImageBitmap(bm.getImage(photo.getPhotoImage()));
        }

        holder.imageView.setOnClickListener(v -> {
            //Log.i("DETAIL","PhotoAdapter:" + photo.getPhotoId());
            Intent intent = new Intent(mContext, PhotoDetailActivity.class);
            intent.putExtra(PHOTO_ID, photo);
           // intent.putExtra(AddGrowthRecordActivity.GROWTH_ID, photo.getGrowthId());
            intent.putExtra(PhotoDetailActivity.PHOTO_POSITION, position);
            intent.putExtra(PHOTO_COUNTER,photosList.size());
            ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_UPDATE_PHOTO);

        });


    }


    @Override
    public int getItemCount() {
        return photosList.size();
    }
}


