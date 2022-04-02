<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="header.jsp" />

<body>
<c:choose>
<c:when test="${user.hatKeinKonto()}">

<form method="POST" action=KontoErstellen>

					<input style="display: none" type="text" id="aenderungsTyp"
						name="aenderungsTyp" value="konto"><br> <br>

					<div class="form-check"></div>
					<h2>Sie scheinen kein Konto angelegt zu haben.</h2>
					<button type="submit" class="btn btn-primary">Neues Konto
						erstellen</button>
				</form>

</c:when>
<c:otherwise> 

   
	<div class="card m-4">
		<div class="card-body">
		<div style ="position:relative; left:66%; max-width:30%;">
<form action='AenderKId' class='form-select' method='POST'>
    	<input style="display: none" type="text" id="ursprungsseite"
					name="ursprungsseite" value="suche.jsp">
   <my:kontoAuswahl></my:kontoAuswahl>
   	<input class='ml-2 btn btn-primary' type='submit' value='Konto wechseln'>
				
   
   </form>
   </div>
		
			<h5 class="card-title">Durchsuchen sie Ihre Kontoauszüge:</h5>
			<small class="form-text text-muted">Bei der Suche kann eine
				Grenze von Betrag oder ein Datum ausgelassen werden. Es wird dann
				implizit angenommen, dass das Suchintervall in Richtung "unendlich"
				geöffnet wird. Ist bspw. nur das Bis-Datum gesetzt, werden alle
				Überweisungen vor dem Bis-Datum gefunden (oder durch andere
				Suchbegriffe gefiltert). TLDR: Leere Suche liefert alle
				Überweisungen.</small>


			<form method="POST" action="Suche">
				<div class="row">
					<div class="form-group m-2 col-2 col-sm">
						<label for="betreff">Verwendungszweck durchsuchen</label> <input
							type="text" class="form-control" id="betreff"
							placeholder="nach Verwendungszweck suchen" name="betreff">
						<small class="form-text text-muted">Bitte auf Groß- bzw.
							Kleinschreibung achten!</small>
					</div>
					<div class="form-group m-2 col-2 col-sm">
						<label for="empfaenger">Empfängername</label> <input type="text"
							class="form-control" id="empfaenger"
							placeholder="nach Empfänger suchen" name="empfaenger"> <small
							class="form-text text-muted">Bitte auf Groß- bzw.
							Kleinschreibung achten!</small>
					</div>

				</div>
				<table class="table">
					<thead>
						<tr>
							<td scope="col"><label for="start">Von Datum:</label> </td>

							<td scope="col"><label for="ende"> bis:</label></td>

						</tr>
					</thead>

					<tbody>
						<tr>
							<td><input class="form-control-sm" type="date" id="start"
								name="start"><small
								class="form-text text-muted">Beim Auslassen werden alle
									älteren Überweisungen gefunden! </small></td>
							<td><input class="form-control-sm" type="date" id="ende"
								name="ende"> <small
								class="form-text text-muted">Beim Auslassen werden alle
									neueren Überweisungen gefunden! </small></td>
						</tr>
					</tbody>
				</table>

				<table class="table">
					<thead>
						<tr>
							<td scope="col"><label for="niedrigerBetrag">Überweisungshöhe
									von:</label></td>

							<td scope="col"><label for="hoherBetrag"> bis:</label> </td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input class="form-control-sm" type="number" step="any"
								id="niedrigerBetrag" name="niedrigerBetrag"> &euro;  <small class="form-text text-muted">Bitte geben sie den Betrag in Cent an! Beim Auslassen
									werden alle kleineren Beträge gefunden! </small></td>
							<td><input class="form-control-sm" type="number" step="any"
								id="hoherBetrag" name="hoherBetrag">&euro; <small
								class="form-text text-muted">Bitte geben sie den Betrag in Cent an! Beim Auslassen werden alle
									höheren Beträge gefunden! </small></td>
						</tr>
					</tbody>
				</table>



				<div class="form-check"></div>
				<button type="submit" class="btn btn-primary">Suchen!</button>

			</form>
		</div>
		   </div>
		
	
	</c:otherwise>
	
	</c:choose>
</body>
</html>