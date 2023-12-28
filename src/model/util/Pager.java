package model.util;

import java.util.ArrayList;
import java.util.List;

public class Pager<T> {

    private List<T> items = new ArrayList<>();

    private final int page;

    private final int totalItems;

    public Pager(int page, int totalItems) {
        this.page = page;
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        if (totalItems <= 0 && !items.isEmpty()) {
            throw new IllegalArgumentException("Need to fill Total Items atribute to build the pagination.");
        } else {
            return items;
        }
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public int getTotalItems() {
        return totalItems;
    }

}
