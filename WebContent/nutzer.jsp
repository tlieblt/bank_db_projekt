<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>
<html>
<head>
<meta name="author" content="Tobias Liebl">
<meta name="author" content="Sven de Haan">
</head>
<body>

<jsp:include page="header.jsp" />

<div id="datenAendern" class="container">
	<form method="POST" action="DatenAendern">
		<input style="display: none" type="text" id="aenderungsTyp"
			name="aenderungsTyp" value="daten"><br> <br>

		<div class="form-group">
			<label for="EmailInput">Emailadresse ändern</label> <input
				type="email" class="form-control" id="EmailInput"
				aria-describedby="emailHelp" value="${ user.getEmail() }"
				name="EmailInput" required>
		</div>
		<div class="form-group">
			<label for="Password">Bitte mit Passwort bestätigen</label> <input
				type="password" class="form-control" id="Passwort"
				placeholder="Passwort" required name="Passwort"
				oninvalid="this.setCustomValidity('Leere Passwörter machen keinen Sinn, wir sind hier doch nicht bei Apple.')"
				oninput="setCustomValidity('')">
		</div>

		<div class="form-group">
			<label class="radio-inline">Derzeit sind sie als ${ user.getRolle() }
				registriert. Wollen Sie ihre Accountart wechseln?: </label> <input
				class="ml-2" type="radio" name="userart" id="User" value="User"
				required><label for="User"> User</label> <input class="ml-2"
				type="radio" name="userart" id="Admin" value="Admin" required><label
				for="Admin"> Admin</label> <input class="ml-2" type="radio"
				name="userart" id="Gast" value="Gast" required><label
				for="Gast"> Gast</label>
		</div>
		<div class="form-group">
			<label for="Vorname">Vorname: </label> <input type="text"
				class="form-control" id="Vorname" value="${ user.getVorname() }"
				name="Vorname">
		</div>
		<div class="form-group">
			<label for="Nachname">Nachname: </label> <input type="text"
				class="form-control" id="Nachname" value="${ user.getNachname() }"
				name="Nachname">
		</div>
		<div class="form-group">
			<label for="Alter">Alter</label> <input type="number"
				class="form-control" min="0" id="Alter" value="${ user.getAlter() }"
				name="Alter"
				style="-webkit-appearance: none; -moz-appearance: textfield;">
		</div>
		<div class="form-check"></div>
		<button type="submit" class="btn btn-primary">Änderungen
			speichern</button>
	</form>

</div>
<p />

<div class="container">

	<form method="POST" action=KontoErstellen>

		<input style="display: none" type="text" id="aenderungsTyp"
			name="aenderungsTyp" value="konto"><br> <br>

		<div class="form-check"></div>
		<button type="submit" class="btn btn-primary">Neues Konto
			erstellen</button>
	</form>
	<p />


	<c:choose>
		<c:when test="${user.hatKeinKonto()}">
		</c:when>
		<c:otherwise>
			<div class="container">

				<form action='AenderKId' class='form-select' method='POST'>
					<input style="display: none" type="text" id="aenderungsArt"
						name="aenderungsArt" value="kontoLoeschen" name="aenderungsArt">
					<my:kontoAuswahl></my:kontoAuswahl>
					<input class='ml-2 btn btn-danger' type='submit'
						value='Ausgewähltes Konto löschen'>

				</form>

			</div>
		</c:otherwise>
	</c:choose>

	<p />




	<form id="deleteForm" method="POST" action="Loeschen">
		<input type="button" id="deletor" class=" btn btn-outline-danger"
			value="Account löschen" onclick="loeschDich()"
			style="position: relative; margin-top: 1%; margin-left: 48%;">
	</form>


	<script>
		
			//Anstatt dem üblichen langweiligen Popup um nachzufragen ob sicher gelöscht werden soll,
			//entschuldigt den schlechten Style kein extra JS File dafür zu erstellen
		    function loeschDich(){
		        var speicherknopf = document.getElementById("deletor");
		        speicherknopf.disabled = true;
		        let timerId = setInterval(() => springKnopf(), 333);
		        setTimeout(() => { clearInterval(timerId); knopfloeschend() }, 1999);
            }

		    function knopfloeschend(){
		    	
		    	var form = document.getElementById("deleteForm");		    	
                var speicherknopf = document.getElementById("deletor");
                speicherknopf.addEventListener("click", function () {
		    	  form.submit();
		    	});
                speicherknopf.style.position="absolute";
                speicherknopf.style.top = "10%";
                speicherknopf.style.right = "45%";
                speicherknopf.disabled = false;
                speicherknopf.value = "Account endgültig löschen?";
                speicherknopf.type="submit";
				window.scrollTo(0,0);                
		    }

		    function springKnopf(){
		        let dltr = document.getElementById('deletor');
		        let x = Math.random()*40+40;
		        let y = Math.random()*40+40;
		        dltr.style.position ="absolute";
		        dltr.style.top=y+"%";
		        dltr.style.right=x+"%";

		    }
		</script>
</div>
</body>
</html>
