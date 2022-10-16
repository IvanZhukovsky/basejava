package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;

public class Organization implements Serializable {

    private final Link homePage;
    private final List<Period> periods;

    public Organization(String name, String url, Period... periods) {
        this(new Link(name, url), Arrays.asList(periods));
    }

    public Organization(Link homePage, List<Period> periods) {
        this.homePage = homePage;
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "homePage=" + homePage +
                ", periods=" + periods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homePage.equals(that.homePage)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    public static class Period implements Serializable{
        private LocalDate beginDate;
        private LocalDate endDate;
        private String title;
        private String description;
        private static final long serialVersionUID = 1L;

        public Period(int startYear, Month startMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), NOW, title, description);
        }

        public Period(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), title, description);
        }

        public Period(LocalDate beginDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(beginDate, "beginDate must be not null");
            Objects.requireNonNull(endDate, "endDate must be not null");
            Objects.requireNonNull(title, "title must be not null");
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
}
