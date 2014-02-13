<%@page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>Соревнование успешно загружено</title>
	<%@ include file="./headerInclude.jspf" %>
</head>
<body>
<div id="wrapper">
	<h2>Загрузка соревнования прошла успешно</h2>
	Соревование успешно загружено.
	Вы можете перейти <a href="./competitionsList.jsp">к списку соревнований</a> либо <a href="./competition.jsp?competitionId=<%=request.getParameter("competitionId")%>">на страницу соревнования</a>.
</div>
</body>
</html>