<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta name="author" content="Sven de Haan">
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {

        var data = google.visualization.arrayToDataTable([
          ['Kategorie', 'Vorkommen in Konto'],
          ['${auswahl1}',     ${summeauswahl1}],
          ['${auswahl2}',      ${summeauswahl2}],
          ['${auswahl3}',  ${summeauswahl3}],
          ['${auswahl4}',  ${summeauswahl4}],
          ['${auswahl5}',  ${summeauswahl5}],
          ['nicht kategorisiert',  ${summenichtkategorisiert}]
        ]);

        var options = {
          title: 'Meine Kategorien',
        	  is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
      }
     </script>
<meta charset="ISO-8859-1">
<title>Diagramm JSP</title>
</head>
<body>
	<jsp:include page="header.jsp" />


	<div class="card m-4">
		<div class="card-body">
			<h5 class="card-title">Hier können Sie ein Kreisdiagramm
				erstellen, welches das Aufkommen Ihrer Kategorien zählt und visuell
				darstellt. <br/>Wählen Sie dafür mindestens eine Kategorie aus!</h5>
			<small class="form-text text-muted">Beachten Sie: Es werden
				alle Überweisungen von Ihrem aktuell ausgewählten Konto ${ user.getCurIban() }
				visuell dargestellt. Wollen Sie die Kontoeinträge eines anderen Kontos kategorisiert haben,
				 so ändern Sie Ihr Konto in der <a
				href="kontouebersicht.jsp">Kontoübersicht</a>.
			</small>

			<form method="POST" action="DiagrammKreis" name="auswahl">
				<div class="row">
					<div class="form-group p-2 col-3 col-sm">
						<label for="Auswahl 1">Auswahl 1: </label> <select id="auswahl1"
							name="auswahl1">
							<!-- Erstellt fuer jede Kategorie ein Optionsfeld im Selectmenue -->
							<c:forEach items="${kategorien}" var="auswahlName"
								varStatus="loop">
								<option value="${auswahlName}">${auswahlName}</option>
							</c:forEach>
						</select>

					</div>
					<div class="form-group p-2 col-3 col-sm">
						<label for="Auswahl 2">Auswahl 2: </label> <select id="auswahl2"
							name="auswahl2">
							<c:forEach items="${kategorien}" var="auswahlName"
								varStatus="loop">
								<option value="${auswahlName}">${auswahlName}</option>
							</c:forEach>
						</select>

					</div>
					<div class="form-group p-2 col-3 col-sm">
						<label for="Auswahl 3">Auswahl 3: </label> <select id="auswahl3"
							name="auswahl3">
							<c:forEach items="${kategorien}" var="auswahlName"
								varStatus="loop">
								<option value="${auswahlName}">${auswahlName}</option>
							</c:forEach>
						</select>

					</div>
					<div class="form-group p-2 col-3 col-sm">
						<label for="Auswahl 4">Auswahl 4: </label> <select id="auswahl4"
							name="auswahl4">
							<c:forEach items="${kategorien}" var="auswahlName"
								varStatus="loop">
								<option value="${auswahlName}">${auswahlName}</option>
							</c:forEach>
						</select>

					</div>
					<div class="form-group p-2 col-3 col-sm">
						<label for="Auswahl 5">Auswahl 5: </label> <select id="auswahl5"
							name="auswahl5">
							<c:forEach items="${kategorien}" var="auswahlName"
								varStatus="loop">
								<option value="${auswahlName}">${auswahlName}</option>
							</c:forEach>
						</select>

					</div>
				</div>

				<div class="row">
					<div class="form-group p-2 col-3 col-sm">
						<label for="start">Von Datum:</label> <input
							class="form-control-sm" type="date" id="start" name="start"><small
							class="form-text text-muted">Beim Auslassen werden alle
							älteren Überweisungen gefunden! </small>
					</div>
					<div class="form-group p-2 col-3 col-sm">
						<label for="ende"> bis:</label> <input class="form-control-sm"
							type="date" id="ende" name="ende"> <small
							class="form-text text-muted">Beim Auslassen werden alle
							neueren Überweisungen gefunden! </small>
					</div>
				</div>





				<button type="submit" class="btn btn-primary" value="kreis"
					name="kreis">Diagramm erstellen</button>
			</form>

			<div id="piechart" style="width: 900px; height: 500px;"></div>
		</div>
	</div>
</body>
</html>