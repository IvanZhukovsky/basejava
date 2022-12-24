package com.urise.webapp.model;

public enum ContactType {
    TELEFON ("Тел.") {
        @Override
        protected String toHtml0(String value) {
            return getTitle() + ": " + value;
        }
    },
    SKYPE ("Skype"){
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": " + toLink("skype:" + value, value);
        }
    },
    EMAIL ("Почта") {
        @Override
        public String toHtml0(String value) {

            return getTitle() + ": " + toLink("mailto:" + value, value);
        }
    },
    LINKEDIN ("Профиль в LinkedIn"),
    GITHUB ("Профиль в GitHub"),
    STACKOVERFLOW ("Профиль в Stackoverflow"),
    HOMEPAGE ("Домашняя страница");

    private String title;

    ContactType (String title) {
        this.title = title;
    }

    public String getTitle (){
        return title;
    }

    protected String toHtml0(String value) {
        return toLink(value, getTitle());
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }

}
