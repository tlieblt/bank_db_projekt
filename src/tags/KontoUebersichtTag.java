package tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import benutzer.Benutzer;
import benutzer.Ueberweisung;
import manager.FehlerManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Ein Tag der alle Überweisungen die zu einem Konto momentan
 * im Nutzerobjekt der Session gespeichert sind
 * als Tabelle ausgibt und Betreff und Versender
 * nach den Kategoriebegriffen durchsucht und kategorisiert
 * 
 * @author Tobias Liebl
 */

public class KontoUebersichtTag extends TagSupport {
	static final long serialVersionUID = 1289347520984579752L;

	ArrayList<Ueberweisung> ueberweisungen;
	HashMap<String, String[]> kategorien;

	/**
	 * Hole Kategorien aus Session
	 * 
	 * @param session
	 * @return Kategorien von User
	 */
	public HashMap<String, String[]> getKategorien(HttpSession session) {
		Benutzer usi = (Benutzer) session.getAttribute("user");

		return usi.getKategorien();

	}

	/**
	 * Ueberweisungen aus Session anzeigen
	 * 
	 * @param session
	 * @return Ueberweisungen
	 */
	public ArrayList<Ueberweisung> getUeberweisungenInSession(HttpSession session) {

		ArrayList<Ueberweisung> fallsLeer = new ArrayList<Ueberweisung>();
		fallsLeer.add(new Ueberweisung("keine"));

		Benutzer user = (Benutzer) session.getAttribute("user");

		ArrayList<Ueberweisung> ueberweisungen = user.getCurKontoUeberweisungen();

		if (ueberweisungen != null) {
			if (ueberweisungen.size() > 0) {

				return ueberweisungen;
			}
		}
		return fallsLeer;

	}

	public int doStartTag() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession();
		JspWriter out = pageContext.getOut();

		String tabellenKopf = "<table class='table table-striped table-bordered'>"
				+ "<thead><tr><th scope='col'>#</th><th scope='col'>Versender</th>"
				+ "<th scope='col'>IBAN</th><th scope='col'>Beschreibung</th><th scope='col'>Betrag</th>"
				+ "<th scope='col'>Zeit</th><th scope='col'>Kategorie</th></tr></thead><tbody>";

		try {
			ueberweisungen = getUeberweisungenInSession(session);
			kategorien = getKategorien(session);

			if (ueberweisungen.get(0).getBetreff().equals("Keine Überweisungsdaten verfügbar.")) {

				return EVAL_BODY_INCLUDE;
			}

			else {
				int transaktionsSumme = 0;

				for (Ueberweisung tmpUeber : ueberweisungen) {
					transaktionsSumme += tmpUeber.getBetrag();}
				out.println("<div class='m-3'>");
				out.println("<h5>Die Gesamtsumme über die hier angezeigten Überweisungen ist: "
						+ FehlerManager.machBetragAusInt(transaktionsSumme) + "</h5>");
				out.println("</div>");

				
				int i = 1;

				out.println(tabellenKopf);


				for (Ueberweisung tmpUeber : ueberweisungen) {

					out.println("<tr><th scope='row'>" + i + "</th>");
					out.println("<td>" + tmpUeber.getVersender() + "</td>");
					out.println("<td>" + tmpUeber.getVersender_iban() + "</td>");
					out.println("<td>" + tmpUeber.getBetreff() + "</td>");
					out.println("<td>" + FehlerManager.machBetragAusInt(tmpUeber.getBetrag()) + "</td>");
					out.println("<td>" + tmpUeber.getTimestamp().toString().substring(0, 19) + "</td>");

					// Gibt es noch keine Kategorien, will der Nutzer ja vielleicht welche machen
					if (kategorien == null || kategorien.size() == 0) {
						out.println("<td><a href='kategorien.jsp' class='link-success'>Kategorien anlegen</a></td>");

					} else {
						ArrayList<String> treffer = new ArrayList<String>();

						// Für alle Kategorieelemente prüfen ob die Überweisung dazu passt
						for (var entry : kategorien.entrySet()) {

							for (String katEl : entry.getValue()) {
								if (tmpUeber.getBetreff().contains(katEl) || tmpUeber.getVersender().contains(katEl)) {
									// sobald eine Kategorie passt, merken wir uns die in treffer und stoppen die
									// weitere Suche in dieser Kategorie -> es wird weiter nach den anderen Kat.
									// gesucht
									treffer.add(entry.getKey());
									break;
								}
							}
						}
						String kategorieTreffer = "";
						for (String trf : treffer) {
							kategorieTreffer += "Kategorie: " + trf + "; ";
						}

						// hat bisher kein KategorieElement zu der Überweisung gepasst
						// teilen wir das dem Benutzer mit

						if (treffer.isEmpty()) {

							kategorieTreffer = "<a href='kategorien.jsp'>Nicht kategorisiert.  </a>";
						}

						out.println("<td>" + kategorieTreffer + "</td>");
					}
					i++;

				}

				out.println("</tbody></table>");


			}
		} catch (NullPointerException e) {
		} catch (IOException e) {
		}

		return SKIP_BODY;
	}

}