<html>
<head>
<meta name="author" content="Tobias Liebl">
<meta name="author" content="Sven de Haan">
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container">
	<form method="POST" action="Registrieren">
		<div class="form-group">
			<label for="EmailInput">Emailadresse</label> <input type="email"
				class="form-control" id="EmailInput" aria-describedby="emailHelp"
				placeholder="Mailadresse" name="EmailInput" required>
		</div>
		<div class="form-group">
			<label for="Password">Passwort</label> <input type="password"
				class="form-control" id="Passwort" placeholder="Passwort" required
				name="Passwort"
				oninvalid="this.setCustomValidity('Leere Passwörter machen keinen Sinn, wir sind hier doch nicht bei Apple.')"
				oninput="setCustomValidity('')">
		</div>
		<div class="form-group">
			<label for="PasswortRepeat">Passwort wiederholen: </label> <input
				type="password" class="form-control" id="PasswortRepeat"
				placeholder="Passwort wiederholen" required name="PasswortRepeat">
		</div>
		<div class="form-group">
			<label class="radio-inline">Welche Art von Account wollen Sie
				registrieren: </label> <input class="ml-2" checked type="radio"
				name="userart" id="User" value="User" required><label
				for="User"> User</label> <input class="ml-2" type="radio"
				name="userart" id="Admin" value="Admin" required><label
				for="Admin"> Admin</label> <input class="ml-2" type="radio"
				name="userart" id="Gast" value="Gast" required><label
				for="Gast"> Gast</label>
		</div>
		<div class="form-group">
			<label for="Vorname">Vorname: </label> <input type="text"
				class="form-control" id="Vorname" placeholder="Vorname"
				name="Vorname">
		</div>
		<div class="form-group">
			<label for="Nachname">Nachname: </label> <input type="text"
				class="form-control" id="Nachname" placeholder="Nachname"
				name="Nachname">
		</div>
		<div class="form-group">
			<label for="Alter">Alter</label> <input type="number"
				class="form-control" min="0" id="Alter" placeholder="Alter"
				name="Alter"
				style="-webkit-appearance: none; -moz-appearance: textfield;">
		</div>
		<div class="form-check"></div>
		<button type="submit" class="btn btn-primary">Registrieren</button>
	</form>
</div>

</body>
</html>