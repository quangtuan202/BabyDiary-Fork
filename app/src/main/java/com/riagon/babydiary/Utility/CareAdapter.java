package com.riagon.babydiary.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Care;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CareAdapter extends RecyclerView.Adapter<CareAdapter.MyViewHolder> {
    private List<Care> careList;
    private Context mContext;
    //    private DbBitmapUtility dbBitmapUtility;
//    private DateFormatUtility dateFormatUtility;
//    private FormHelper formHelper;
//    private Bitmap userProfileImage;
//    private DatabaseHelper db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference careRootRef;
    private User activeUser;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // public ImageView babyPicture;
        public CircularImageView care_profile_image;
        public TextView careName;
        private Button deleteCare;


        public MyViewHolder(View view) {
            super(view);
            deleteCare = view.findViewById(R.id.delete_care);
            careName = view.findViewById(R.id.care_name);
            care_profile_image = view.findViewById(R.id.care_profile_image);
            //  userAge = (TextView) view.findViewById(R.id.userAge);
        }
    }

    public CareAdapter(Context mContext, List<Care> careList, User activeUser) {
        this.mContext = mContext;
        this.careList = careList;
        //   careList.addAll(db.getAllUsers());
//        dbBitmapUtility = new DbBitmapUtility();
//        dateFormatUtility = new DateFormatUtility(mContext);
//        formHelper = new FormHelper(mContext);
        this.activeUser = activeUser;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.care_list, parent, false);
        //   View itemView = LayoutInflater.from(parent.getContext())
        //          .inflate(R.layout.user_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Care care = careList.get(position);

//        if (care.getCareName() != null) {
//            holder.careName.setText(care.getCareName());
//        } else {
//            holder.careName.setText(care.getCareEmail());
//        }

        Resources res = holder.itemView.getContext().getResources();
        //    Log.i("TAG",care.getCareURLPhoto().toString());


        if (care.getOwner()) {
            //Drawable cancelInviteBt = res.getDrawable(R.drawable.ic_delete);
            holder.deleteCare.setVisibility(View.GONE);
            setUserProfileImage(care, holder);
            holder.careName.setText(care.getCareName());

        } else {
            if (!care.getAccept()) {
//                Glide.with(mContext).load(care.getCareURLPhoto())
//                        .into(holder.care_profile_image);
                Drawable icon90 = res.getDrawable(R.drawable.ic_invitation);
                // Drawable cancelInviteBt = res.getDrawable(R.drawable.ic_cancelgrey);
                holder.care_profile_image.setBackground(icon90);
                //holder.deleteCare.setBackground(cancelInviteBt);
                holder.deleteCare.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancelgrey, 0);
                holder.careName.setText(care.getCareEmail());

            } else {
                // Drawable iconDelete = res.getDrawable(R.drawable.ic_delete_account);
                // iconDelete.setBounds(0, 0, 60, 60);
                // holder.deleteCare.setCompoundDrawables(iconDelete,null, null, null);
                holder.deleteCare.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_account, 0);
                setUserProfileImage(care, holder);
                holder.careName.setText(care.getCareName());
                // Glide.with(mContext).load(getImage("ic_add")).into(holder.care_profile_image);
            }

        }


        holder.deleteCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCare(care);
            }
        });


    }


    public void setUserProfileImage(Care care, MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (care.getCareURLPhoto() != null) {
            Glide.with(mContext).load(care.getCareURLPhoto())
                    .into(holder.care_profile_image);
        } else {
            Drawable defaultUserImage = res.getDrawable(R.drawable.ic_invitation);
            holder.care_profile_image.setBackground(defaultUserImage);

        }


    }


//    public void  stopShareBaby()
//    {
//
//
//    }

    public void deleteCare(Care care) {

        //  DatabaseReference careRootRef = database.getReference(care.getCareId() + "/User");


        //Check if this user belong to other
        if (activeUser.getRequestStatus().equals("Accept")) {

            //Remove care from baby belong to other
            DatabaseReference userRequestRef = database.getReference(activeUser.getCreatedBy() + "/User").child(activeUser.getUserId() + "/caregiver").child(care.getCareId());

            //Remove baby from user list in fire
            DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User").child(activeUser.getUserId());
            // Attach a listener to read the data at our posts reference
            //  userRequestRef.child(activeUser.getUserIDByOwner() + "/caregiver").child(care.getCareId());

            userRequestRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(mContext, "Delete Care", Toast.LENGTH_SHORT).show();

                }
            });


            userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(mContext, "Delete User", Toast.LENGTH_SHORT).show();

                }
            });

        }

        //if belong to you
        else {
            if (care.getAccept()) {
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");

                userRef.child(activeUser.getUserId() + "/caregiver").child(care.getCareId());

                //Remove care from baby belong to other
                DatabaseReference userRequestRef = database.getReference(care.getCareId() + "/User").child(activeUser.getUserId());


                userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(mContext, "Delete Care", Toast.LENGTH_SHORT).show();

                    }
                });


//                userRequestRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Toast.makeText(mContext, "Delete User", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
            } else {
                deleteInvite(care);
                deleteRequest(care);
            }


        }
        //DatabaseReference inviteRef = careRootRef.child(care.getCareId());


    }


    public void deleteInvite(Care care) {
        DatabaseReference inviteRootRef = database.getReference(currentUser.getUid() + "/User");
        DatabaseReference inviteRef = inviteRootRef.child(activeUser.getUserId() + "/caregiver").child(care.getCareId());

        inviteRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(mContext, "Delete Invite", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void deleteRequest(Care care) {
        DatabaseReference careRequestRef = database.getReference("/CareRequest").child(care.getCareId());
        careRequestRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Toast.makeText(mContext,"Delete Request",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }


    @Override
    public int getItemCount() {
        return careList.size();
    }


}
