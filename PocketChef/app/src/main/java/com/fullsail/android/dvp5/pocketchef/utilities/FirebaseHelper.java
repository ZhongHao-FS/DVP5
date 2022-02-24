package com.fullsail.android.dvp5.pocketchef.utilities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class FirebaseHelper {
    public static FirebaseUser mUser;

    public void signIn(ActivityResultLauncher<Intent> launcher) {
        List<AuthUI.IdpConfig> provider = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).build();
        launcher.launch(signInIntent);
    }

    public void signOut(Context context) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(task -> {
            mUser = null;
            Toast.makeText(context, "Signed out successfully!", Toast.LENGTH_SHORT).show();
        });
    }
}
