package com.urise.webapp.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume>{

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

    public Resume() {
        this(UUID.randomUUID().toString(), "default name");
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setContact(ContactType contactType, String textContact) {
        contacts.put(contactType, textContact);
    }

    public String getContact(ContactType contactType) {
        return contacts.get(contactType);
    }

    public void setSection(SectionType sectionType, AbstractSection abstractSection) {
        sections.put(sectionType, abstractSection);
    }

    public AbstractSection getSection(SectionType sectionType) {
        return sections.get(sectionType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
