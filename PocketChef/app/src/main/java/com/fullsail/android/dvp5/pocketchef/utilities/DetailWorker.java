package com.fullsail.android.dvp5.pocketchef.utilities;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fullsail.android.dvp5.pocketchef.ListItem;
import com.fullsail.android.dvp5.pocketchef.Recipe;
import com.fullsail.android.dvp5.pocketchef.fragments.DetailsFragment;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DetailWorker extends Worker {
    private final Context mContext;

    public DetailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String baseHead = "https://api.spoonacular.com/recipes/";
        String baseTail = "/information?apiKey=56113b8493f8442dae66892e54246bfa&includeNutrition=true";
        String webAddress = baseHead + DetailsFragment.ID + baseTail;
        String jsonData = "";
        HttpsURLConnection connection;
        Recipe recipe = null;

        try {
            URL url = new URL(webAddress);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

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
            JSONArray ingredients = obj.getJSONArray("extendedIngredients");
            ArrayList<ListItem> ingredientsList = new ArrayList<>();

            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);
                String name = ingredient.getString("name");
                int amount = ingredient.getInt("amount");
                String unit = ingredient.getString("unit");
                String quantity = amount + unit;

                ingredientsList.add(new ListItem(name, quantity));
            }

            String title = obj.getString("title");
            String image = obj.getString("image");
            JSONObject nutrition = obj.getJSONObject("nutrition");
            JSONArray nutrients = nutrition.getJSONArray("nutrients");
            ArrayList<ListItem> nutritionList = new ArrayList<>();

            for (int i = 0; i < nutrients.length(); i++) {
                JSONObject nutrient = nutrients.getJSONObject(i);
                String name = nutrient.getString("name");
                int amount = nutrient.getInt("amount");
                String unit = nutrient.getString("unit");
                String quantity = amount + unit;

                nutritionList.add(new ListItem(name, quantity));
            }

            String summary = obj.getString("summary");
            String[] sentences = summary.split("\\.");
            String descript = sentences[0];
            String instructions = obj.getString("instructions");

            recipe = new Recipe(DetailsFragment.ID, title, descript, instructions, image, ingredientsList, nutritionList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(DetailsFragment.BROADCAST_ACTION);
        intent.putExtra("ExtraRecipe", recipe);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        return Result.success();
    }
}
