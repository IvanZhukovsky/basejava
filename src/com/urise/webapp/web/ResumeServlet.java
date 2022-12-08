package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String size = String.valueOf(storage.size());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
                out.println("<head>");
                    out.println("<title>Заголовок</title>");
                out.println("</head>");
                out.println("<body>");
                    out.println("<h1>База данныx резюме</h1>");
                    out.println("<table border=\"1\" >");
                        out.println("<tr>");
                            out.println("<th>uuid</th>");
                            out.println("<th>full name</th>");
                        out.println("</tr>");
                        for (Resume resume : storage.getAllSorted()) {
                            out.println("<tr>");
                                out.println("<th>" + resume.getUuid() + "</th>");
                                out.println("<th>" + resume.getFullName() + "</th>");
                            out.println("</tr>");
                        }
                    out.println("</table>");
                out.println("</body>");
            out.println("</html>");




        } finally {
            out.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
