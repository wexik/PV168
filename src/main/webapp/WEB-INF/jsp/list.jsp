<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List of person</title>
</head>
<body>
    <h1>Create person</h1>
    <c:if test="${not empty error}">
        <span style="color: red;">${error}</span>
    </c:if>
    <c:choose>
        <c:when test="${editPerson == null}">
            <form method="POST" action="${pageContext.request.contextPath}/add">
                Name: <input type="text" name="name" /><br />
                Address: <input type="text" name="address" /><br />
                Phone: <input type="text" name="phoneNumber" /><br />
                <input type="submit" value="Add">
            </form>
        </c:when>
        <c:otherwise>
            <form method="POST" action="${pageContext.request.contextPath}/edit">
                <input type="hidden" name="id" value="${editPerson.id}" /><br />
                Name: <input type="text" name="name" value="${editPerson.name}" /><br />
                Address: <input type="text" name="address" value="${editPerson.address}"  /><br />
                Phone: <input type="text" name="phoneNumber" value="${editPerson.phoneNumber}" /><br />
                <input type="submit" value="Edit">
            </form>
        </c:otherwise>
    </c:choose>

    <h1>List of person</h1>
    <ul>
        <c:forEach items="${people}" var="person">
            <li>
                <form method="POST" action="${pageContext.request.contextPath}/delete">
                    <input type="hidden" value="${person.id}" name="id" />
                    ${person.name} | ${person.address} | ${person.phoneNumber}
                    | <input type="submit" value="Delete"><%--POST--%>
                    | <a href="${pageContext.request.contextPath}/edit?id=${person.id}">Edit</a><%--GET--%>
                </form>
            </li>
        </c:forEach>
    </ul>
</body>
</html>
