package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private List<String> content;
    private static final long serialVersionUID = 1L;

    public ListSection() {
    }

    public ListSection(String... items) {
        this(Arrays.asList(items));
    }

    public ListSection(List<String> content) {
        Objects.requireNonNull(content, "content must not be null");
        this.content = content;
    }

    public List<String> getContent() {
        return content;
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

        StringBuilder items = new StringBuilder();
        for (String item : content) {
            items.append(item + "\n");
        }
//        String items = "";
//        for (String item : content) {
//            items = items + item + "\n";
//        }
        return items.toString();
    }
}
