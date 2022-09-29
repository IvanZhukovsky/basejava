package com.urise.webapp.model;

import java.util.ArrayList;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");
        resume.contacts.setContact(ContactType.TELEFON, "+7(921) 855-04-82");
        resume.contacts.setContact(ContactType.SKYPE, "skype:grigory.kislin");
        resume.contacts.setContact(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.contacts.setContact(ContactType.LINKEDIN, "ссылка");
        resume.contacts.setContact(ContactType.GITHUB, "ссылка");
        resume.contacts.setContact(ContactType.STACKOVERFLOW, "ссылка");
        resume.contacts.setContact(ContactType.HOMEPAGE, "ссылка");

        resume.sectionMap.get(SectionType.OBJECTIVE).setContent("Ведущий стажировок и корпоративного обучения " +
                "по Java Web и Enterprise технологиям");
        resume.sectionMap.get(SectionType.PERSONAL).setContent("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры");

        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("Организация команды и успешная реализация " +
                "Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, " +
                "система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, " +
                "многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("С 2013 года: разработка проектов \"Разработка " +
                "Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). " +
                "Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок " +
                "и ведение проектов. Более 3500 выпускников.");
        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("Реализация двухфакторной аутентификации для " +
                "онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, " +
                "Jira, Zendesk.");
        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("Налаживание процесса разработки и непрерывной " +
                "интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения " +
                "управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации " +
                "различных ERP модулей, интеграция CIFS/SMB java сервера.");
        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("Реализация c нуля Rich Internet Application " +
                "приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, " +
                "Highstock для алгоритмического трейдинга.");
        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("Создание JavaEE фреймворка для отказоустойчивого " +
                "взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). " +
                "Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация " +
                "онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        resume.sectionMap.get(SectionType.ACHIEVEMENT).setContent("Реализация протоколов по приему платежей всех " +
                "основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и " +
                "Никарагуа.");

        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, " +
                "Tomcat, Jetty, WebLogic, WSO2");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Version control: Subversion, Git, Mercury, " +
                "ClearCase, Perforce");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("DB: PostgreSQL(наследование, pgplsql, PL/Python), " +
                "Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("VLanguages: Java, Scala, Python/Jython/PL-Python, " +
                "JavaScript, Groovy");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Java Frameworks: Java 8 (Time API, Streams), " +
                "Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), " +
                "Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium " +
                "(htmlelements).");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Python: Django.");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("JavaScript: jQuery, ExtJS, Bootstrap.js, " +
                "underscore.js");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, " +
                        "RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, " +
                        "Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Инструменты: Maven + plugin development, Gradle, " +
                "настройка Ngnix");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("администрирование Hudson/Jenkins, Ant + " +
                "custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Отличное знание и опыт применения концепций ООП, " +
                "SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        resume.sectionMap.get(SectionType.QUALIFICATIONS).setContent("Родной русский, английский \"upper intermediate\"");

        resume.sectionMap.get(SectionType.EXPERIENCE).setContent("Java Online Projects\n" +
                "10/2013 - Сейчас\n" +
                "Автор проекта.\n" +
                "Создание, организация и проведение Java онлайн проектов и стажировок.");
        resume.sectionMap.get(SectionType.EDUCATION).setContent("Coursera\n" +
                "03/2013 - 05/2013\n" +
                "'Functional Programming Principles in Scala' by Martin Odersky");

        showResume(resume);
    }

    private static void showResume(Resume resume) {
        //Отображение fullName
        System.out.println(resume.getFullName());
        //Отображение перечня контактов
        for (ContactType contactType : ContactType.values()) {
            if (contactType.equals(ContactType.TELEFON)
                    || contactType.equals(ContactType.EMAIL)
                    || contactType.equals(ContactType.SKYPE)) {
                System.out.print(contactType.getTitle() + " ");
                System.out.println(resume.contacts.getContact(contactType));
            } else {
                System.out.println(contactType.getTitle());
            }
        }
        //отображение секций
        for (SectionType sectionType : SectionType.values()) {
            System.out.println(sectionType.getTitle());
            printContent(resume.sectionMap.get(sectionType).getContent());
        }

    }

    private static void printContent(Object content) {
        if (content instanceof String) {
            System.out.println((String) content);
        } else {
            for (String paragraph : (ArrayList<String>) content) {
                System.out.println(paragraph);
            }
        }
    }

}
