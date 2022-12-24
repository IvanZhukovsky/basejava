<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/style.css">
        <title>Список всех резюме</title>
    </head>
    <body>
        <jsp:include page="fragments/header.jsp"/>
        <section>
            <table border="1" cellpadding="g" cellspacing="0">
                <tr>
                    <th>Имя</th>
                    <th>Email</th>
                    <th></th>
                    <th></th>
                </tr>
            <c:forEach items="${resumes}" var="resume">
                <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
                <tr>
                    <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                    <td><%=ContactType.EMAIL.toHtml(resume.getContact(ContactType.EMAIL))%></td>
                    <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a></td>
                    <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></td>
                </tr>
            </c:forEach>
        </table>
        <button type="button" onclick= "window.location.href = 'resume?uuid=${"new"}&action=edit'">Добавить новое резюме</button>

    </section>
    <jsp:include page="fragments/footer.jsp"/>
</body>
</html>
