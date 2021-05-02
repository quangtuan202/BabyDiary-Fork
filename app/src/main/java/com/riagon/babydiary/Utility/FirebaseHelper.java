package com.riagon.babydiary.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseHelper {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    public FirebaseHelper() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


}
