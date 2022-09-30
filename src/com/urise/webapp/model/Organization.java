package com.urise.webapp.model;

import java.util.ArrayList;

public class Organization {
    ArrayList<Period> periods = new ArrayList<>();
    String link;

    public Organization(ArrayList<Period> periods, String link) {
        this.periods = periods;
        this.link = link;
    }

    @Override
    public String toString() {

        String textGroup = link + "\n";
        for (Period period : periods) {
            textGroup = textGroup + period.toString() + "\n";
        }
        return textGroup;
    }
}
