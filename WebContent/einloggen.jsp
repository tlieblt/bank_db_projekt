<html>
<head>
<meta name="author" content="Sven de Haan">
</head>
<jsp:include page="header.jsp" />
<body>
	<div class="container" style="max-width: 50%;">
		<form method="POST" action="Einloggen">
			<div class="form-group">
				<label for="EmailInput">Emailadresse</label> <input type="email"
					class="form-control" id="EmailInput" aria-describedby="emailHelp"
					placeholder="Mailadresse" name="mailadresse" required>
			</div>
			<div class="form-group">
				<label for="PasswordInput">Password</label> <input type="password"
					class="form-control" id="PasswordInput" placeholder="Passwort"
					required name="passwort"
					oninvalid="this.setCustomValidity('Leere Passwörter machen keinen Sinn, wir sind hier doch nicht bei Apple.')"
					oninput="setCustomValidity('')">
				<!-- Um den Server nicht zu überlasten :D -->
			</div>
			<div class="form-check"></div>
			<button type="submit" class="btn btn-primary">Einloggen</button>
		</form>
	</div>
</body>
</html>