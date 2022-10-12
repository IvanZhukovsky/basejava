package com.urise.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private LocalDate beginDate;
    private LocalDate endDate;
    private String title;
    private String description;

    public Period(LocalDate beginDate, LocalDate endDate, String title, String description) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return beginDate.equals(period.beginDate) && endDate.equals(period.endDate) && title.equals(period.title) && Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginDate, endDate, title, description);
    }

    @Override
    public String toString() {
        if (description == null) return beginDate + " - " + endDate + "   " + title;
        return beginDate + " - " + endDate + "   " + title + "\n" + description;
    }
}
