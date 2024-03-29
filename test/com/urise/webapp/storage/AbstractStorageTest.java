package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.util.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.get().getStorageDir();
    //protected static final File STORAGE_DIR = new File("storage");
    protected final Storage storage;

    private final static String UUID_1 = UUID.randomUUID().toString();
    private final static String UUID_2 = UUID.randomUUID().toString();
    private final static String UUID_3 = UUID.randomUUID().toString();
    private final static String UUID_4 = UUID.randomUUID().toString();

    protected static final Resume R1;
    protected static final Resume R2;
    protected static final Resume R3;
    protected static final Resume R4;

    protected static final String UUID_NOT_EXIST = "uuid5";
    protected static final int STORAGE_LIMIT = 10000;

    static {
        R1 = ResumeTestData.createResume(UUID_1, "Григорий Кислин");
        R2 = ResumeTestData.createResume(UUID_2, "Name2");
        R3 = ResumeTestData.createResume(UUID_3, "Name3");
        R4 = ResumeTestData.createResume(UUID_4, "Name4");

        R1.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        R1.addContact(ContactType.TELEFON, "+7(921) 855-0482");

        R2.addContact(ContactType.EMAIL, "mail1@ya.ru");
        R2.addContact(ContactType.TELEFON, "11111");



        R4.addContact(ContactType.EMAIL, "mail1@ya.ru");
        R4.addContact(ContactType.TELEFON, "11111");
        R1.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по " +
                "Java Web и Enterprise технологиям"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, " +
                "креативность, инициативность. Пурист кода и архитектуры."));
        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("Организация команды и успешная реализация " +
                "Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, " +
                "система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, " +
                "многомодульный Spring Boot + Vaadin проект для комплексных DIY смет",
                "С 2013 года: разработка проектов Разработка Web приложения,\"Java Enterprise\", \"Многомодульный " +
                        "maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие " +
                        "(JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с " +
                        "Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk."));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
        R1.addSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("Organization11", "https://yandex.ru",
                        new Organization.Period(2005, Month.JANUARY, "Автор проекта.", "Создание" +
                                ", организация и проведение Java онлайн проектов и стажировок."),
                        new Organization.Period(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))
        ));
        R1.addSection(SectionType.EDUCATION, new OrganizationSection(
                new Organization("Institute", null,
                        new Organization.Period(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                        new Organization.Period(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet"))
        ));
//
        //R2.addContact(ContactType.SKYPE, "skype2");
        //R2.addContact(ContactType.TELEFON, "22222");
//        R2.addSection(SectionType.EXPERIENCE,
//                new OrganizationSection(
//                        new Organization("Organization2", "http://Organization2.ru",
//                                new Organization.Period(2015, Month.JANUARY, "position1", "content1"))));
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void clear() {
        //Проверяем произошло ли обнуление счетчика заполненых элементов
        storage.clear();
        assertSize(0);
        //Assert.assertFalse(!storage.getAllSorted().isEmpty());
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1, "New Name");
        newResume.addContact(ContactType.EMAIL, "mail1@ya.ru");
        newResume.addContact(ContactType.TELEFON, "11111");
        storage.update(newResume);
        Assert.assertTrue(newResume.equals(storage.get(UUID_1)));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume());
    }

    @Test
    public void save() {
        storage.save(R4);
        assertGet(R4);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(R1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        //Проверяем произошло ли уменьшение счетчика при удалении резюме
        storage.delete(UUID_1);
        assertSize(2);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test
    public void getAllSorted() {
//        storage.clear();
//        storage.save(R4);
//        storage.save(R1);
//        storage.save(R2);
//        storage.save(R3);
//
//        List<Resume> list = storage.getAllSorted();
//        Assert.assertSame(R1, list.get(0));
//        Assert.assertSame(R2, list.get(1));
//        Assert.assertSame(R3, list.get(2));
//        Assert.assertSame(R4, list.get(3));

        List<Resume> list = storage.getAllSorted();
        assertEquals(3, list.size());
        assertEquals(list, Arrays.asList(R1, R2, R3));
        //assertEquals(list.get(0), R1);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void get() {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

//    @Test
//    public void fillAfterDelete() {
//        storage.delete("uuid1");
//        Assert.assertEquals("uuid2", storage.getAllSorted().get(0).getUuid());
//    }

    @Test
    public void jsonTest() {
        OrganizationSection organizationSection = (OrganizationSection) R1.getSections().get(SectionType.EXPERIENCE);
        String orgJson = JsonParser.write(organizationSection, OrganizationSection.class);
        OrganizationSection organizationSection1 = JsonParser.read(orgJson, OrganizationSection.class);
        Assert.assertEquals(organizationSection, organizationSection1);
    }
    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}