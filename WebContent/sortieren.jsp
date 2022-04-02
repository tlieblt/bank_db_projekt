<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<jsp:include page="header.jsp"></jsp:include>
<div class="container m-2">
	<h4>Hier können sie ihre Überweisungen sortieren - entweder die
		letzten Suchergebnisse oder die Überweisungen die zu ihrem aktuellen
		Konto geladen sind.</h4>
	<div class="row">
		<div class="col">

			<form method='POST' action='Sortieren'>
				<div class='form-group'>
					<input style='display: none' type='text' id='wasSortieren'
						name='wasSortieren' value='suche'>


					<button type='submit' class='btn btn-outline-success mt-2'>Suchergebnisse sortieren</button>

				</div>
			</form>
		</div>
		<div class="col">

			<form method='POST' action='Sortieren'>
				<div class='form-group'>
					<input style='display: none' type='text' id='wasSortieren'
						name='wasSortieren' value='konto'>


					<button type='submit' class='btn btn-outline-success mt-2'>Kontoüberweisungen sortieren</button>

				</div>
			</form>
		</div>

	</div>
</div>

<div class="container-lg m-4 p-2"
	style="border: 4px solid MediumSeaGreen; border-radius: 15px;">

	<table class='table table-striped table-bordered'
		style="table-layout: fixed;">
		<thead>
			<tr>
				<th scope='col'>Name</th>
				<th scope='col'>IBAN</th>
				<th scope='col'>Beschreibung</th>
				<th scope='col'>Betrag</th>
				<th scope='col'>Zeit</th>
				<th scope='col'>Kategorie</th>
			</tr>
			<tr>
				<th scope='col'>


					<form method='POST' action='Sortieren'>
						<div class='form-group'>
							<input style='display: none' type='text' id='wasSortieren'
								name='wasSortieren' value='${ wasSortieren }'>
							<input style='display: none' type='text' id='sortierenNach'
								name='sortierenNach' value='name'> <input
								style='display: none' type='text' id='reihenfolge'
								name='reihenfolge' value="${ nameReihenfolge }">

							<button type='submit'
								class='btn btn-outline-dark ${nameHervorheben} mt-2'>Sortieren</button>

						</div>
					</form>
				</th>
				<th scope='col'>

					<form method='POST' action='Sortieren'>
						<div class='form-group'>
							<input style='display: none' type='text' id='wasSortieren'
								name='wasSortieren' value='${ wasSortieren }'>
							<input style='display: none' type='text' id='sortierenNach'
								name='sortierenNach' value='iban'> <input
								style='display: none' type='text' id='reihenfolge'
								name='reihenfolge' value="${ ibanReihenfolge }">

							<button type='submit'
								class='btn btn-outline-dark  ${ibanHervorheben} mt-2'>Sortieren</button>

						</div>
					</form>

				</th>
				<th scope='col'>

					<form method='POST' action='Sortieren'>
						<div class='form-group'>
							<input style='display: none' type='text' id='wasSortieren'
								name='wasSortieren' value='${ wasSortieren }'>
							<input style='display: none' type='text' id='sortierenNach'
								name='sortierenNach' value='betreff'> <input
								style='display: none' type='text' id='reihenfolge'
								name='reihenfolge' value="${ betreffReihenfolge }">

							<button type='submit'
								class='btn btn-outline-dark ${betreffHervorheben} mt-2'>Sortieren</button>

						</div>
					</form>
				</th>
				<th scope='col'>

					<form method='POST' action='Sortieren'>
						<div class='form-group'>
							<input style='display: none' type='text' id='wasSortieren'
								name='wasSortieren' value='${ wasSortieren }'>
							<input style='display: none' type='text' id='sortierenNach'
								name='sortierenNach' value='betrag'> 
								<input style='display: none' type='text' id='reihenfolge'
								name='reihenfolge' value="${ betragReihenfolge }">

							<button type='submit'
								class='btn btn-outline-dark ${betragHervorheben} mt-2'>Sortieren</button>

						</div>
					</form>
				</th>
				<th scope='col'>

					<form method='POST' action='Sortieren'>
						<div class='form-group'>
							<input style='display: none' type='text' id='wasSortieren'
								name='wasSortieren' value='${ wasSortieren }'>
							<input style='display: none' type='text' id='sortierenNach'
								name='sortierenNach' value='datum'> <input
								style='display: none' type='text' id='reihenfolge'
								name='reihenfolge' value="${ datumReihenfolge }">

							<button type='submit'
								class='btn btn-outline-dark ${datumHervorheben} mt-2'>Sortieren</button>

						</div>
					</form>
				</th>
			</tr>
		</thead>
		<tbody>


			<c:forEach items="${sortierteUeberweisungen}" var="eineUeberweisung">

				<tr>
					<td>${ eineUeberweisung.getVersender() }</td>
					
					<td>${ eineUeberweisung.getVersender_iban() }</td>

					<td>${ eineUeberweisung.getBetreff() }</td>

					<td>${ eineUeberweisung.getEuroBetrag() }</td>

					<td>${ eineUeberweisung.getTimestamp().toString() }</td>

					<td>${ eineUeberweisung.getKategorien().toString() }</td>

				</tr>


			</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>