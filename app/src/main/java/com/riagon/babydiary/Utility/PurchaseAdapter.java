package com.riagon.babydiary.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;

import com.riagon.babydiary.R;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.MyViewHolder> {
    private Context mContext;
    private List<SkuDetails> skuDetailsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView purchaseTitle;
        public TextView purchasePrice;

        public MyViewHolder(View view) {
            super(view);
            purchasePrice = view.findViewById(R.id.purchase_price);
            purchaseTitle = view.findViewById(R.id.purchase_title);


        }
    }


    public PurchaseAdapter(Context mContext, List<SkuDetails> skuDetailsList) {
        this.mContext = mContext;
        this.skuDetailsList = skuDetailsList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        SkuDetails skuDetails = skuDetailsList.get(position);

        holder.purchaseTitle.setText(skuDetails.getTitle());
        holder.purchasePrice.setText(skuDetails.getPrice());

        //  Log.i("DETAIL","PhotoAdapter:" + photo.getPhotoId());


    }


    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }
}
