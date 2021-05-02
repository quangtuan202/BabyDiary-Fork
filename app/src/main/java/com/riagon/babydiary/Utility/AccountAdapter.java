package com.riagon.babydiary.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Care;
import com.riagon.babydiary.Model.CareRequest;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Notification.AlarmHelper;
import com.riagon.babydiary.R;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {
    private List<User> userList;
    private Context mContext;
    private DbBitmapUtility dbBitmapUtility;
    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;
    private Bitmap userProfileImage;
    private DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private String theme;
    String nameUser;

    //  private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public Button babyPicture;
        public TextView userName;
        private LinearLayout userDetailLayout;
        private Button profile_avatar;
        private CardView cardView;
        private Button acceptRequest;
        private Button cancelRequest;


        public MyViewHolder(View view, int viewType) {
            super(view);
            babyPicture = view.findViewById(R.id.babyPicture);
            userName = view.findViewById(R.id.userName);
            userDetailLayout = (LinearLayout) view.findViewById(R.id.user_detail_layout);
            cardView = view.findViewById(R.id.card_view);
            profile_avatar = view.findViewById(R.id.babyPicture);

            if (viewType == 1) {
                acceptRequest = view.findViewById(R.id.acceptRequestBt);
                cancelRequest = view.findViewById(R.id.cancelRequestBt);
            }

        }
    }


    public AccountAdapter(Context mContext, List<User> userListIn, DatabaseHelper db) {
        this.mContext = mContext;
        this.db = db;
        userList = new ArrayList<>();
        this.userList = userListIn;
        dbBitmapUtility = new DbBitmapUtility();
        dateFormatUtility = new DateFormatUtility(mContext);
        localDataHelper = new LocalDataHelper(mContext);
        formHelper = new FormHelper(mContext);

        if (!localDataHelper.getActiveUserId().equals("")) {
            theme = db.getUser(localDataHelper.getActiveUserId()).getUserTheme();
        } else {
            theme = "red";
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_list_request, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_list, parent, false);
        }

        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User user = userList.get(position);

        nameUser = user.getUserName();

        if (nameUser.length() > 9) {
            nameUser = nameUser.substring(0, 7) + "...";
        }
        holder.userName.setText(nameUser);


        if (user.getUserImage() == null) {
            setDefaultProfileImage(holder, getFirstChar(user.getUserName()));
        } else {
            holder.babyPicture.setText(null);
            userProfileImage = dbBitmapUtility.getImage(user.getUserImage());
            Drawable drawableIcon = new BitmapDrawable(mContext.getResources(), dbBitmapUtility.createCircleBitmap(userProfileImage));
            holder.babyPicture.setBackground(drawableIcon);

        }

        if (user.getUserId().equals(localDataHelper.getActiveUserId())) {
            //holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.lightGrey));
            //  holder.cardView.getBackground().setAlpha(45);

            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.opacity20));

        } else {
            // holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.design_default_color_background));
            //holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.redError));
        }


        if (user.getRequestStatus().equals("Yes")) {
            holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRequest(user);
                }
            });
            holder.cancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelRequest(user);
                }
            });

        } else {
            holder.userDetailLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAnotherUser(user);
                }
            });
            holder.profile_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAnotherUser(user);
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAnotherUser(user);
                }
            });
        }


    }


    public void cancelRequest(User user) {
        //String careRequestId = "BXbKOfJ8HiejWPVzrzZLSR9DUqC2";

        //Delete Request in Requestor

        // userList.remove(user);
        // notifyDataSetChanged();

        deleteRequest(user);
        //Delete Invite in Invitor
        deleteInvite(user);

    }


    public void acceptRequest(User user) {


//        userList.remove(user);
//        notifyDataSetChanged();

        updateInvite(user);
        deleteRequest(user);
        deleteInvite(user);
        addBabyRequester(user);
        addBabyLocal(user);

        userList.remove(user);
        notifyDataSetChanged();


    }

    public void deleteInvite(User user) {
        DatabaseReference inviteRootRef = database.getReference(user.getCreatedBy() + "/User");
        DatabaseReference inviteRef = inviteRootRef.child(user.getUserId() + "/caregiver").child(user.getRequestorId());

        inviteRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(mContext, "Delete Invite", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void deleteRequest(User user) {
        DatabaseReference careRequestRef = database.getReference("/CareRequest").child(user.getRequestorId());
        careRequestRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Toast.makeText(mContext,"Delete Request",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void updateInvite(User user) {

        DatabaseReference inviteRootRef = database.getReference(user.getCreatedBy() + "/User");
        DatabaseReference inviteRef = inviteRootRef.child(user.getUserId() + "/caregiver").child(currentUser.getUid());
        Care care = new Care();
        care.setCareEmail(currentUser.getEmail());
        care.setCareId(currentUser.getUid());
        care.setCareName(currentUser.getDisplayName());
        care.setCareURLPhoto(currentUser.getPhotoUrl().toString());
        care.setOwner(false);
        care.setAccept(true);

        inviteRef.setValue(care).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //   Toast.makeText(mContext,"Delete Invite",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void addBabyRequester(User user) {
        //   long id = getMaxID();
        DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
        Log.i("CARE", "Ad baby to request User ID " + user.getUserId());

        // userRef.child(String.valueOf(u.getUserId())).child("userImageUrl").setValue(url);
        User userFirebase = new User();
        userFirebase.setUserId(user.getUserId());
        userFirebase.setCreatedBy(user.getCreatedBy());
        userFirebase.setRequestStatus("Accept");

        userRef.child(String.valueOf(user.getUserId())).setValue(userFirebase).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("CARE", "Upload success");
                // db.updateUserSyn(u, true);
                //db.deleteUserId(String.valueOf(1));
                // db.updateUserSyn(user, true);
                // Write was successful!
                // ...
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });


    }


    public void addBabyLocal(User user) {
        user.setRequestStatus("Accept");
        db.addUserWithID(user);

        notifyDataSetChanged();
    }


    public void getAnotherUser(User user) {

        if (!localDataHelper.getActiveUserId().equals(user.getUserId()))
        {
            localDataHelper.setActiveUserId(user.getUserId());
            AlarmHelper alarmHelper = new AlarmHelper(mContext,db);
            alarmHelper.setNotification(user);
        }

        Intent i = ((Activity) mContext).getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(((Activity) mContext).getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
        ((Activity) mContext).finish();
    }

    public void setDefaultProfileImage(MyViewHolder holder, String name) {

        if (theme.equals("blue")) {
            holder.babyPicture.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_blue_circle));
        } else if (theme.equals("purple")) {
            holder.babyPicture.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_purple_circle));
        } else if (theme.equals("red")) {
            holder.babyPicture.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_red_circle));
        } else if (theme.equals("pink")) {
            holder.babyPicture.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_pink_circle));
        } else if (theme.equals("green")) {
            holder.babyPicture.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_green_circle));
        }

        holder.babyPicture.setText(name);
        holder.babyPicture.setTextSize(16F);
        holder.babyPicture.setTextColor(Color.WHITE);
        holder.babyPicture.setPadding(5, 5, 5, 5);
    }

    public String getFirstChar(String name) {
        return String.valueOf(name.charAt(0));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    @Override
    public int getItemViewType(int position) {

        int viewType = 0; //Default is 1
//        if (activitiesList.get(position).getActivitiesId() == 1)
//            viewType = 1; //if zero, it will be a header view
        if (userList.get(position).getRequestStatus().equals("Yes")) {
            viewType = 1;

        }

        return viewType;

        //  return super.getItemViewType(position);
    }


}