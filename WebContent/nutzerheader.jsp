<html>
<head>
<meta name="author" content="Tobias Liebl">
<meta name="author" content="Sven de Haan">
</head>
<body>
<a class='navbar-link disabled text-info'>Hallo, ${ user.getVorname() }!</a>

<a class='navbar-link ml-3' href='kontouebersicht.jsp'>
	Kontoübersicht</a>
<a class="nav-link active ml-3" href="suche.jsp">Suche</a>

<div class="dropdown ml-3">
	<button class="btn dropdown-toggle text-primary" type="button"
		id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true"
		aria-expanded="false">Funktionen</button>
	<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
	
		<a class="dropdown-item" href="nutzer.jsp">Profildaten ändern</a> 
		<a class="dropdown-item" href="kategorien.jsp">Kategorien verwalten</a>
		<a class="dropdown-item" href="eineueberweisung.jsp">Eine Überweisung erstellen</a> 
		<a class="dropdown-item" href="KategorieDropDownKreisDiagramm">Kreisdiagramm erstellen</a> 
		<a class="dropdown-item" href="KategorieDropDownBalkenDiagramm">Balkendiagramm erstellen</a>
		<a class = "dropdown-item" href="SchreibeExcelDatei">Excel Export</a>
		<a class = "dropdown-item" href="LeseExcelDatei">Excel Import</a>
			
				
			
	</div>
</div>

<a class='navbar-link ml-3' style="position: absolute; left: 90%;"
	href='Ausloggen'>Ausloggen</a>
	</body>
</html>