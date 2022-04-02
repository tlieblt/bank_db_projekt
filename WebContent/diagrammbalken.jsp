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
	  // erstelle Balkendiagramm mit Variablen die von Servlet kommen
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawVisualization);

      function drawVisualization() {
         
          var data = google.visualization.arrayToDataTable([
            ['Zeitraum', 'Betrag von ${auswahl1}', 'Betrag von ${auswahl2}', 'Betrag von ${auswahl3}', 'Betrag von ${auswahl4}', 'Betrag von ${auswahl5}'],
            ['${anfangszeit} bis ' + '${endzeit}',  ${betragauswahl1},     ${betragauswahl2},         ${betragauswahl3},             ${betragauswahl4},           ${betragauswahl5}]
          ]);

          var options = {
            title : 'Betrag je Kategorie in bestimmten Zeitraum',
            vAxis: {title: 'Betrag'},
            hAxis: {title: 'Zeitraum'},
            seriesType: 'bars',
            series: {5: {type: 'line'}}
          };

          var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
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
			<h5 class="card-title">Hier können Sie ein Balkendiagramm
				erstellen, welches Ihre Kategorien zählt, den kompletten Betrag der
				Kategorie berechnet und dann visuell darstellt. <br/>Wählen Sie dafür mindestens eine Kategorie aus!</h5>
			<small class="form-text text-muted">Beachten Sie: Es werden
				alle Überweisungen von Ihrem aktuell ausgewählten Konto ${ user.getCurIban() }
				visuell dargestellt. Wollen Sie die Kontoeinträge eines anderen Kontos kategorisiert haben,
				 so ändern Sie Ihr Konto in der <a
				href="kontouebersicht.jsp">Kontoübersicht</a>.
			</small>

			<form method="POST" action="DiagrammBalken" name="auswahl">
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




				<button type="submit" class="btn btn-primary" value="balken"
					name="balken">Diagramm erstellen</button>
			</form>

			<div id="chart_div" style="width: 900px; height: 500px;"></div>
		</div>
	</div>
</body>
</html>