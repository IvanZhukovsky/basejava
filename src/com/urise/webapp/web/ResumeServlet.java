package com.urise.webapp.web;

import com.sun.xml.internal.ws.util.StringUtils;
import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntPredicate;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        String sectionType = request.getParameter("sectionType");
        String organization = request.getParameter("organization");


        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                if (uuid.equals("new")) {
                    r = new Resume();
                    //storage.save(r);
                } else {
                    r = storage.get(uuid);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        Resume r;
        try {
            //r = storage.get(uuid);
            storage.delete(uuid);
            r = new Resume(uuid);
            storage.save(r);
        } catch (Exception e) {
            r = new Resume();
            storage.save(r);
        }

        r.setFullName(fullName);

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String content = request.getParameter(type.name());

            if (content != null && content.trim().length() != 0) {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(type, new TextSection(content));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> contentList = new ArrayList<>();
                        Scanner scanner = new Scanner(content);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line != null && line.trim().length() != 0) {
                                contentList.add(line);
                            }
                        }
                        r.addSection(type, new ListSection(contentList));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:

                        List<Organization> organizations = new ArrayList<>();
                        for (int i = 0; i < request.getParameterValues(type.name()).length; i++) {

                            String name = request.getParameterValues(type.name())[i];
                            String url = request.getParameterValues(type.name() + " " + "url")[i];

                            if (url.trim().length() == 0) {
                                url = null;
                            }

                            List<Organization.Period> periods = new ArrayList<>();

                            if (request.getParameterValues(type.name() + " " + (i + 1) + " " + "pozition") != null && name.trim().length() != 0) {


                                for (int j = 0; j < request.getParameterValues(type.name() + " " + (i + 1) + " " + "pozition").length; j++) {


                                    String period = request.getParameterValues(type.name() + " " + (i + 1) + " " + "pozition")[j];

                                    if (period.trim().length() != 0) {

                                        String begin = request.getParameter(type.name() + " "
                                                + (i + 1) + " " + (j + 1) + " " + "beginDate");

                                        LocalDate beginDate = parseDate(begin);

                                        String end = request.getParameter(type.name() + " "
                                                + (i + 1) + " " + (j + 1) + " " + "endDate");

                                        LocalDate endDate = parseDate(end);

                                        String description = request.getParameter(type.name() + " "
                                                + (i + 1) + " " + (j + 1) + " " + "description");
                                        periods.add(new Organization.Period(beginDate, endDate, period, description));
                                    }
                                }

                                Organization theOrg = new Organization(new Link(name, url), periods);
                                organizations.add(theOrg);
                                r.addSection(type, new OrganizationSection(organizations));

                            }

                        }
//                        r.getSections().remove(type);
//                        r.addSection(type, new OrganizationSection(organizations));
                }
            }
        }

        storage.update(r);
        response.sendRedirect("resume");

    }

    private LocalDate parseDate(String date) {

        LocalDate localDate;
        if (date.length() == 7) {
            String monthStr = date.substring(0, 2);
            String yearStr = date.substring(3, 7);
            if (monthStr.chars().allMatch(Character::isDigit)
                    && yearStr.chars().allMatch(Character::isDigit)) {
                int month = Integer.parseInt(monthStr);
                int year = Integer.parseInt(yearStr);
                localDate = LocalDate.of(year, month, 1);
            } else {
                localDate = DateUtil.DEFAULT;
            }
        } else {
            localDate = DateUtil.DEFAULT;
        }
        return localDate;
    }
}

