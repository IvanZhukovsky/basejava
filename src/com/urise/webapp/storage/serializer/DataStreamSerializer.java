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
                switch (entry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        writeTextSection(dos, entry);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeListSection(dos, entry);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeOrganizationSection(dos, entry);
                        break;
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
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        readTextSection(resume, sectionType, dis);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        readListSection(resume, sectionType, dis);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        readOrganizationSection(resume, sectionType, dis);
                        break;
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

            dos.writeUTF(organization.getHomePage().getUrl() != null ? organization.getHomePage().getUrl() : "null");

            dos.writeInt(organization.getPeriods().size());
            for (Organization.Period period : organization.getPeriods()) {
                writeDate(dos, period.getBeginDate());
                writeDate(dos, period.getEndDate());
                dos.writeUTF(period.getTitle());
                dos.writeUTF(period.getDescription() != null ? period.getDescription() : "null");
            }
        }
    }

    private void writeDate(DataOutputStream dos, LocalDate localDate) throws IOException {
        dos.writeInt(localDate.getYear());
        dos.writeInt(localDate.getMonthValue());
        dos.writeInt(localDate.getDayOfMonth());
    }

    private void readTextSection(Resume resume, SectionType sectionType, DataInputStream dis) throws IOException {
        resume.addSection(sectionType, new TextSection(dis.readUTF()));
    }

    private void readListSection(Resume resume, SectionType sectionType, DataInputStream dis) throws IOException {
        List<String> notes = new ArrayList<>();
        int notesSize = dis.readInt();
        for (int j = 0; j < notesSize; j++) {
            notes.add(dis.readUTF());
        }
        resume.addSection(sectionType, new ListSection(notes));
    }

    private void readOrganizationSection(Resume resume, SectionType sectionType, DataInputStream dis) throws IOException {
        //Создаем секцию в объекте resume
        resume.addSection(sectionType, new OrganizationSection(new ArrayList<>()));
        OrganizationSection organizationSection = (OrganizationSection) resume.getSections().get(sectionType);
        //Считываем количество организаций в данной секции
        int organizationsSize = dis.readInt();
        for (int j = 0; j < organizationsSize; j++) {
            //Считываем данные необходимые для создания объекта Link
            String linkName = dis.readUTF();
            String linkUrl = dis.readUTF();
            linkUrl = linkUrl.equals("null") ? null : linkUrl;
            //Создаем организацию и кладем ее в коллекцию секции
            organizationSection.getOrganizations().add(new Organization(new Link(linkName, linkUrl), new ArrayList<>()));
            List<Organization> organizations = organizationSection.getOrganizations();

            //Считываем количество периодов в данной организации
            int periodSize = dis.readInt();
            for (int k = 0; k < periodSize; k++) {
                //Считываем дату начала
                LocalDate beginDate = readDate(dis);
                //Считываем дату конца
                LocalDate endDate = readDate(dis);
                //Создаем период в этой организации
                String title = dis.readUTF();
                String description = dis.readUTF();
                description = description.equals("null") ? null : description;
                organizations.get(j).getPeriods().add(new Organization.Period(
                        beginDate, endDate, title, description));
            }
        }
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        int year = dis.readInt();
        int month = dis.readInt();
        int day = dis.readInt();
        return LocalDate.of(year, month, day);
    }
}
