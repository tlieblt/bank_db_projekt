<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
<meta name="author" content="Tobias Liebl">
<meta name="author" content="Sven de Haan">
<meta charset="ISO-8859-1">
<title>Kontouebersicht</title>
</head>
<jsp:include page="header.jsp" />

<body>

	<c:choose>
		<c:when test="${user.hatKeinKonto()}">
<div class="container m-2">
			<form method="POST" action=KontoErstellen>

				<input style="display: none" type="text" id="aenderungsTyp"
					name="aenderungsTyp" value="konto"><br> <br>

				<div class="form-check"></div>
				<h2>Sie scheinen kein Konto angelegt zu haben.</h2>
				<button type="submit" class="btn btn-primary">Neues Konto
					erstellen</button>
			</form>
</div>
		</c:when>
		<c:otherwise>

			<div class="container-fluid">
				<div class="row">
					<div class="col-4">
						<h2>Kontoübersicht zu Ihrem ausgewählten Konto:</h2>
					</div>
					<div class="col-4 mt-2"
						style="position: relative; ">
						<form action='AenderKId' class='form-select' method='POST'>
							<my:kontoAuswahl></my:kontoAuswahl>
							<input class='ml-2 btn btn-primary' type='submit'
								value='Konto wechseln'>

						</form>

					</div>
					<div class="col-4">
		<form method='POST' action='Sortieren'>
				<div class='form-group'>
					<input style='display: none' type='text' id='wasSortieren'
						name='wasSortieren' value='konto'>
					<button type='submit' class='btn btn-outline-success mt-2'>Angezeigte Überweisungen sortieren</button>

				</div>
			</form>
			<div class="row">
<a class="btn btn-outline-primary m-3" href="TransaktionErstellen">30 Zufällige Überweisungen erzeugen</a>			
</div>
		</div>
					</div>
				</div>
			<!-- /div-->



			<my:ueberweisungsliste>
				<div class="container m-4">
					<p>Zu diesem Konto existieren bisher keine Überweisungen.</p>
					<a class="btn btn-success m-2" href="TransaktionErstellen">30
						Zufällige Überweisungen erzeugen</a> <a class="btn btn-success m-2"
						href="eineueberweisung.jsp">Eine eigene Überweisung hinzufügen</a>
				</div>

			</my:ueberweisungsliste>



			<div class="container">
				<div class="row">
					<div class="col-6">
						<form method="POST" action="LadeUeberweisungen" id="auszugsMenge"
							name="auszugsMenge">
							<input style="display: none" type="text" id="auszugsMenge"
								name="auszugsMenge" value="15"><br>
							<button type="submit" class="mb-5 ml-1 btn btn-primary">Lädt
								die 15 nächsten Überweisungen</button>
						</form>
					</div>
					<div class="col-4">

						<form method="POST" action="LadeUeberweisungen" id="auszugsMenge"
							name="auszugsMenge">
							<input style="display: none" type="text" id="auszugsMenge"
								name="auszugsMenge" value="alle"><br>

							<button type="submit" class="mb-5 ml-1  btn btn-primary">Lädt
								alle weiteren Überweisungen</button>
						</form>
					</div>

				</div>
			</div>

			

		</c:otherwise>
	</c:choose>
</body>
</html>