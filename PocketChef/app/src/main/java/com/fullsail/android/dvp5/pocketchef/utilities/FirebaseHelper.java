package com.fullsail.android.dvp5.pocketchef.utilities;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.firebase.ui.auth.AuthUI;
import com.fullsail.android.dvp5.pocketchef.ListItem;
import com.fullsail.android.dvp5.pocketchef.RecipeCard;
import com.fullsail.android.dvp5.pocketchef.fragments.CardListFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.ShoppingFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseHelper {
    public static FirebaseUser mUser;
    public static DatabaseReference mDatabase;

    public void signIn(ActivityResultLauncher<Intent> launcher) {
        List<AuthUI.IdpConfig> provider = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).build();
        launcher.launch(signInIntent);
    }

    public void signOut(Context context) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(task -> {
            mUser = null;
            Toast.makeText(context, "Signed out successfully!", Toast.LENGTH_SHORT).show();
        });
    }

    public static void writeRecipeCard(Context context, int _id, String _title, String _desc, String _image) {
        if (mUser != null && mDatabase != null) {
            RecipeCard card = new RecipeCard(_id, _title, _desc, _image);
            mDatabase.child(mUser.getUid()).child("recipes").child(String.valueOf(_id)).setValue(card).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Recipe added!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void writeShoppingItem(String _name, String _quantity) {
        if (mUser != null && mDatabase != null) {
            ListItem item = new ListItem(_name, _quantity);
            mDatabase.child(mUser.getUid()).child("shoppingList").child(_name).setValue(item);
        }
    }

    public static void deleteShoppingItem(String _name) {
        if (mUser != null && mDatabase != null) {
            mDatabase.child(mUser.getUid()).child("shoppingList").child(_name).removeValue();
        }
    }

    public static void readRecipeCards(Context context) {
        if (mUser != null && mDatabase != null) {
            mDatabase.child(mUser.getUid()).child("recipes").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ArrayList<RecipeCard> cards = new ArrayList<>();
                    for (DataSnapshot snapshot: task.getResult().getChildren()) {
                        String des = String.valueOf(snapshot.child("des").getValue());
                        Integer id = snapshot.child("id").getValue(Integer.class);
                        String link = String.valueOf(snapshot.child("imageLink").getValue());
                        String title = String.valueOf(snapshot.child("title").getValue());

                        if (id != null) {
                            RecipeCard card = new RecipeCard(id, title, des, link);
                            cards.add(card);
                        }
                    }

                    CardListFragment.mCards = cards;
                    Intent intent = new Intent(CardListFragment.BROADCAST_ACTION);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } else {
                    Toast.makeText(context, "Error getting data!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void readShoppingList(Context context) {
        if (mUser != null && mDatabase != null) {
            mDatabase.child(mUser.getUid()).child("shoppingList").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ArrayList<ListItem> list = new ArrayList<>();
                    for (DataSnapshot snapshot: task.getResult().getChildren()) {
                        String name = String.valueOf(snapshot.getKey());
                        String quantity = String.valueOf(snapshot.child("quantity").getValue());
                        ListItem item = new ListItem(name, quantity);
                        list.add(item);
                    }

                    Intent intent = new Intent(ShoppingFragment.BROADCAST_ACTION);
                    intent.putExtra("ExtraList", list);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } else {
                    Toast.makeText(context, "Error getting data!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
