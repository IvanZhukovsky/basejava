package com.urise.webapp.model;

public enum ContactType {
    TELEFON ("Тел.:"),
    SKYPE ("Skype:"),
    EMAIL ("Почта:"),
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

}
