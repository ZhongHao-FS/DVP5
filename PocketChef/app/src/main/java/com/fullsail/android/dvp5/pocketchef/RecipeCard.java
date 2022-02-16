package com.fullsail.android.dvp5.pocketchef;

import java.io.Serializable;

public class RecipeCard implements Serializable {
    private final int id;
    private final String title;
    private final String descipt;
    private final String imageLink;

    public RecipeCard(int _id, String _title, String _descript, String _image) {
        id = _id;
        title = _title;
        descipt = _descript;
        imageLink = _image;
    }

    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return descipt;
    }

    public String getImageLink() {
        return imageLink;
    }
}
