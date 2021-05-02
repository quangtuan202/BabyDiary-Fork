package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.riagon.babydiary.Utility.GridSpacingItemDecoration;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.PhotoAdapter2;
import com.riagon.babydiary.Utility.PurchaseAdapter;
import com.riagon.babydiary.Utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class UpgradeProActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    public LocalDataHelper localDataHelper;
    public RecyclerView recyclerView;
    public RelativeLayout purchaseLayout;
    public PurchaseAdapter purchaseAdapter;
    public List<SkuDetails> skuDetailsList2 = new ArrayList();
    private BillingClient billingClient;
    private List skuInappList = new ArrayList();
    private List skuSubList = new ArrayList();
    public String skuLifetime = "life_time";
    public String skuMonthly = "monthly";
    public String skuYearly = "yearly";
    private AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_pro);
        recyclerView = findViewById(R.id.recyclerview_purchase);
        purchaseLayout = findViewById(R.id.purchase_layout);
        localDataHelper = new LocalDataHelper(this);

        if (!localDataHelper.getPurchaseState().equals("")) {
            purchaseLayout.setVisibility(View.GONE);
        }


        skuSubList.add(skuMonthly);
        skuSubList.add(skuYearly);
        skuInappList.add(skuLifetime);

        setupBillingClient();


    }


    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    loadAllSKUs();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void loadAllSKUs() {
        //  Toast.makeText(ProUpgradeActivity.this, "", Toast.LENGTH_SHORT).show();

        if (billingClient.isReady()) {
            //    Toast.makeText(ProUpgradeActivity.this, "billingclient ready", Toast.LENGTH_SHORT).show();

            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(skuInappList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            SkuDetailsParams params2 = SkuDetailsParams.newBuilder()
                    .setSkusList(skuSubList)
                    .setType(BillingClient.SkuType.SUBS)
                    .build();


            purchaseAdapter = new PurchaseAdapter(getApplicationContext(), skuDetailsList2);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(purchaseAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, final int position) {

                    BillingFlowParams billingFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(skuDetailsList2.get(position))
                            .build();
                    billingClient.launchBillingFlow(UpgradeProActivity.this, billingFlowParams);


                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                    // Toast.makeText(ProUpgradeActivity.this, "inside query" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                            && !skuDetailsList.isEmpty()) {

                        skuDetailsList2.addAll(skuDetailsList);
                        purchaseAdapter.notifyDataSetChanged();


                    }
                }
            });

            billingClient.querySkuDetailsAsync(params2, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                    // Toast.makeText(ProUpgradeActivity.this, "inside query" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                            && !skuDetailsList.isEmpty()) {

                        skuDetailsList2.addAll(skuDetailsList);
                        purchaseAdapter.notifyDataSetChanged();


                    }
                }
            });


        } else {

        }
        //Toast.makeText(ProUpgradeActivity.this, "billingclient not ready", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

        int responseCode = billingResult.getResponseCode();
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            //Log.d(TAG, "User Canceled" + responseCode);
        } else if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            // localData.setProUpgrade(true);
        } else {
            //Log.d(TAG, "Other code" + responseCode);
            // Handle any other error codes.
        }

    }


    private void handlePurchase(Purchase purchase) {

        if (purchase.getSku().equals(skuLifetime)) {
            localDataHelper.setPurchaseState(skuLifetime);

        } else if (purchase.getSku().equals(skuMonthly)) {

            localDataHelper.setPurchaseState(skuMonthly);

        } else if (purchase.getSku().equals(skuYearly)) {

            localDataHelper.setPurchaseState(skuYearly);
        }


//        Date purchaseDate = new Date(purchase.getPurchaseTime());
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(purchaseDate);

        localDataHelper.setPurchaseTime(purchase.getPurchaseTime());

        // Acknowledge the purchase if it hasn't already been acknowledged.
//        if (!purchase.isAcknowledged()) {
//
//            AcknowledgePurchaseParams acknowledgePurchaseParams =
//                    AcknowledgePurchaseParams.newBuilder()
//                            .setPurchaseToken(purchase.getPurchaseToken())
//                            .build();
//
//            acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
//                @Override
//                public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
//
//                }
//
//            };
//
//            billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
//
//        }

        purchaseLayout.setVisibility(View.GONE);
        //   proVersionLayout.setVisibility(View.VISIBLE);
        // localData.setProUpgrade(true);

//            Intent intent = new Intent();
//            intent.putExtra("IS_PRO", true);
//            setResult(RESULT_OK, intent);
        //  Toast.makeText(this, "Purchase done. you are now a premium member.", Toast.LENGTH_SHORT).show();
        //  }
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = this.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
