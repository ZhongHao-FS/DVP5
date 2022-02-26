package com.fullsail.android.dvp5.pocketchef;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private final int id;
    private final String title;
    private final String instruction;
    private final String imageLink;
    private final ArrayList<ListItem> ingredients;
    private final ArrayList<ListItem> nutrition;

    public Recipe(int _id, String _title, String _instruction, String _image, ArrayList<ListItem> _ingredients, ArrayList<ListItem> _nutrition) {
        id = _id;
        title = _title;
        instruction = _instruction;
        imageLink = _image;
        ingredients = _ingredients;
        nutrition = _nutrition;
    }

    public String getTitle() {
        return title;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getImageLink() {
        return imageLink;
    }

    public ArrayList<ListItem> getIngredients() { return ingredients; }

    public ArrayList<ListItem> getNutrition() { return nutrition; }
}
