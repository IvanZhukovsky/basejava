package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Map<ContactType, String> contacts = resume.getContacts();

            writeWithExeption(dos, contacts.entrySet(), element -> {
                dos.writeUTF(element.getKey().name());
                dos.writeUTF(element.getValue());
            });

            Map<SectionType, AbstractSection> sections = resume.getSections();


            writeWithExeption(dos, sections.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((TextSection) entry.getValue()).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeWithExeption(dos, ((ListSection) entry.getValue()).getContent(), element -> dos.writeUTF(element));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        DataStreamSerializer.this.writeOrganizationSection(dos, entry);
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            readWithExeption1(dis, (CustomConsumerRead1<String>) list -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readWithExeption1(dis, (CustomConsumerRead1<String>) list -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        DataStreamSerializer.this.readTextSection(resume, sectionType, dis);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        DataStreamSerializer.this.readListSection(resume, sectionType, dis);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        DataStreamSerializer.this.readOrganizationSection(resume, sectionType, dis);
                        break;
                }
            });
            return resume;
        }
    }

    @FunctionalInterface
    interface CustomConsumer<T> {
        void doWrite(T element) throws IOException;
    }

    private static <T> void writeWithExeption(DataOutputStream dos, Collection<T> collection, CustomConsumer<T> customConsumer) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            customConsumer.doWrite(element);
        }
    }

    private void writeOrganizationSection(DataOutputStream dos, Map.Entry<SectionType, AbstractSection> entry) throws IOException {
        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();

        writeWithExeption(dos, organizationSection.getOrganizations(), element -> {
            dos.writeUTF(element.getHomePage().getName());
            dos.writeUTF(element.getHomePage().getUrl() != null ? element.getHomePage().getUrl() : "null");
            writeWithExeption(dos, element.getPeriods(), element1 -> {
                DataStreamSerializer.this.writeDate(dos, element1.getBeginDate());
                DataStreamSerializer.this.writeDate(dos, element1.getEndDate());
                dos.writeUTF(element1.getTitle());
                dos.writeUTF(element1.getDescription() != null ? element1.getDescription() : "null");
            });
        });
    }

    private void writeDate(DataOutputStream dos, LocalDate localDate) throws IOException {
        dos.writeInt(localDate.getYear());
        dos.writeInt(localDate.getMonthValue());
        dos.writeInt(localDate.getDayOfMonth());
    }

    @FunctionalInterface
    interface CustomConsumerRead1<W> {
        void doRead(List<W> list) throws IOException;
    }

    private static <W> List<W> readWithExeption1(DataInputStream dis, CustomConsumerRead1<W> customConsumerRead1) throws IOException {
        List<W> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            customConsumerRead1.doRead(list);
        }
        return list;
    }

    private void readTextSection(Resume resume, SectionType sectionType, DataInputStream dis) throws IOException {
        resume.addSection(sectionType, new TextSection(dis.readUTF()));
    }

    private void readListSection(Resume resume, SectionType sectionType, DataInputStream dis) throws IOException {
        resume.addSection(sectionType, new ListSection(readWithExeption1(dis, list -> list.add(dis.readUTF()))));
    }

    private void readOrganizationSection(Resume resume, SectionType sectionType, DataInputStream dis) throws IOException {
        OrganizationSection organizationSection = new OrganizationSection(readWithExeption1(dis, organizations -> {
            String linkName = dis.readUTF();
            String linkUrl = dis.readUTF();
            linkUrl = linkUrl.equals("null") ? null : linkUrl;
            organizations.add(new Organization(new Link(linkName, linkUrl), new ArrayList<>()));

            //Считываем количество периодов в данной организации
            organizations.get(organizations.size() - 1).setPeriods(readWithExeption1(dis, list -> {
                //Считываем дату начала
                LocalDate beginDate = DataStreamSerializer.this.readDate(dis);
                //Считываем дату конца
                LocalDate endDate = DataStreamSerializer.this.readDate(dis);
                //Создаем период в этой организации
                String title = dis.readUTF();
                String description = dis.readUTF();
                description = description.equals("null") ? null : description;
                list.add(new Organization.Period(
                        beginDate, endDate, title, description));
            }));
        }));
        resume.addSection(sectionType, organizationSection);
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        int year = dis.readInt();
        int month = dis.readInt();
        int day = dis.readInt();
        return LocalDate.of(year, month, day);
    }
}
