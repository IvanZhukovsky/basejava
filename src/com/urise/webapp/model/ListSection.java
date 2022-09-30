package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Objects;

public class ListSection extends Section{
    ArrayList<String> content;

    public ListSection(ArrayList<String> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        String items = "";
        for (String item : content) {
            items = items + item + "\n";
        }
        return items;
    }
}
