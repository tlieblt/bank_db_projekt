<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>
<html>
<head>
<meta name="author" content="Tobias Liebl">
</head>
<jsp:include page="header.jsp" />
<body>

<div class="m-3">
	<h5>Kategorieverwaltung</h5>
	<h6 class=' text-muted m-1'>Suche durch Betreff und Empfänger ist
		abhängig von Groß- und Kleinschreibung!</h6>
	<small class="form-text text-muted">Beim Anklicken einer
		Kategorie werden ihre Elemente angezeigt. Die Kategorieelemente können
		nachträglich verändert werden und werden durch Enter/Speichern
		aktualisiert. Ein leer gelassenes Feld beim Speichern wird gelöscht.<br>
		<br>
	</small>
	<div class="row justify-content-start">

		<my:kategorien>


			<p>
				<button class="btn btn-primary m-2" type="button"
					data-toggle="collapse" data-target="#collapseExample"
					aria-expanded="false" aria-controls="collapseExample">
					Erste Kategorie anlegen:</button>
			</p>
			<div class="collapse m-4" id="collapseExample">
				<div class="card card-body">
					<form method="POST" action="KategorienAendern">
						<input style="display: none" type="text" id="aenderungsArt"
							name="aenderungsArt" value="kategorieDazu" name="aenderungsArt">
						<label for="kategorieName" class="text-primary m-1">Kategoriename:</label>
						<input type="text" class="form-control text-primary"
							id="kategorieName" placeholder="der erste Kategoriename"
							name="kategorieName"><br> <label
							for="kategorieElement" class="text-primary m-1">Ein
							erster Kategoriebegriff nach Empfänger <br> und Betreff von
							Überweisungen durchsucht werden:
						</label> <input type="text" class="form-control" id="kategorieElement"
							placeholder="Kategoriebegriff" name="kategorieElement"> <br>
						<div class="form-check" style="position: relative; left: 40%">
							<button type="submit" class="btn btn-primary">Speichern</button>
							<small class="form-text text-muted">Alternativ mit Enter
								abschicken</small>
						</div>

					</form>

				</div>
			</div>



		</my:kategorien>
	</div>
</div>


</body>
</html>