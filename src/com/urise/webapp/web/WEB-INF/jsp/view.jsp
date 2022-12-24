<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="java.time.LocalDate" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>

    <p>
        <c:forEach var="sectionEntry" items="${resume.getSections()}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType,
                         com.urise.webapp.model.AbstractSection>"/>
    <c:choose>
        <c:when test="${sectionEntry.getKey().equals(SectionType.OBJECTIVE)
        || sectionEntry.getKey().equals(SectionType.PERSONAL)}">
            <h3><%=sectionEntry.getKey().getTitle()%></h3>
            <p> ${sectionEntry.getValue().getContent()} </p>
        </c:when>

        <c:when test="${sectionEntry.getKey().equals(SectionType.ACHIEVEMENT)
        || sectionEntry.getKey().equals(SectionType.QUALIFICATIONS)}">
            <h3><%=sectionEntry.getKey().getTitle()%></h3>
            <c:forEach var="content" items="${sectionEntry.getValue().getContent()}">
                <li> ${content} </li>
            </c:forEach>
        </c:when>

        <c:when test="${sectionEntry.getKey().equals(SectionType.EXPERIENCE)
        || sectionEntry.getKey().equals(SectionType.EDUCATION)}">
            <h3><%=sectionEntry.getKey().getTitle()%></h3>
            <c:forEach var="organization" items="${sectionEntry.getValue().getOrganizations()}">
                <h4>${organization.getHomePage().getName()}</h4>
                <c:forEach var="period" items="${organization.getPeriods()}">
                    <p> ${period.getBeginDate()} - ${period.isEndAfterNow() ? "по наст.вр" : period.getEndDate()}      ${period.getTitle()}</p>
                    <p> ${period.getDescription()} </p>
                </c:forEach>
            </c:forEach>
        </c:when>

    <c:otherwise>

    </c:otherwise>
    </c:choose>


    </c:forEach>
    </p>


</section>
<jsp:include page="fragments/footer.jsp"/>

</body>
</html>
