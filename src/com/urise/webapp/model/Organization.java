package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Objects;

public class Organization {
    private ArrayList<Period> periods = new ArrayList<>();
    String link;

    public Organization(ArrayList<Period> periods, String link) {
        this.periods = periods;
        this.link = link;
    }

    public ArrayList<Period> getPeriods() {
        return periods;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {

        String textGroup = link + "\n";
        for (Period period : periods) {
            textGroup = textGroup + period.toString() + "\n";
        }
        return textGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return periods.equals(that.periods) && link.equals(that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periods, link);
    }
}
