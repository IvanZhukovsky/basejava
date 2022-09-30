package com.urise.webapp.model;

import java.util.Objects;

public class Contact {
    String textConact;

    public Contact(String textConact) {
        this.textConact = textConact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return textConact.equals(contact.textConact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textConact);
    }

    @Override
    public String toString() {
        return textConact;
    }
}
