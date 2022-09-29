package com.urise.webapp.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume>{

    // Unique identifier
    private final String uuid;
    private final String fullName;
    public Contacts contacts = new Contacts();
    public Map<SectionType, Section> sectionMap = new HashMap<>();

    public Resume() {
        this(UUID.randomUUID().toString(), "default name");
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
        sectionMap.put(SectionType.PERSONAL, new TextSections());
        sectionMap.put(SectionType.OBJECTIVE, new TextSections());
        sectionMap.put(SectionType.ACHIEVEMENT, new ListSection());
        sectionMap.put(SectionType.QUALIFICATIONS, new ListSection());
        sectionMap.put(SectionType.EXPERIENCE, new TextSections());
        sectionMap.put(SectionType.EDUCATION, new TextSections());
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }

    public class Contacts {
        private Map<ContactType, String> contactsMap = new HashMap<>();

        public void setContact(ContactType contactType, String contact) {
            contactsMap.put(contactType, contact);
        }

        public String getContact(ContactType contactType) {
            return contactsMap.get(contactType);
        }
    }

    interface Section {
        public void setContent(String content);
        public Object getContent();

    }

    class TextSections implements Section{
        private String content;

        @Override
        public Object getContent() {
            return content;
        }

        @Override
        public void setContent(String content) {
            this.content = content;
        }
    }

    class ListSection implements Section {
        private ArrayList<String> content = new ArrayList<>();

        @Override
        public Object getContent() {
            return content;
        }

        @Override
        public void setContent(String content) {
            this.content.add(content);
        }
    }
}
