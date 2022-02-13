package com.fullsail.android.dvp5.pocketchef;

import androidx.annotation.Nullable;

public class Recipe {
    private final int id;
    private final String title;
    private final int calories;
    private final String carbs;
    private final String fat;
    private final String protein;
    private final String imageLink;

    public Recipe(int _id, String _title, int _calories, @Nullable String _carbs, @Nullable String _fat, @Nullable String _protein, String _image) {
        id = _id;
        title = _title;
        calories = _calories;
        carbs = _carbs;
        fat = _fat;
        protein = _protein;
        imageLink = _image;
    }

    public String getTitle() {
        return title;
    }

    public String getImageLink() {
        return imageLink;
    }
}
