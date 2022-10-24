package com.urise.webapp.storage.serializer;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionType, AbstractSection> sections = resume.getSections();
            dos.writeInt(sections.size());

            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {

//                switch (entry.getKey()) {
//                    case OBJECTIVE, PERSONAL -> writeTextSection(dos, entry);
//                    case ACHIEVEMENT, QUALIFICATIONS -> writeListSection(dos, entry);
//                    case EXPERIENCE, EDUCATION -> writeOrganizationSection(dos, entry);
//                }

                //Запись TextSections
                if (entry.getKey().equals(SectionType.OBJECTIVE) || entry.getKey().equals(SectionType.PERSONAL)) {
                    writeTextSection(dos, entry);
                }
                //Запись ListSections
                if (entry.getKey().equals(SectionType.ACHIEVEMENT) || entry.getKey().equals(SectionType.QUALIFICATIONS)) {
                    writeListSection(dos, entry);
                }
                //Запись OrganizationsSections
                if (entry.getKey().equals(SectionType.EXPERIENCE) || entry.getKey().equals((SectionType.EDUCATION))) {
                    writeOrganizationSection(dos, entry);
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                String value = dis.readUTF();
                resume.addContact(contactType, value);
            }

            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                //Чтение TextSections
                if (sectionType.equals(SectionType.OBJECTIVE) || sectionType.equals(SectionType.PERSONAL)) {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));
                }
                //Чтение ListSections
                if (sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATIONS)) {
                    List<String> notes = new ArrayList<>();
                    int notesSize = dis.readInt();
                    for (int j = 0; j < notesSize; j++) {
                        notes.add(dis.readUTF());
                    }
                    resume.addSection(sectionType, new ListSection(notes));
                }
                //Чтение OrganizationsSections
                if (sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)) {
                    //Создаем секцию в объекте resume
                    resume.addSection(sectionType, new OrganizationSection(new ArrayList<>()));
                    OrganizationSection organizationSection = (OrganizationSection) resume.getSections().get(sectionType);
                    //Считываем количество организаций в данной секции
                    int organizationsSize = dis.readInt();
                    for (int j = 0; j < organizationsSize; j++) {
                        //Считываем данные необходимые для создания объекта Link
                        String linkName = dis.readUTF();
                        String linkUrl = dis.readUTF();
                        if (linkUrl.equals("null")) {
                            linkUrl = null;
                        }
                        //Создаем организацию и кладем ее в коллекцию секции
                        organizationSection.getOrganizations().add(new Organization(new Link(linkName, linkUrl), new ArrayList<>()));
                        List<Organization> organizations = organizationSection.getOrganizations();

                        //Считываем количество периодов в данной организации
                        int periodSize = dis.readInt();
                        for (int k = 0; k < periodSize; k++) {
                            //Считываем дату начала
                            int beginYear = dis.readInt();
                            int beginMonth = dis.readInt();
                            int beginDay = dis.readInt();
                            //Считываем дату конца
                            int endYear = dis.readInt();
                            int endMonth = dis.readInt();
                            int endDay = dis.readInt();
                            //Создаем период в этой организации
                            String title = dis.readUTF();
                            String description = dis.readUTF();
                            if (description.equals("null")) {
                                description = null;
                            }
                            organizations.get(j).getPeriods().add(new Organization.Period(
                                    LocalDate.of(beginYear, beginMonth, beginDay),
                                    LocalDate.of(endYear, endMonth, endDay), title
                                    , description));
                        }
                    }
                }
            }
            return resume;
        }
    }

    private void writeTextSection(DataOutputStream dos, Map.Entry<SectionType, AbstractSection> entry) throws IOException {
        dos.writeUTF(entry.getKey().name());
        TextSection textSection = (TextSection) entry.getValue();
        dos.writeUTF(textSection.getContent());
    }

    private void writeListSection(DataOutputStream dos, Map.Entry<SectionType, AbstractSection> entry) throws IOException {
        dos.writeUTF(entry.getKey().name());
        ListSection listSection = (ListSection) entry.getValue();
        dos.writeInt(listSection.getContent().size());
        for (String note : listSection.getContent()) {
            dos.writeUTF(note);
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, Map.Entry<SectionType, AbstractSection> entry) throws IOException {
        dos.writeUTF(entry.getKey().name());
        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
        dos.writeInt(organizationSection.getOrganizations().size());
        for (Organization organization : organizationSection.getOrganizations()) {
            dos.writeUTF(organization.getHomePage().getName());
            if (organization.getHomePage().getUrl() != null) {
                dos.writeUTF(organization.getHomePage().getUrl());
            } else {
                dos.writeUTF("null");
            }
            dos.writeInt(organization.getPeriods().size());
            for (Organization.Period period : organization.getPeriods()) {

                dos.writeInt(period.getBeginDate().getYear());
                dos.writeInt(period.getBeginDate().getMonthValue());
                dos.writeInt(period.getBeginDate().getDayOfMonth());

                dos.writeInt(period.getEndDate().getYear());
                dos.writeInt(period.getEndDate().getMonthValue());
                dos.writeInt(period.getEndDate().getDayOfMonth());

                dos.writeUTF(period.getTitle());
                if (period.getDescription() != null) {
                    dos.writeUTF(period.getDescription());
                } else {
                    dos.writeUTF("null");
                }
            }
        }
    }
}
