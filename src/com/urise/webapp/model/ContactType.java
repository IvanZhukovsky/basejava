package com.urise.webapp.model;

public enum ContactType {
    TELEFON ("Тел."),
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
        return title + ": " + value;
    }
    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    public String toLink(String href) {
        return toLink(href, title);
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }

}
