package com.fullsail.android.dvp5.pocketchef;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RandomWorker extends Worker {
    private final Context mContext;

    public RandomWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String webAddress = "https://api.spoonacular.com/recipes/random?number=4&apiKey=56113b8493f8442dae66892e54246bfa";
        String jsonData = "";
        HttpsURLConnection connection;
        ArrayList<RecipeCard> recipeList = new ArrayList<>();

        try {
            URL url = new URL(webAddress);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            // Convert the stream to a string (think about out utils lib)
            jsonData = IOUtils.toString(url, StandardCharsets.UTF_8);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonData.equals("")) {
            return Result.failure();
        }

        try {
            JSONObject obj = new JSONObject(jsonData);
            JSONArray recipes = obj.getJSONArray("recipes");

            for (int i = 0; i < 4; i++) {
                JSONObject recipe = recipes.getJSONObject(i);
                int id = recipe.getInt("id");
                String title = recipe.getString("title");
                String summary = recipe.getString("summary");
                String[] sentences = summary.split("\\.");
                String descript = sentences[0];
                String imageLink = recipe.getString("image");

                recipeList.add(new RecipeCard(id, title, descript, imageLink));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(HomeFragment.BROADCAST_ACTION);
        intent.putExtra("ExtraCards", recipeList);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        return Result.success();
    }
}
