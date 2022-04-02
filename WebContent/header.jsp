<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="author" content="Sven de Haan">
<meta name="author" content="Tobias Liebl">
<meta name="description" content="Kontochecker">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" type="image/png" href="pics/favicon.png">
<!-- Falls die Seite von mobilen GerÃ¤ten aufgerufen wird, braucht man anscheinend ein PNG Image -->
<link rel="shortcut icon" type="image/x-icon" href="pics/favicon.ico">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
	integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous"></script>

<!--  link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/style.css" -->

<title>Kontochecker</title>
</head>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark"
	style="padding-top: 1%;">
	<h2 class="navbar-brand">Kontochecker</h2>
	<a class="nav-link active" href="index.jsp">Home</a>
	<!--  Für eingeloggte Benutzer werden Funktionen angezeigt -->
	<my:erlaubteFunktionen>
		<jsp:include page='nutzerheader.jsp' />
	</my:erlaubteFunktionen>
</nav>
<!--  Notifcation Tag -->

<my:message></my:message>