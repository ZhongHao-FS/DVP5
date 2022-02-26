package com.fullsail.android.dvp5.pocketchef;

import java.io.Serializable;

public class ListItem implements Serializable {
    private final String name;
    private final String quantity;

    public ListItem(String _name, String _quantity) {
        name = _name;
        quantity = _quantity;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
}
