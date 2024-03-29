package com.urise.webapp.model;


import com.sun.org.apache.xpath.internal.operations.Or;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.Month;
import java.util.*;

import com.urise.webapp.util.DateUtil;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;
    // Unique identifier
    private String uuid;
    private String fullName;
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

    public List<Organization> getDefaultListOrg() {
        List<Organization> list = new ArrayList<>();
        list.add(new Organization("", null,
                new Organization.Period(DateUtil.DEFAULT, DateUtil.DEFAULT, "", "")));
        return list;
    }

    public void addDefaultOrg(SectionType sectionType) {
        if (sections.get(sectionType) != null) {
            OrganizationSection organizationSection = (OrganizationSection) sections.get(sectionType);
            boolean isExist = false;
            for (Organization organization : organizationSection.getOrganizations()) {
                for (Organization.Period period: organization.getPeriods()) {
                    if (period.getTitle().trim().length() == 0) isExist = true;
                }
                if (!isExist) {
                    organization.getPeriods().add(new Organization.Period(DateUtil.DEFAULT, DateUtil.DEFAULT, "", ""));
                }
            }
            for (Organization organization : organizationSection.getOrganizations()) {
                if (organization.getHomePage().getName().equals("")) {
                    return;
                }
            }
            organizationSection.getOrganizations().add(new Organization("", null,
                    new Organization.Period(DateUtil.DEFAULT, DateUtil.DEFAULT, "", "")));
        }

    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, AbstractSection> getSections() {
        return sections;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void addContact(ContactType contactType, String textContact) {
        contacts.put(contactType, textContact);
    }

    public String getContact(ContactType contactType) {
        return contacts.get(contactType);
    }

    public void addSection(SectionType sectionType, AbstractSection abstractSection) {
        sections.put(sectionType, abstractSection);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
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
