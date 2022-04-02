
package tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import benutzer.Benutzer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Tag um Kategorien anzuzeigen
 * 
 * @author Tobias Liebl
 *
 */
public class KategorieTag extends TagSupport {
	static final long serialVersionUID = 1289347520984579752L;

	HashMap<String, String[]> kategorieMap;

	/**
	 * Holt die Suchergebnisse aus der Session um sie anzeigen zu können
	 * @param session
	 * @return
	 */
	public HashMap<String, String[]> getNutzerKategorien(HttpSession session) {

		Benutzer currUser = (Benutzer) session.getAttribute("user");

		HashMap<String, String[]> kategorien = currUser.getKategorien();

		if (kategorien == null || kategorien.isEmpty()) {

			// Ein Failsafe, falls keine Kategorien im Nutzer gespeichert sind
			HashMap<String, String[]> nochKeineKategorien = new HashMap<String, String[]>();
			String[] lr = {};
			nochKeineKategorien.put("asföjlaksfiasof", lr);

			return nochKeineKategorien;
		} else {

			return kategorien;
		}
	}

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		JspWriter out = pageContext.getOut();

		String neueKategorie = "</div><p><button class='btn btn-primary m-4' type='button'data-toggle='collapse' data-target='#collapseNeu'aria-expanded='false' aria-controls='collapseNeu'>Neue Kategorie anlegen:</button></p><div class='collapse m-4' id='collapseNeu'><div class='card card-body'><form method='POST' action='KategorienAendern'><input style='display: none' type='text' id='aenderungsArt'"
				+ "name='aenderungsArt' value='kategorieDazu' name='aenderungsArt'> <label for='kategorieName' class='text-primary m-1'>Kategoriename:</label> <input type='text' class='form-control text-primary' id='kategorieName' placeholder='Kategoriename'"
				+ "name='kategorieName'><br> <label for='kategorieElement' class='text-primary m-1'>Ein erster Kategoriebegriff nach dem Empfänger <br> und Betreff von Überweisungen durchsucht werden:</label> <input type='text' class='form-control' id='kategorieElement'placeholder='Kategoriebegriff' name='kategorieElement'> <br><div class='form-check' style='position: relative; left: 40%'>"
				+ "<button type='submit' class='btn btn-primary'>Speichern</button><small class='form-text text-muted'>Alternativ mit Enter abschicken</small></div></form></div>";

		String neueReihe = "<div class='row justify-content-start'>";

		try {

			kategorieMap = getNutzerKategorien(session);

			// wird aufgrund des Tabellenlayouts gebraucht
			// damit die Elemente der letzten Zeile nicht
			// z.B. den ganzen Screen einnehmen
			int anzahlKategorien = kategorieMap.size();
			int iBeiLetzterReihe = Math.floorDiv(anzahlKategorien, 3) * 3;
			int restElemente = anzahlKategorien - iBeiLetzterReihe;

			if (kategorieMap.containsKey("asföjlaksfiasof")) {
				return EVAL_BODY_INCLUDE;
			}

			else {
				int i = 0;

				out.println(neueKategorie);

//Es wird durch alle Kategorienamen iteriert 

				for (Iterator<Map.Entry<String, String[]>> entries = kategorieMap.entrySet().iterator(); entries
						.hasNext();) {
					Map.Entry<String, String[]> entry = entries.next();

					// Praktisch ein Escape Zeichen, damit der Nutzer Leerzeichen verwenden kann und
					// HTML funktioniert (keine Leerzeichen in Name/ID)
					String eineKategorie = entry.getKey().replace(" ", "89hjafoiuslkfj");

					// Damit das letzte oder das erste bzw. die ersten oder
					// letzten zwei Kategorie-Divs nicht die komplette
					// col-Breite ausfüllen, sondern einheitlich groß sind...
					if (i == iBeiLetzterReihe && restElemente == 1) {
						neueReihe = "<div class='row justify-content-start' style ='max-width:34%;'>";
					} else if (i == iBeiLetzterReihe && restElemente == 2) {
						neueReihe = "<div class='row justify-content-start' style ='max-width:68%;'>";
					}
					// nach jeder 3. Kategorie wird eine neue Zeile mit Kategorien angefangen
					if (i % 3 == 0) {
						out.println("</div>");
						out.println(neueReihe);
					}

					// Da die Kategorienamen Buttons sind die Divs öffnen, brauchen alle einen
					// eindeutigen Namen (es gibt keine doppelten Kategorienamen, werden überprüft)

					out.println(
							"<div class='col'><div class = 'card m-4 p-3  bg-success' style = 'max-width:90%;'><div class = 'card-body'>");

					out.println("<h5 class='card-title text-white'>Kategorie: " + entry.getKey() + "</h5>");

					out.println(
							"<button class='btn btn-info card-title mt-3' type='button' data-toggle='collapse' data-target=");
					out.print("'#" + eineKategorie + "' aria-expanded='false' aria-controls=");
					out.print("'" + eineKategorie + "'>Bearbeiten</button></p>");

					out.println("<div class='collapse' id='" + eineKategorie + "'>");
					out.println("<form  method='POST' action='KategorienAendern' ><div class='form-group' >");
					out.println(
							"<input style='display: none' type='text' id='aenderungsArt' name='aenderungsArt' value='kategorieUpdate' >");
					out.println(
							"<input style='display: none' type='text' id='kategorieName' name='kategorieName' value='"
									+ eineKategorie + "' >");

					// nun iteriert man über alle Kategorieelemente und gibt jedes in einem
					// Input Feld aus, damit sie auf Wunsch direkt bearbeitet werden können
					String[] dieKategorieElemente = entry.getValue();

					if (dieKategorieElemente != null && dieKategorieElemente.length > 0) {

						for (String el : dieKategorieElemente) {

							String n = "<input type = 'text' class ='form-control text-success m-1 p-1' value ='" + el
									+ "' name = '" + el + "' id = '" + el + "'>";
							out.println(n);

						}
					}

					// Input Feld um einen neuen Eintrag zu erstellen
					String neuEintrag = "<input type='text' class='form-control m-1' id='neuesElement' placeholder='neuer Suchbegriff' name='neuesElement'>";

					out.println(neuEintrag);

					out.println("<button type='submit' class='btn btn-primary mt-2'>Speichern</button>");

					out.println("</div></form>");

					out.println("<form method='POST' action='KategorienAendern'>");
					out.println(
							"<input style='display: none' type='text' id='kategorieName' name='kategorieName' value='"
									+ eineKategorie + "' >");
					out.println(
							"<input style='display: none' type='text' id='aenderungsArt' name='aenderungsArt' value='kategorieLoeschen' >");

					out.println("<button type='submit' class='btn btn btn-danger'>Kategorie löschen</button></form>");
					out.println("</div>");
					out.println("</div></div></div>");

					i++;
				}

			}

		} catch (NullPointerException e) {
		} catch (IOException e) {
		}

		return SKIP_BODY;

	}
}