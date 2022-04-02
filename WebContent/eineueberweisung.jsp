
<%@ taglib uri="WEB-INF/taglib/benutzer.tld" prefix="my"%>

	<jsp:include page="header.jsp" />
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
	
	
	<div class="card m-3 text-white bg-dark">
  <div class="card-body">
  <div class="container">
  <div class="row">
   <div class="col">
    <h5 class="card-title">Hier kann eine Überweisung von Ihnen erstellt werden und dem aktuell ausgewählten Konto hinzugefügt werden!</h5>
    </div>
    <div class="col m-3">
    <form action='AenderKId' class='form-select' method='POST'>
    	<input style="display: none" type="text" id="ursprungsseite"
					name="ursprungsseite" value="eineueberweisung.jsp">
   <my:kontoAuswahl></my:kontoAuswahl>
 		<input class='ml-2 btn btn-primary' type='submit'
							value='Konto wechseln'>

   </form>
   
   </div>
   </div>
   </div>
   
   <form method="POST" action="TransaktionErstellen">
				<input style="display: none" type="text" id="eineUeberweisung"
					name="eineUeberweisung" value="eineUeberweisung">
		<div class="row">
				<div class="col">
				<div class="form-group">
					<label for="iban">IBAN der anderen Partei:</label> <input
						type="text" class="form-control" id="iban"
						placeholder = "IBAN" name="iban" required>
				</div>
				</div>
				<div class="col">
				<div class="form-group">
					<label for="name">Name der anderen Partei:</label> <input
						type="text" class="form-control" id="name"
						placeholder = "Name" name="name" required>
				</div>
				</div>
				</div>
				<div class="row">
				<div class="col">
				<div class="form-group">
					<label for="betreff">Betreff:</label> <input type="text"
						class="form-control" id="betreff" placeholder="Betreff" 
						name="betreff">
				</div></div>
				<div class="col">
				<div class="form-group">
					<label for="betrag">Betrag: </label> <input type="text"
						class="form-control" id="betrag" placeholder="akzeptiert: 35,38 / 35.38 / 3538 Nicht: 35,38&euro;"
						name="betrag" required > <small class="form-text text-muted">Bitte geben sie den Betrag ohne Währungssymbol an!</small>
				</div>
				</div>
				</div>
				
				<div class="row">
				<div class="col">
				<div class="form-group">
					<label for="datum">Überweisungsdatum: </label> 
					<input type="date"
						class="form-control" id="datum" name="datum" required>
				</div>
				</div>
				<div class="col">
				<div class="form-group">
					<label for="zeit">Überweisungszeit:</label> 
					<input type="time"
						required class="form-control" min="0" id="zeit" name="zeit"
						style="-webkit-appearance: none; -moz-appearance: textfield;">
				</div>
				</div>
				</div>
					<div class="row">
					<div class="col" style="max-width:34%;">

					<div class="form-group">
						<label for="kategorie">Direkt einer Kategorie zuordnen?</label> <small
							class="form-text text-white">Dabei werden sowohl Betreff
							als auch Versender einer Kategorie hinzugefügt (falls nicht
							leer). </small> 
							<br/>
							<select class="form-control" id="kategorieName"
							name="kategorieName">
							
							<option value="">-</option>
							
							<c:forEach items="${ user.getKategorieNamen() }" var="kategorie">
								
								<option value="${ kategorie }">${ kategorie }</option>

							</c:forEach>
						</select>
					</div>
					</div>
					<div class="col"></div>


</div>
				
				<div class="form-check">
				<button type="submit" class="btn btn-primary">Überweisung speichern</button>
			  				</div>
			
			</form>
  				
  			
  
  </div>
  </div>
</body>
</html>