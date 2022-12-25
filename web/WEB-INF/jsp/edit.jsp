<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
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
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
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
                    <c:forEach var="organization" items="${resume.getSections().get(sectionType).getOrganizations()}">
                        <h4>${organization.getHomePage().getName()}</h4>
                        <c:forEach var="period" items="${organization.getPeriods()}">
                            <p> ${period.getBeginDate()} - ${period.getEndDate()}      ${period.getTitle()}</p>
                            <p> ${period.getDescription()} </p>
                        </c:forEach>
                    </c:forEach>
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