
<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>
<html>
<head>
<meta name="author" content="Tobias Liebl">
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container-fluid">
	<div class="row">
		<div class="col-4">
			<h5 class="mt-4">Ihre Suchergebnisse:</h5>
		</div>

		<div class="col-4">
		<div style ="position:relative; left:10%; margin-top:2%;">
			<form action='AenderKId' class='form-select' method='POST'>
				<input style="display: none" type="text" id="ursprungsseite"
					name="ursprungsseite" value="suche.jsp">
				<my:kontoAuswahl></my:kontoAuswahl>
				<input class='ml-2 btn btn-primary' type='submit'
					value='Konto wechseln'>
			</form>
		</div>
		</div>
		<div class="col-4">
		<form method='POST' action='Sortieren'>
				<div class='form-group'>
					<input style='display: none' type='text' id='wasSortieren'
						name='wasSortieren' value='suche'>


					<button type='submit' class='btn btn-outline-success mt-2'>Sortieren</button>

				</div>
			</form>
		</div>



	</div>
</div>
<my:suchergebnisse>
</my:suchergebnisse>
</body>
</html>