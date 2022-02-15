package com.fullsail.android.dvp5.pocketchef;

public class RecipeCard {
    private final int id;
    private final String title;
    private final String imageLink;

    public RecipeCard(int _id, String _title, String _image) {
        id = _id;
        title = _title;
        imageLink = _image;
    }

    public String getTitle() {
        return title;
    }

    public String getImageLink() {
        return imageLink;
    }
}
