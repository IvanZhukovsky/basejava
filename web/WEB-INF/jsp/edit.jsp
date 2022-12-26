<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.Organization" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}" required></dd>
        </dl>
        <h3>Контакты:</h3>

        <c:forEach var="type" items="<%=ContactType.values() %>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>

        <c:forEach var="sectionType" items="${SectionType.values()}">

            <jsp:useBean id="sectionType"
                         type="com.urise.webapp.model.SectionType"/>
            <h3><%=sectionType.getTitle()%></h3>
            <c:choose>
                <c:when test=""></c:when>
                <c:when test="${sectionType.equals(SectionType.EXPERIENCE)
        || sectionType.equals(SectionType.EDUCATION)}">

                    <c:forEach var="organization" items="${
                    resume.getSections().get(sectionType).getOrganizations() != null ?
                    resume.getSections().get(sectionType).getOrganizations() : resume.getDefaultListOrg()}">

                        <c:set var="id" scope="session" value="${id = id + 1}" />

                        <input type="text" name="${sectionType}" size="30" value="${organization.getHomePage().getName()}"
                               placeholder="Название организации">
                        <input type="text" name="${sectionType} url" size="30" value="${organization.getHomePage().getUrl()}"
                               placeholder="Сайт организации">
                        <c:forEach var="period" items="${organization.getPeriods()}">

                            <c:set var="id2" scope="session" value="${id2 = id2 + 1}" />

                            <p>
                                <input type="text" name="${sectionType} ${id} pozition" size="60" value="${period.getTitle()}"
                                      placeholder="Позиция">
                            </p>
                            <p>
                                <input type="text" name="${sectionType} ${id} ${id2} beginDate" size="30" value="${period.getFormatBeginDate()}"
                                       placeholder="Начало, ММ/ГГГГ">
                                <input type="text" name="${sectionType} ${id} ${id2} endDate" size="30" value="${period.getFormatEndDate()}"
                                       placeholder="Конец, ММ\ГГГГ">
                            </p>
                            <p>
                                <input type="text" name="${sectionType} ${id} ${id2} description" size="60" value="${period.getDescription()}"
                                       placeholder="Описание">
                            </p>

                        </c:forEach>
                        <c:set var="id2" scope="session" value="${id2 = 0}" />
                    </c:forEach>
                    <c:set var="id" scope="session" value="${id = 0}" />
                </c:when>

                <c:otherwise>
                    <textarea type="text" name="${sectionType}" cols="50" rows="3">${resume.getSections().get(sectionType)}</textarea><br/>
                </c:otherwise>

            </c:choose>

        </c:forEach>

        <hr>

        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>

</section>
<jsp:include page="fragments/footer.jsp"/>

</body>
</html>